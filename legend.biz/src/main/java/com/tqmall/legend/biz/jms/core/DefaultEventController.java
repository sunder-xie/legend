package com.tqmall.legend.biz.jms.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 和rabbitmq通信的控制器，主要负责：
 * <p>1、和rabbitmq建立连接</p>
 * <p>2、声明exChange和queue以及它们的绑定关系</p>
 * <p>3、启动消息监听容器，并将不同消息的处理者绑定到对应的exchange和queue上</p>
 * <p>4、持有消息发送模版以及所有exchange、queue和绑定关系的本地缓存</p>
 * Created by guozhiqiang on 14-7-4.
 */
public class DefaultEventController implements EventController {

    public final static Logger logger = LoggerFactory.getLogger(DefaultEventController.class);

    private CachingConnectionFactory rabbitConnectionFactory;

    private EventControlConfig config;

    private CodecFactory defaultCodecFactory = new HessionCodecFactory();

    private List<SimpleMessageListenerContainer> msgListenerContainers =new ArrayList<>(); // rabbitMQ msg listener container

    private List<MessageAdapterHandler> msgAdapterHandlers = new ArrayList<>();

    private MessageConverter serializerMessageConverter = new SerializerMessageConverter(); // 直接指定
    //queue cache, key is exchangeName
    private Map<String, TopicExchange> exchanges = new HashMap<>();
    //queue cache, key is queueName
    private Map<String, Queue> queues = new HashMap<>();
    //bind relation of queue to exchange cache, value is exchangeName | queueName
    private Set<String> binded = new HashSet<>();

    private EventTemplate eventTemplate; // 给App使用的Event发送客户端

    private AtomicBoolean isStarted = new AtomicBoolean(false);


    public synchronized static DefaultEventController getInstance(EventControlConfig config) {
        return new DefaultEventController(config);
    }

    private DefaultEventController(EventControlConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config can not be null.");
        }
        this.config = config;
        initRabbitConnectionFactory();
        // 初始化RabbitTemplate
        RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory);
        rabbitTemplate.setMessageConverter(serializerMessageConverter);
        eventTemplate = new DefaultEventTemplate(rabbitTemplate, defaultCodecFactory, this);
    }

    /**
     * 初始化rabbitmq连接
     */
    private void initRabbitConnectionFactory() {
        logger.info("初始化MQ的Connection");
        logger.info("host=" + config.getServerHost());
        logger.info("port" + config.getPort());
        logger.info("username=" + config.getUsername());
        logger.info("virtualhost=" + config.getVirtualHost());
        rabbitConnectionFactory = new CachingConnectionFactory();
        rabbitConnectionFactory.setHost(config.getServerHost());
        rabbitConnectionFactory.setChannelCacheSize(config.getEventMsgProcessNum());
        rabbitConnectionFactory.setPort(config.getPort());
        rabbitConnectionFactory.setUsername(config.getUsername());
        rabbitConnectionFactory.setPassword(config.getPassword());
        if (!StringUtils.isEmpty(config.getVirtualHost())) {
            rabbitConnectionFactory.setVirtualHost(config.getVirtualHost());
        }
    }

    /**
     * 注销程序
     */
    public synchronized void destroy() throws Exception {
        if (!isStarted.get()) {
            return;
        }
        for (SimpleMessageListenerContainer smlc : msgListenerContainers) {
            smlc.stop();
        }
        eventTemplate = null;
        rabbitConnectionFactory.destroy();
    }

    @Override
    public void start() {
        if (isStarted.get()) {
            return;
        }
        initMsgListenerAdapter();
        isStarted.set(true);
    }

    /**
     * 初始化消息监听器容器
     */
    private void initMsgListenerAdapter() {
        logger.info("初始化消息监听器");
        for (MessageAdapterHandler messageAdapterHandler : msgAdapterHandlers) {
            MessageListener listener = new MessageListenerAdapter(messageAdapterHandler, serializerMessageConverter);
            SimpleMessageListenerContainer msgListenerContainer = new SimpleMessageListenerContainer();
            msgListenerContainer.setConnectionFactory(rabbitConnectionFactory);
            msgListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
            msgListenerContainer.setMessageListener(listener);
            msgListenerContainer.setErrorHandler(new MessageErrorHandler());
            msgListenerContainer.setPrefetchCount(config.getPrefetchSize()); // 设置每个消费者消息的预取值
            msgListenerContainer.setConcurrentConsumers(config.getEventMsgProcessNum());
            msgListenerContainer.setTxSize(config.getPrefetchSize());//设置有事务时处理的消息数
//            msgListenerContainer.setQueues(queues.values().toArray(new Queue[queues.size()]));
            msgListenerContainer.setQueues(queues.get(messageAdapterHandler.getQueueName()));
            msgListenerContainer.start();
            msgListenerContainers.add(msgListenerContainer);
        }
    }

    @Override
    public EventTemplate getEopEventTemplate() {
        return eventTemplate;
    }

    @Override
    public EventController add(String queueName, String exchangeName, EventProcessor eventProcessor) {
        return add(queueName, exchangeName, eventProcessor, defaultCodecFactory);
    }

    public EventController add(String queueName, String exchangeName, EventProcessor eventProcessor, CodecFactory codecFactory) {
        MessageAdapterHandler messageAdapterHandler = new MessageAdapterHandler();
        messageAdapterHandler.add(queueName, exchangeName, eventProcessor, codecFactory);
        if (!beBinded(exchangeName,queueName)){
            declareBinding(exchangeName,queueName);
        }
        msgAdapterHandlers.add(messageAdapterHandler);
        return this;
    }


    /**
     * exchange和queue是否已经绑定
     */
    protected boolean beBinded(String exchangeName, String queueName) {
        return binded.contains(exchangeName + "|" + queueName);
    }

    /**
     * 声明exchange和queue已经它们的绑定关系
     */
    protected synchronized void declareBinding(String exchangeName, String queueName) {
        String bindRelation = exchangeName + "|" + queueName;
        if (binded.contains(bindRelation)) return;

        boolean needBinding = false;
        TopicExchange topicExchange = exchanges.get(exchangeName);
        if (topicExchange == null) {
            topicExchange = new TopicExchange(exchangeName, false, true, null);
            exchanges.put(exchangeName, topicExchange);
            needBinding = true;
        }

        Queue queue = queues.get(queueName);
        if (queue == null) {
            queue = new Queue(queueName, true, false, false);
            queues.put(queueName, queue);
            needBinding = true;
        }

        if (needBinding) {
            BindingBuilder.bind(queue).to(topicExchange);//将queue绑定到exchange
            binded.add(bindRelation);
        }
    }

}

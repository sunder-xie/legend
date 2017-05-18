package com.tqmall;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by lixiao on 15/11/21.
 */
public class LegendSSLServer {

    public static void main(String[] args) {
        Server server = new Server(8082);

        // 设置ssl连接器
        SslSocketConnector ssl_connector = new SslSocketConnector();
        ssl_connector.setPort(8089);
        SslContextFactory sslContextFactory = ssl_connector.getSslContextFactory();
        sslContextFactory.setKeyStorePath("./legend.web/src/test/resources/keystore");
        sslContextFactory.setKeyStorePassword("zxc123");
        sslContextFactory.setKeyManagerPassword("zxc123");
        server.addConnector(ssl_connector);

        WebAppContext context = new WebAppContext();
        context.setContextPath("/legend");//应用访问Context
        //web.xml描述符号的位置，注意 eclipse和idea的位置路径不一样
        context.setDescriptor("./legend.web/src/main/webapp/WEB-INF/web.xml");
        //web资源的路径
        context.setResourceBase("./legend.web/src/main/webapp");
        context.setParentLoaderPriority(true);
        server.setHandler(context);

        try {
            System.out.println("[HINT] Don't forget to set VM options \" -Xms512m -Xmx1024m -XX:PermSize=512m -XX:MaxPermSize=512m -Djava.awt.headless=true \");");
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
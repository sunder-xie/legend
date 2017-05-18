package com.tqmall.legend.facade.sms.newsms;

import com.tqmall.legend.facade.sms.newsms.processor.presend.PreSendContext;
import com.tqmall.legend.facade.sms.newsms.processor.presend.ProcessorChain;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by majian on 16/11/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:sms_processor.xml")
public class ProcessorChainTest {
    @Autowired
    ProcessorChain processorChain;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void doProcess() throws Exception {
        processorChain.doProcess(new PreSendContext());
    }
}
package com.tqmall.legend.dao.customer;

import com.beust.jcommander.internal.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * Created by tanghao on 17/2/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-dal-context.xml")
public class CustomerCarRelDaoTest {
    @Autowired
    private CustomerCarRelDao customerCarRelDao;

    @Test
    public void CountTest(){
        Map param = Maps.newHashMap();
        param.put("shopId",1L);
        Integer num = customerCarRelDao.selectCount(param);
        System.out.println("---------------------"+num+"-----------------------------");
    }

}

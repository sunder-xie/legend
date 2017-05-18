package com.tqmall.legend.server;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/10/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-dubbo-content.xml")
public class BaseCaseTest {
    @Autowired
    protected DataSource dataSource;

    public List<Map<String, Object>> queryData(String sql) {
        List<Map<String, Object>> result = null;
        try {
            QueryRunner runner = new QueryRunner(dataSource);

            result = runner.query(sql, new MapListHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int updateData(String sql) {
        int result;
        try {
            QueryRunner runner = new QueryRunner(dataSource);

            result = runner.update(sql);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public Map<String,Object> getShopInfoMap(){
        String querySql = "select * from legend_shop where is_deleted= 'N' limit 1";
        List<Map<String, Object>> shopQueryMapList = queryData(querySql);
        Assert.assertNotNull(shopQueryMapList);
        Map<String,Object> shopQueryMap = shopQueryMapList.get(0);
        return shopQueryMap;
    }

    public Map<String,Object> getShopInfoMapByShopId(Long shopId){
        String querySql = "select * from legend_shop where is_deleted= 'N' and id = " + shopId;
        List<Map<String, Object>> shopQueryMapList = queryData(querySql);
        Assert.assertNotNull(shopQueryMapList);
        Map<String,Object> shopQueryMap = shopQueryMapList.get(0);
        return shopQueryMap;
    }
}

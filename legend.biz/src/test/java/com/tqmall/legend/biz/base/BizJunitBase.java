package com.tqmall.legend.biz.base;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by wushuai on 16/10/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-biz-context.xml")
public class BizJunitBase {
    @Autowired
    protected DataSource dataSource;

    public List<Map<String, Object>> queryData(String sql) throws SQLException {
        List<Map<String, Object>> result = null;
        QueryRunner runner = new QueryRunner(dataSource);
        result = runner.query(sql, new MapListHandler());
        return result;
    }

    public int updateData(String sql) throws SQLException {
        QueryRunner runner = new QueryRunner(dataSource);
        int result = runner.update(sql);
        return result;
    }

}

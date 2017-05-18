package com.tqmall.legend.dao.sys;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.sys.User;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现. 方法名称必须与Mapper.xml中保持一致.
 * 
 */
@MyBatisRepository
public interface UserDao extends BaseDao<User> {
    String selectByName(String name);
    //参数不同
    User selectById(String name);
    User selectById(User user);

    //插入数据
    int insert(User user);
    //更新数据
    int updateById(User user);
    //删除数据
    int deleteById(String name);
    int delete(User user);

}

package com.tqmall.legend.biz.sys;

import com.tqmall.legend.entity.sys.WhiteAddress;
import com.tqmall.legend.pojo.ShopManagerCom;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 白名单相关代码
 */

/**
 * @author litan
 *         <p/>
 *         2014年7月22日
 */
public interface WhiteAddressService {

    /**
     * 获取用户白名单信息
     *
     * @param userId
     * @param shopId
     * @return
     */
    public WhiteAddress getWhiteAddressInfo(Long userId, Long shopId);

    /**
     *
     * @param userId
     * @param shopId
     * @param loginTime 当前登陆时间
     * @return
     */
    public WhiteAddress getWhiteAddressInfo(Long userId, Long shopId , String loginTime);

    /**
     * create by jason 2015-08-24
     * 查询用户白名单信息
     *
     * @param ipAddress
     * @return
     */
    public WhiteAddress selectByIpAddress(String ipAddress);

    void saveWhiteAddress(WhiteAddress whiteAddress);

    void updateWhiteAddress(WhiteAddress whiteAddress);

    /**
     * 根据用户ids获取白名单列表
     *
     * @param userIdList
     * @return
     */
    List<WhiteAddress> selectByUserIds(List<Long> userIdList);
}

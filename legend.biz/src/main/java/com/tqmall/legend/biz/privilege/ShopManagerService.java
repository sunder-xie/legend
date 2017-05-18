package com.tqmall.legend.biz.privilege;

import com.google.common.base.Optional;
import com.tqmall.legend.api.entity.ShopManagerResp;
import com.tqmall.legend.biz.privilege.vo.ShopManagerAndLoginVo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.view.AddUser;
import com.tqmall.legend.pojo.ShopManagerCom;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by QXD on 2014/10/30.
 */
public interface ShopManagerService {
    /**
     * 根据店铺ID获取店铺的员工
     *
     * @param shopId 店铺id
     */
    public List<ShopManager> selectByShopId(long shopId);

    /**
     * 根据shopId 和manager_id 获取员工
     *
     * @param shopId    店铺id
     * @param managerId 员工id
     * @return ShopManager
     */
    public ShopManager selectByShopIdAndManagerId(long shopId, long managerId);

    /**
     * 根据shopId 和manager_id 获取员工
     * 缓存取不到去数据库取
     *
     * @param shopId    店铺id
     * @param managerId 员工id
     * @return ShopManager
     */
    public ShopManager selectByShopIdAndManagerIdDB(long shopId, long managerId);

    public List<ShopManager> select(Map<String, Object> parameters);

    /**
     * 查询一共多少员工数据
     * @param parameters
     * @return
     */
    Integer selectCount(Map<String, Object> parameters);


    List<ShopManager> selectWithoutCache(Map<String, Object> parameters);

    /**
     * 根据登录账号查询员工信息
     * @param account
     * @return
     */
    List<ShopManager> selectManagerInfoByAccount(String account);

    /**
     * 根据shopId 和manager_id 获取员工的信息
     *
     * @param shopId    店铺id
     * @param managerId 被请求员工id
     * @return ShopManagerCom
     */
    @Deprecated
    public ShopManagerCom selectUserInfoByShopIdAndManagerId(long shopId, long managerId);

    /**
     * 更新用户信息 本人更新
     *
     * @param shopManager 用户信息
     * @param managerId   更新操作用户
     */
    public Result upDataShopManager(long shopId, ShopManager shopManager, long managerId);


    /**
     * 更新用户信息 管理员更新
     *
     * @param shopManagerCom 用户信息
     * @param managerId      更新操作用户
     */
    public Result upDataShopManagerByAdmin(long shopId, ShopManagerCom shopManagerCom, long managerId);

    /**
     * 查询当前是管理员访问，还是普通用户访问
     *
     * @param currentLoginManageId 当前登录用户
     * @param managerId            被查询用户
     * @param isAdmin              管理员标示
     * @return 0表示访问自己页面，1表示管理员访问别人页面
     */
    public Integer getChannel(Integer isAdmin, long currentLoginManageId, long managerId);

    public ShopManager selectById(long id);


    /**
     * multiply query ShopManager
     *
     * @param shopManagerIds
     * @return
     */
    List<ShopManager> selectByIds(Long[] shopManagerIds);

    /**
     * 获取用户信息
     *
     * @param managerId 用户ID
     * @param shopId    店铺ID
     * @return
     */
    Optional<ShopManagerResp> getManagerInfo(Integer managerId, Long shopId);

    /**
     * 更新用户信息
     *
     * @param shopManager
     * @return
     */
    public Integer update(ShopManager shopManager);

    /**
     * 检查用户id是否存在
     *
     * @param userIdSet 用户id集合
     * @param shopId    店铺id
     * @return Set<Long> 返回存在的用户id
     */
    public Set<Long> checkExist(Long shopId, Set<Long> userIdSet);

    /**
     * 根据shopId获取门店管理员账号
     *
     * @param shopId
     * @return
     */
    ShopManager getAdmin(Long shopId);

    /**
     * 根据shopIds批量查询管理员信息
     *
     * @param shopIds
     * @return
     */
    List<ShopManager> selectAdminByShopIds(List<Long> shopIds);

    /**
     * 根据用户ids获取map
     *
     * @param shopManagerIds
     * @return
     */
    Map<Long, ShopManager> getMapByByIds(Long[] shopManagerIds);

    /**
     * @param shopId
     * @param roleId
     * @return
     */
    List<ShopManager> findByShopIdAndRoleId(Long shopId, Long roleId);

    /**
     * 获取个人信息
     *
     * @param shopId
     * @param managerId
     * @return
     */
    ShopManagerAndLoginVo findByShopIdAndManagerId(Long shopId, Long managerId);

    List<ShopManager> findByShopIdAndMobile(Long shopId, String mobile);

    /**
     * 添加员工
     * @param addUser
     */
    void saveShopManagerAandManagerLogin(AddUser addUser);

    /**
     * 更新员工（非管理员）
     * @param shopManagerAndLoginVo
     */
    void updateShopManager(ShopManagerAndLoginVo shopManagerAndLoginVo);

    /**
     * 更新员工（管理员）
     * @param shopManagerAndLoginVo
     */
    void updateShopManagerOfAdmin(ShopManagerAndLoginVo shopManagerAndLoginVo);

    /**
     * 删除员工
     * @param shopManagerAndLoginVo
     */
    void deleteShopManager(ShopManagerAndLoginVo shopManagerAndLoginVo);

    /**
     * 根据门店id和员工、手机号模糊查询
     * @param shopId 门店id
     * @param keyword 员工、手机号模糊
     * @return
     */
    List<ShopManager> findByNameOrMobile(Long shopId,String keyword);

    /**
     * 根据ids获取所有包括删除的员工
     * @param ids
     * @return
     */
    List<ShopManager> selectByIdsWithDeleted(Long[] ids);

}

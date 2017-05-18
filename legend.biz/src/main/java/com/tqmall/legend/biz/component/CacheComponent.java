package com.tqmall.legend.biz.component;

import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.core.utils.SerializeUtil;
import com.tqmall.legend.cache.CacheConstants;
import com.tqmall.legend.dao.customer.LicenseCityDao;
import com.tqmall.legend.dao.insurance.InsuranceCompanyDao;
import com.tqmall.legend.dao.order.OrderTypeDao;
import com.tqmall.legend.dao.privilege.ShopManagerDao;
import com.tqmall.legend.dao.settlement.PaymentDao;
import com.tqmall.legend.dao.shop.ShopServiceCateDao;
import com.tqmall.legend.entity.customer.LicenseCity;
import com.tqmall.legend.entity.insurance.InsuranceCompany;
import com.tqmall.legend.entity.order.OrderType;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.settlement.Payment;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Mokala on 6/16/15.
 */
@Slf4j
@Component("cacheComponent")
public class CacheComponent<T> implements ApplicationContextAware, InitializingBean {
    public static final String BIZ_CACHE_KEY = "BIZ_CACHE_KEY";

    private static String[] BIZ_KEYS = new String[]{CacheKeyConstant.ORDER_TYPE, CacheKeyConstant.INSURANCE_COMPANY,
            CacheKeyConstant.SERVICE_CATEGORY, CacheKeyConstant.PICKING, CacheKeyConstant.PAYMENT,
             CacheKeyConstant.LICENSE_CITY};
    private Map<String, DataLoader<?>> cacheMap;
    private ApplicationContext applicationCxt;
    /**
     * 读写锁,用于控制缓存内容读取
     */
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private boolean isDebug = false;

    /**
     * 因为jedisPoolUtils中的静态属性通过bean注入,为保证能初始时重载,不得如此
     */
    @Autowired
    JedisPoolUtils jedisPoolUtils;

    private void init() {
        cacheMap = new HashMap<>();
        /**
         * 业务类型缓存
         */
        register(CacheKeyConstant.ORDER_TYPE, new DataLoader<List<OrderType>>() {
            @Override
            public List<OrderType> loadData() {
                OrderTypeDao orderTypeDao = applicationCxt.getBean(OrderTypeDao.class);
                Map<String, Object> paramMap = new HashMap<String, Object>(1);
                List<String> sorts = new ArrayList<String>(1);
                sorts.add(" sort asc ");
                paramMap.put("sorts", sorts);
                return orderTypeDao.select(paramMap);
            }
        });
        /**
         * 保险公司缓存
         */
        register(CacheKeyConstant.INSURANCE_COMPANY, new DataLoader<List<InsuranceCompany>>() {
            @Override
            public List<InsuranceCompany> loadData() {
                InsuranceCompanyDao dao = applicationCxt.getBean(InsuranceCompanyDao.class);
                return dao.select(null);
            }
        });
        /**
         * 缓存服务类别
         */
        register(CacheKeyConstant.SERVICE_CATEGORY, new DataLoader<List<ShopServiceCate>>() {
            @Override
            public List<ShopServiceCate> loadData() {
                ShopServiceCateDao dao = applicationCxt.getBean(ShopServiceCateDao.class);
                return dao.select(null);
            }
        });
        /**
         * 缓存领料、采购人
         */
        register(CacheKeyConstant.PICKING, new DataLoader<List<ShopManager>>() {
            @Override
            public List<ShopManager> loadData() {
                ShopManagerDao dao = applicationCxt.getBean(ShopManagerDao.class);
                return dao.select(null);
            }
        });
        /**
         * 缓存结算方式
         */
        register(CacheKeyConstant.PAYMENT, new DataLoader<List<Payment>>() {
            @Override
            public List<Payment> loadData() {
                PaymentDao dao = applicationCxt.getBean(PaymentDao.class);
                return dao.select(null);
            }
        });

        /**
         * 缓存车牌与城市对应数据
         */
        register(CacheKeyConstant.LICENSE_CITY, new DataLoader<List<LicenseCity>>() {
            @Override
            public List<LicenseCity> loadData() {
                LicenseCityDao dao = applicationCxt.getBean(LicenseCityDao.class);
                return dao.select(null);
            }
        });
    }

    public T getCache(String cacheKey) {
        T cacheData = null;
        try {
            lock.readLock().lock();
            if (isDebug) {
                _clearCache(cacheKey);
            }
            cacheData = getFromCache(cacheKey);
            if(log.isDebugEnabled()){
                log.debug("get cache:{}, result:{}",cacheKey, cacheData.toString());
            }
        } catch (Exception e) {
            log.error("get cache object error:" + cacheKey, e);
        } finally {
            lock.readLock().unlock();
        }
        return cacheData;
    }

    private void register(String cacheKey, DataLoader dataLoader) {
        log.info("register biz dict:{}", cacheKey);
        cacheMap.put(cacheKey, dataLoader);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationCxt = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.init();
        this.reloadAll();
    }

    public void reload(String cacheKey) {
        this._clearCache(cacheKey);
        this._reLoad(cacheKey);
    }

    private void _reLoad(String cacheKey) {
        log.info("reload biz dict:{}", cacheKey);
        try {
            lock.writeLock().lock();
            DataLoader<?> dataLoader = cacheMap.get(cacheKey);
            if (dataLoader != null) {
                this._cacheToRedis(cacheKey);
            } else {
                log.warn("cache key is not exist:{}", cacheKey);
            }
        } catch (Exception e) {
            log.error("reload biz dict Fail!" + cacheKey, e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private T getFromCache(String cacheKey) {
        T cacheObject = _loadFromRedis(cacheKey);
        if (cacheObject == null) {
            return _cacheToRedis(cacheKey);
        } else {
            return cacheObject;
        }
    }

    private void _clearCache(String cacheKey) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            jedis.hdel(BIZ_CACHE_KEY.getBytes(), cacheKey.getBytes());
        } catch (Exception e) {
            log.error("del biz dict fail", e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }

    /**
     * 如果redis中目前没有值,则从数据加载器中加载值,并缓存至redis中
     *
     * @param cacheKey
     * @return
     */
    private T _cacheToRedis(String cacheKey) {
        log.info("cache {} to redis.", cacheKey);
        Jedis jedis = null;
        T cacheData = null;
        try {
            cacheData = (T) this.cacheMap.get(cacheKey).loadData();
            byte[] data = SerializeUtil.serialize(cacheData);
            jedis = JedisPoolUtils.getMasterJedis();
            jedis.hset(BIZ_CACHE_KEY.getBytes(), cacheKey.getBytes(), data);
            jedis.expire(BIZ_CACHE_KEY.getBytes(), CacheConstants.BIZ_DIR_KEY_EXP_TIME);
            return cacheData;
        } catch (Exception e) {
            log.error("set cache value to redis error.", e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
        return cacheData;
    }

    /**
     * 从redis中加载业务字典数据
     *
     * @param cacheKey 业务字典key
     * @return
     */
    private T _loadFromRedis(String cacheKey) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getSlaveJedis();
            byte[] data = jedis.hget(BIZ_CACHE_KEY.getBytes(), cacheKey.getBytes());
            if (data != null) {
                Object object = SerializeUtil.unserialize(data);
                return (T) object;
            }
        } catch (Exception e) {
            log.error("get cache from redis error", e);
        } finally {
            JedisPoolUtils.returnSlaveRes(jedis);
        }
        return null;
    }

    /**
     * 重载所有资源
     */
    public void reloadAll() {
        for (String key : BIZ_KEYS) {
            _reLoad(key);
        }
    }


}

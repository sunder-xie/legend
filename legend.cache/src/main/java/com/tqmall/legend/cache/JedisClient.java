package com.tqmall.legend.cache;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.SafeEncoder;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis原子操作封装，该bean作为单例对象
 */
@Component
@Slf4j
public class JedisClient {

    /**
     * 冲突延时 1000ms
     */
    private static final int MUTEX_EXP = 1000;
    private static final List<Class<?>> SIMPLE_CLASS_OBJ = Lists.newArrayList();

    static {
        SIMPLE_CLASS_OBJ.add(Number.class);
        SIMPLE_CLASS_OBJ.add(String.class);
        SIMPLE_CLASS_OBJ.add(Boolean.class);
    }

    @Resource
    private JedisPoolUtils jedisPoolUtils;


    private static boolean isSimpleObj(Class<?> classObj) {

        for (Class<?> c : SIMPLE_CLASS_OBJ) {
            if (c.isAssignableFrom(classObj))
                return true;
        }
        return false;
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public Boolean exists(final String key) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {
                return jedis.exists(key);
            }
        });
        return ret == null ? false : (Boolean) ret;
    }

    /**
     * 尝试获取锁并设置有效时间
     */
    public Boolean acquireLock(final String lock, final int expired) {

        final Object result = runTask(new Callback() {
            @Override
            public Boolean onTask(Jedis jedis) {
                long value = System.currentTimeMillis() + expired + 1;
                long acquired = jedis.setnx(lock, String.valueOf(value));
                if (acquired == 1) {
                    jedis.expire(lock, expired);
                    return true;
                }
                return false;
            }

        });
        return (Boolean) result;
    }


    public boolean set(final String key, final int seconds, final Object value) {
        if (key == null || value == null) {
            return false;
        }
        Object succeed = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                String ret;
                if (isSimpleObj(value.getClass())) {
                    if (seconds == 0) {//CACHE_EXP_FOREVER=0
                        ret = jedis.set(key, value.toString());
                    } else {
                        ret = jedis.setex(key, seconds, value.toString());
                    }
                } else {
                    byte[] bKey = SafeEncoder.encode(key);
                    byte[] bValue = serialize(value);

                    if (seconds == 0) {//CACHE_EXP_FOREVER=0
                        ret = jedis.set(bKey, bValue);
                    } else {
                        ret = jedis.setex(bKey, seconds, bValue);
                    }
                }
                return "OK".equals(ret);
            }
        });
        return succeed != null && (boolean) succeed;
    }

    /**
     * 设定一个hash对象
     *
     * @param key   哈希表中的key
     * @param field 域
     * @param value 值
     * @return 如果是第一次创建，则返回true，否则为false
     */

    public boolean setHash(final String key, final int seconds, final String field, final Object
            value) {
        if (key == null || field == null || value == null) {
            return false;
        }
        Object succeed = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                Long ret;
                if (isSimpleObj(value.getClass())) {
                    ret = jedis.hset(key, field, value.toString());
                } else {
                    byte[] bKey = SafeEncoder.encode(key);
                    byte[] bField = SafeEncoder.encode(field);
                    byte[] bValue = serialize(value);
                    ret = jedis.hset(bKey, bField, bValue);
                }
                if (seconds != 0) {//CACHE_EXP_FOREVER=0
                    jedis.expire(key, seconds);
                }
                return ret != null && ret == 1;
            }
        });
        return succeed != null && (boolean) succeed;
    }


    @SuppressWarnings("unchecked")
    public <T> T getHash(final String key, final String field, final Class<T> cls) {
        if (field == null)
            throw new IllegalArgumentException("field can not null");
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                Object obj = null;
                if (isSimpleObj(cls)) {
                    String str = jedis.hget(key, field);
                    if (str != null)
                        obj = createSimpleObj(str, cls);
                } else {
                    byte[] bs = jedis.hget(SafeEncoder.encode(key), SafeEncoder.encode(field));
                    if (bs != null) {
                        obj = deserialize(bs);
                    }
                }
                return obj;
            }
        });
        return ret == null ? null : (T) ret;
    }

    public <T> Map<String, T> getHashMap(String key, Class<T> cls, String... fields) {
        List<T> list = getHash(key, cls, fields);
        if (list == null) {
            return null;
        }
        Map<String, T> map = Maps.newHashMap();
        for (int i = 0; i < fields.length; i++) {
            map.put(fields[i], list.get(i));
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getHash(final String key, final Class<T> cls, final String... fields) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                final byte[][] bfields = new byte[fields.length][];
                for (int i = 0; i < bfields.length; i++) {
                    bfields[i] = SafeEncoder.encode(fields[i]);
                }
                List<byte[]> bytes = jedis.hmget(SafeEncoder.encode(key), bfields);
                if (bytes == null) return null;
                List<T> retList = Lists.newArrayList();
                boolean isSimple = isSimpleObj(cls);
                for (byte[] e : bytes) {
                    if (e == null) {
                        retList.add(null);
                        continue;
                    }
                    retList.add(isSimple ? createSimpleObj(SafeEncoder.encode(e), cls) : (T) deserialize(e));
                }
                return retList;
            }
        });
        return ret == null ? null : (List<T>) ret;
    }

    /**
     * 获取list的size
     */
    public long getListSize(final String key) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.llen(key);
            }
        });
        return ret == null ? 0 : (long) ret;
    }


    /**
     * 获取list所有元素
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getListRange(final String key) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.lrange(key, 0, jedis.llen(key) - 1);
            }
        });
        return ret == null ? Collections.<T>emptyList() : (List<T>) ret;
    }


    /**
     * 获取key下面所有的值，string的
     */
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getKeyAll(final String key) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.hgetAll(key);
            }
        });
        return ret == null ? Collections.<K, V>emptyMap() : (Map<K, V>) ret;
    }

    /**
     * 获取指定指定index的list存储对象
     */


    @SuppressWarnings("unchecked")
    public <T> T getList(final String key, final int index, final Class<T> cls) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                Object obj = null;
                if (isSimpleObj(cls)) {
                    String str = jedis.lindex(key, index);
                    if (str != null)
                        obj = createSimpleObj(str, cls);
                } else {
                    byte[] bs = jedis.lindex(SafeEncoder.encode(key), index);
                    if (bs != null) {
                        obj = deserialize(bs);
                    }
                }
                return obj;
            }
        });
        return ret == null ? null : (T) ret;
    }

    /**
     * 讲一批对象推送到list中
     */
    public boolean pushList(final String key, final int seconds, final Object... values) {
        if (key == null || values == null || values.length == 0) {
            return false;
        }
        Object succeed = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                Long ret;
                if (isSimpleObj(values.getClass())) {
                    String[] array = new String[values.length];
                    for (int i = 0; i < values.length; i++) {
                        array[i] = values[i].toString();
                    }
                    ret = jedis.lpush(key, array);
                } else {
                    byte[] bKey = SafeEncoder.encode(key);
                    byte[][] array = new byte[values.length][];
                    for (int i = 0; i < values.length; i++) {
                        array[i] = serialize(values[i]);
                    }
                    ret = jedis.lpush(bKey, array);
                }
                if (seconds != 0) {//CACHE_EXP_FOREVER=0
                    jedis.expire(key, seconds);
                }
                return ret != null && ret == 1;
            }
        });
        return succeed != null && (boolean) succeed;

    }

    @SuppressWarnings("unchecked")
    public <T> boolean lpushList(final String key, final Integer seconds, final T... values) {
        if (key == null || values == null || values.length == 0) {
            return false;
        }
        Object succeed = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                Long ret;
                byte[] bKey = SafeEncoder.encode(key);
                byte[][] array = new byte[values.length][];
                for (int i = 0; i < values.length; i++) {
                    array[i] = serialize(values[i]);
                }
                ret = jedis.lpush(bKey, array);
                if (seconds != 0) {//CACHE_EXP_FOREVER=0
                    jedis.expire(key, seconds);
                }
                return ret != null && ret > 0;
            }
        });
        return succeed != null && (boolean) succeed;
    }


    @SuppressWarnings("unchecked")
    public <T> List<T> rangeList(final String key, final Long start, final Long end) {
        if (key == null || start == null || end == null) {
            return Lists.newArrayList();
        }
        Object obj = runTask(new Callback() {

            @Override
            public Object onTask(Jedis jedis) {

                List<byte[]> rbyteList;
                byte[] bKey = SafeEncoder.encode(key);
                rbyteList = jedis.lrange(bKey, start, end);
                List<T> retList = Lists.newArrayList();
                for (byte[] rbyte : rbyteList) {
                    retList.add((T) deserialize(rbyte));
                }
                return retList;
            }
        });
        return (List<T>) obj;
    }


    public Boolean trimList(final String key, final Long start, final Long end) {
        if (key == null || start == null || end == null) {
            return Boolean.FALSE;
        }
        Object succeed = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                String ret;
                byte[] bKey = SafeEncoder.encode(key);
                ret = jedis.ltrim(bKey, start, end);
                return "OK".equals(ret);
            }
        });
        return succeed != null && (boolean) succeed;
    }


    @SuppressWarnings("unchecked")
    public <T> T rpopList(final String key) {
        if (key == null) {
            return null;
        }
        Object obj = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                byte[] bKey = SafeEncoder.encode(key);
                byte[] ret = jedis.rpop(bKey);
                return deserialize(ret);
            }
        });
        return (T) obj;
    }


    public Long lenList(final String key) {
        if (key == null) {
            return 0l;
        }
        Object obj = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                byte[] bKey = SafeEncoder.encode(key);
                Long ret = jedis.llen(bKey);
                return ret;
            }
        });
        return (Long) obj;
    }

    /**
     * 对hash表中的某个元素执行增加操作，如果操作的field非数字，则结果返回null.如果是负数，就是减少
     *
     * @param value 需要增加的值
     * @return 增加之后的值，如果操作的field非数字，则结果返回null
     */
    public Long incrHash(final String key, final String field, final long value) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.hincrBy(key, field, value);
            }
        });
        return ret == null ? null : (Long) ret;
    }


    public Double incrHash(final String key, final String field, final double value) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.hincrByFloat(key, field, value);
            }
        });
        return ret == null ? null : (Double) ret;
    }


    @SuppressWarnings("unchecked")
    public <T> T get(final String key, final Class<T> cls) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                Object obj = null;
                if (isSimpleObj(cls)) {
                    String str = jedis.get(key);
                    if (str != null)
                        obj = createSimpleObj(str, cls);
                } else {
                    byte[] bs = jedis.get(SafeEncoder.encode(key));
                    if (bs != null) {
                        obj = deserialize(bs);
                    }
                }
                return obj;
            }
        });
        return ret == null ? null : (T) ret;
    }

    //这里不判断简单类型和对象,和其他的有所不同,请注意
    public <T> Long sadd(final String key, final int seconds, final T value) {
        if (key == null || value == null) {
            return 0l;
        }
        Object rs = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                Long ret;
                byte[] bKey = SafeEncoder.encode(key);
                byte[] bValue = serialize(value);
                ret = jedis.sadd(bKey, bValue);
                if (seconds != 0) {//CACHE_EXP_FOREVER = 0
                    jedis.expire(key, seconds);
                }
                return ret == null ? 0 : ret;
            }
        });
        return (Long) rs;
    }


    @SuppressWarnings("unchecked")
    public <T> Set<T> smembers(final String key) {
        if (key == null) {
            return null;
        }
        Object rs = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                Set<byte[]> res;
                Set<T> ret = Sets.newHashSet();
                byte[] bkey = SafeEncoder.encode(key);
                res = jedis.smembers(bkey);
                for (byte[] elem : res) {
                    ret.add((T) deserialize(elem));
                }
                return ret;
            }
        });
        return (Set<T>) rs;

    }


    public Long slen(final String key) {
        if (key == null) {
            return 0l;
        }
        Object length = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                Long ret;
                byte[] bKey = SafeEncoder.encode(key);
                ret = jedis.scard(bKey);
                return ret == null ? 0 : ret;
            }
        });
        return (Long) length;
    }


    public Long hlen(final String key) {
        if (key == null) {
            return 0l;
        }
        Object length = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                Long ret;
                byte[] bKey = SafeEncoder.encode(key);
                ret = jedis.hlen(bKey);
                return ret == null ? 0 : ret;
            }
        });
        return (Long) length;
    }


    public <T> Boolean sisMember(final String key, final T val) {
        if (key == null || val == null) {
            return false;
        }
        Object isMember = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {
                Boolean ret;
                byte[] bKey = SafeEncoder.encode(key);
                byte[] bVal = serialize(val);
                ret = jedis.sismember(bKey, bVal);
                return ret == null ? false : ret;
            }
        });
        return (Boolean) isMember;
    }


    public <T> Long srem(final String key, final T val) {
        if (key == null || val == null) {
            return 0l;
        }
        Object rs = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                Long ret;
                byte[] bKey = SafeEncoder.encode(key);
                byte[] bVal = serialize(val);
                ret = jedis.srem(bKey, bVal);
                return ret == null ? 0 : ret;
            }
        });
        return (Long) rs;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T> T createSimpleObj(String arg, Class<T> cls) {
        T ret = null;
        Constructor[] constructors = cls.getDeclaredConstructors();
        for (Constructor t : constructors) {
            Class[] parameterTypes = t.getParameterTypes();
            if (parameterTypes.length == 1 && parameterTypes[0].equals(String.class)) {
                try {
                    ret = (T) t.newInstance(arg);
                } catch (Exception e) {
                    log.error("create simple obj error: " + e.getMessage(), e);
                }
                break;
            }
        }
        return ret;
    }


    public Long delete(final String key) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.del(key);
            }
        });
        return ret == null ? 0l : (long) ret;
    }


    public Long delHashField(final String key, final String field) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.hdel(key, field);
            }
        });
        return ret == null ? 0l : (long) ret;
    }


    public double incrBy(final String key, final double value) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.incrByFloat(key, value);
            }
        });
        return ret == null ? 0 : (double) ret;
    }


    public double decrBy(final String key, final double value) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.incrByFloat(key, -value);
            }
        });
        return ret == null ? 0 : (double) ret;
    }


    public long incrBy(final String key, final long value) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.incrBy(key, value);
            }
        });
        return ret == null ? 0l : (long) ret;
    }

    /**
     * 增加存储在字段中存储由增量键哈希的数量。
     * 如果键不存在，新的key被哈希创建。如果字段不存在，值被设置为0之前进行操作
     *
     * @param key
     * @param filed
     * @param value
     * @return
     */
    public long hinCrBy(final String key, final String filed, final long value) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.hincrBy(key, filed, value);
            }
        });
        return ret == null ? 0 : (long) ret;
    }

    /**
     * 设置值
     * @param key
     * @param filed
     * @param value
     * @return
     */
    public Object hset(final String key, final String filed, final String value) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.hset(key, filed, value);
            }
        });
        return ret;
    }

    /**
     * 批量设置hash
     * @param key
     * @param hash
     * @return
     */
    public Object hmset(final String key, final Map<String, String> hash) {
        if (Langs.isEmpty(hash)) {
            return null;
        }
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {
                return jedis.hmset(key, hash);
            }
        });
        return ret;
    }

    /**
     *
     * @param key
     * @param filed
     * @return
     */
    public Object hget(final String key, final String filed){
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {
                return jedis.hget(key, filed);
            }
        });
        return ret;
    }
    public long decrBy(final String key, final long value) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.incrBy(key, -value);
            }
        });
        return ret == null ? 0l : (long) ret;
    }


    public long incr(final String key) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.incr(key);
            }
        });
        return ret == null ? 0l : (long) ret;
    }

    /**
     * 获取资源key的锁，超过指定的时间无效，0表示无时间限制
     * 获取lock失败返回null
     *
     * @param key     资源name
     * @param timeout 超时时间, 单位ms
     * @return 返回与该锁配对的值，unlock时必须在key和该返回的value匹配上之后才可以unlock
     * 返回null表示lock已经被占用
     */
    public String lock(final String key, final long timeout) {
        Object value = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                String retValue = String.valueOf((long) (Math.random() * Long.MAX_VALUE));
                long lastTimeout = MUTEX_EXP;
                if (timeout > 0) {
                    lastTimeout = timeout;
                }
                String ret = jedis.set(key, retValue, "NX", "PX", lastTimeout);
                return "OK".equals(ret) ? retValue : null;
            }
        });
        return value == null ? null : (String) value;
    }


    public void unlock(final String key, final String lockVal) {
        runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                if (lockVal.equals(jedis.get(key))) {
                    jedis.del(key);
                }
                return null;
            }
        });
    }

    private Object runTask(Callback callback) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            return callback.onTask(jedis);
        } catch (JedisException e) {
            broken = handleJedisException(e);
        } catch (Exception e) {
            log.error("Redis runTask error: ", e);
        } finally {
            closeResource(jedis, broken);
            jedis = null;
        }
        return null;
    }

    private byte[] serialize(Object object) {
        if (!(object instanceof Serializable)) {
            throw new IllegalArgumentException("设定缓存的对象：" + object.getClass() + "无法序列化，确保 implements Serializable");
        }
        ObjectOutputStream objOS = null;
        ByteArrayOutputStream byteAOS = new ByteArrayOutputStream();
        try {
            objOS = new ObjectOutputStream(byteAOS);
            objOS.writeObject(object);
            return byteAOS.toByteArray();
        } catch (Exception e) {
            log.error("serialize error: " + e.getMessage());
        } finally {
            try {
                if (objOS != null) objOS.close();
                byteAOS.close();
            } catch (IOException e) {
                log.error("serialize close error : " + e.getMessage());
            }
        }
        return null;
    }

    private Object deserialize(byte[] bytes) {
        ByteArrayInputStream byteAIS = new ByteArrayInputStream(bytes);
        ObjectInputStream objIS = null;
        try {
            objIS = new ObjectInputStream(byteAIS);
            return objIS.readObject();
        } catch (Exception e) {
            log.error("deserialize error: " + e.getMessage());
        } finally {
            try {
                byteAIS.close();
                if (objIS != null) objIS.close();
            } catch (IOException e) {
                log.error("deserialize close error: " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * The TTL command returns the remaining time to live in seconds of a key
     * that has an {expire(String, int) EXPIRE} set. This introspection
     * capability allows a Redis client to check how many seconds a given key
     * will continue to be part of the dataset.
     *
     * @param key key
     * @return Integer reply, returns the remaining time to live in seconds of a
     * key that has an EXPIRE. In Redis 2.6 or older, if the Key does
     * not exists or does not have an associated expire, -1 is returned.
     * In Redis 2.8 or newer, if the Key does not have an associated
     * expire, -1 is returned or if the Key does not exists, -2 is
     * returned.
     */
    public Long ttl(final String key) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.ttl(key);
            }
        });
        return ret == null ? 0l : (long) ret;
    }


    public Long expire(final String key, final int seconds) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.expire(key, seconds);
            }
        });
        return ret == null ? 0l : (long) ret;
    }


    public String flushDB(final int dbIndex) {
        Object ret = runTask(new Callback() {
            @Override
            public Object onTask(Jedis jedis) {

                return jedis.flushDB();
            }
        });
        return ret == null ? "0" : (String) ret;
    }

    interface Callback {
        Object onTask(Jedis jedis);
    }


    /**
     * Handle jedisException, write log and return whether the connection is broken.
     */
    private boolean handleJedisException(JedisException jedisException) {
        if (jedisException instanceof JedisConnectionException) {
            log.error("Redis connection lost.", jedisException);
        } else if (jedisException instanceof JedisDataException) {
            if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1)) {
                log.error("Redis connection are read-only slave.", jedisException);
            } else {
                // dataException, isBroken=false
                return false;
            }
        } else {
            log.error("Jedis exception happen.", jedisException);
        }
        return true;
    }

    /**
     * Return jedis connection to the pool, call different return methods depends on the conectionBroken status.
     */
    private void closeResource(Jedis jedis, boolean conectionBroken) {
        try {
            if (conectionBroken) {
                jedisPoolUtils.returnMasterBrokenRes(jedis);
            } else {
                jedisPoolUtils.returnMasterRes(jedis);
            }
        } catch (Exception e) {
            log.error("return back jedis failed, will fore close the jedis.", e);
            jedis.close();
        }
    }
}


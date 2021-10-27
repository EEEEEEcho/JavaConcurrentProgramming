package com.echo.juc.chapter7;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 测试读写锁保证的缓存一致性
 */
public class TestGenericDao {
}

class GenericDaoCached extends GenericDao{
    private GenericDao dao = new GenericDao();
    //用map实现缓存
    private Map<SqlPair,Object> map = new HashMap<>();
    //创建读写锁
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

    @Override
    public <T> List<T> queryList(Class<T> beanClass, String sql, Object... args) {
        return super.queryList(beanClass, sql, args);
    }

    @Override
    public <T> T queryOne(Class<T> beanClass, String sql, Object... args) {
       //查询时，应该先在缓存中找，找到直接返回，找不到查询数据库,同时加读锁

        //加读锁
        rw.readLock().lock();
        SqlPair sqlPair;
        try {
            //先在缓存中找，找到直接返回
            sqlPair = new SqlPair(sql, args);
            T value = (T) map.get(sqlPair);
            if (value != null){
                //缓存中有
                return value;
            }
        }
        finally {
            rw.readLock().unlock();
        }

        //加写锁
        rw.writeLock().lock();
        try {
            //缓存中没有，查询数据库
            T value = (T) map.get(sqlPair);
            if (value == null){     //double check
                value = dao.queryOne(beanClass, sql, args);
                //放到缓存中。
                map.put(sqlPair,value);
            }
            //返回
            return value;
        }
        finally {
            rw.writeLock().unlock();
        }

    }

    @Override
    public int update(String sql, Object... args) {
//        //修改时，清空缓存再修改，这样造成的问题更大
//        //清空缓存
//        map.clear();
//        //然后执行更新并返回结果
//        return super.update(sql, args);
        //应该先更新，再清理缓存,同时加锁
        //上写锁
        rw.writeLock().lock();
        try {
            int update = super.update(sql, args);
            map.clear();
            return update;
        }
        finally {
            rw.writeLock().unlock();
        }
    }

    class SqlPair{
        private String sql;
        private Object[] args;
        public SqlPair(String sql,Object[] args){
            this.sql = sql;
            this.args = args;
        }
    }
}
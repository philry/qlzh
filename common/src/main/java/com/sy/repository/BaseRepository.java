package com.sy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * 这是用来定义一些JpaRepsitory接口中没有定义的一些方法的接口
 * 可以自行添加其中的方法，但是方法应该是具有通用性的方法
 * 而不是一些针对某些特定个业务逻辑的方法
 * 其它的持久层接口继承这个自定义接口而不是JpaRepository
 *
 * @param <T>  实体类型
 * @param <ID> 实体的主键类型
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    /**
     * 用于查找列表信息 可以是单表查询，可以是多表关联
     * 底层调用的就是SpringJDBC中的方法
     *
     * @param sql    要执行的SQL语句
     * @param clazz  查询后返回的对象的类型（不一定需要是Enity实体类型）
     * @param params SQL语句中得到占位符参数的值
     * @param <E>    泛型参数
     * @return 查询出来的列表对象
     */
    <E> List<E> queryBeanList(String sql, Class<E> clazz, Object... params);

    /**
     * 用于单个对象的信息 可以是单表查询，可以是多表关联，也可以是聚合函数的值
     * 底层调用的就是SpringJDBC中的方法
     *
     * @param sql    要执行的SQL语句
     * @param clazz  查询后返回的对象的类型（JavaBean，简单类型均可）
     * @param params SQL语句中得到占位符参数的值
     * @param <E>    泛型参数
     * @return 查询出来的列表对象
     */
    <E> E queryBean(String sql, Class<E> clazz, Object... params);
}
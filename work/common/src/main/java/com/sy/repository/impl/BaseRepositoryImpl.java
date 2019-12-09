package com.sy.repository.impl;

import com.sy.repository.BaseRepository;
import com.sy.utils.ApplicationContextUtils;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * BaseRepository的具体实现
 *
 * @param <T>  实体类类型
 * @param <ID> 主键类型
 */
public class BaseRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements BaseRepository<T, ID> {

    private EntityManager entityManager;
    private final Class<T> domainClass;
    //此处的JdbcTemplate在添加了spring-boot-starter-jdbc后自动注入，但不能直接	//Autowired,需要通过容器对象进行获取


    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {

        super(domainClass, entityManager);
        this.domainClass = domainClass;
        this.entityManager = entityManager;
    }


    /**
     * BaseRepository接口中queryBeanList方法的具体实现
     *
     * @param sql    要执行的SQL语句
     * @param clazz  查询后返回的对象的类型（不一定需要是Enity实体类型）
     * @param params SQL语句中得到占位符参数的值
     * @param <E>    泛型参数
     * @return 查询出来的列表
     */
    @Override
    public <E> List<E> queryBeanList(String sql, Class<E> clazz, Object... params) {
        return ApplicationContextUtils.getBean(JdbcTemplate.class).query(sql, new BeanPropertyRowMapper<>(clazz), params);
    }

    /**
     * BaseRepository接口中queryBean方法的具体实现
     *
     * @param sql    要执行的SQL语句
     * @param clazz  查询后返回的对象的类型（JavaBean，简单类型均可）
     * @param params SQL语句中得到占位符参数的值
     * @param <E>    泛型参数
     * @return 查询出来的对象
     */
    @Override
    public <E> E queryBean(String sql, Class<E> clazz, Object... params) {
        List<E> list = queryBeanList(sql, clazz, params);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

}

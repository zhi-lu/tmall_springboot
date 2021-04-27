package com.luzhi.tmall.dao;

import com.luzhi.tmall.pojo.Order;
import com.luzhi.tmall.pojo.OrderItem;
import com.luzhi.tmall.pojo.Product;
import com.luzhi.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 定义interface method使用Jpa的命名方式.. 通过id不用传递Sort对象
 */
public interface OrderItemDAO extends JpaRepository<OrderItem, Integer> {

    /**
     * 获取OrderItem List对象......
     *
     * @param order 获取相关的order对象
     *              通过Order逆排序获取对象
     * @return List<OrderItem>返回该对象
     * @see #findByOrderOrderByIdDesc(Order)
     */
    List<OrderItem> findByOrderOrderByIdDesc(Order order);

    /**
     * 获取相关OrderItem List对象
     *
     * @param product 通过Order对象获取相关对象
     * @return 返回关于OrderItem列表对象List<Object>
     * @see #findByProduct(Product)
     */
    List<OrderItem> findByProduct(Product product);

    /**
     * 通过jpa命名的相关方法.....
     *
     * @param user 获取相关的user对象
     * @return 返回OrderItem列表对象
     * @see #findByUserAndOrderIsNull(User)
     */
    List<OrderItem> findByUserAndOrderIsNull(User user);
}

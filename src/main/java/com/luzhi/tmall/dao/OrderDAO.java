package com.luzhi.tmall.dao;

import com.luzhi.tmall.pojo.Order;
import com.luzhi.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 创建订单的dao层
 */
public interface OrderDAO extends JpaRepository<Order, Integer> {

    /**
     * 使用Jpa命名大法,不通过Order Id进行排序.获取只通过当前用户和订单的状态
     * 此接口方法查找用户下的订单,不包括已经删除的订单(delete)...
     *
     * @param user   获取当前session中User对象.
     * @param status 获取当前订单的状态.是不是规定status(不返回到列表对象中)...
     * @return 返回 Order的List
     * @see #findByUserAndStatusNotOrderByIdDesc(User, String)
     */
    List<Order> findByUserAndStatusNotOrderByIdDesc(User user, String status);
}

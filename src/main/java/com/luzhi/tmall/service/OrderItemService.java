package com.luzhi.tmall.service;

import com.luzhi.tmall.dao.OrderItemDAO;
import com.luzhi.tmall.pojo.Order;
import com.luzhi.tmall.pojo.OrderItem;
import com.luzhi.tmall.pojo.Product;
import com.luzhi.tmall.pojo.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 创建订单项的服务层
 */
@Service
@SuppressWarnings("unused")
@CacheConfig(cacheNames = "orderItems")
public class OrderItemService {

    @Resource
    OrderItemDAO orderItemDao;

    @Resource
    ProductImageService productImageService;

    /**
     * @see #fill(List)
     * 获取整个订单组合,进行fill处理
     */
    public void fill(List<Order> orderList) {

        for (Order order : orderList) {
            fill(order);
        }
    }

    /**
     * @see #update(OrderItem)
     * 更新订单项
     */
    @CacheEvict(allEntries = true)
    public void update(OrderItem orderItem) {
        orderItemDao.save(orderItem);
    }

    /**
     * @see #get(int)
     * 通过id获取相关OrderItem 订单项
     */
    @Cacheable(key = "'orderItems-one-' + #p0")
    public OrderItem get(int id) {
        return orderItemDao.getOne(id);
    }

    /**
     * @see #delete(int)
     * 通过id删除对象
     */
    @CacheEvict(allEntries = true)
    public void delete(int id) {
        orderItemDao.delete(id);
    }

    /**
     * @see #add(OrderItem)
     * 添加对象。。。
     */
    @CacheEvict(allEntries = true)
    public void add(OrderItem orderItem) {
        orderItemDao.save(orderItem);
    }

    /**
     * @see #fill(Order)
     * 重载方法fill()对订单项进行处理获取价格和产品数目
     * 设置显示的图片为相关的购买产品
     */
    public void fill(Order order) {
        List<OrderItem> orderItemList = orderItemDao.findByOrderOrderByIdDesc(order);
        float totalPrice = 0.00f;
        int totalNumber = 0;
        for (OrderItem orderItem : orderItemList) {
            // 获取价格
            totalPrice += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            totalNumber += orderItem.getNumber();
            productImageService.setFirstProductImage(orderItem.getProduct());
        }
        order.setTotalPrice(totalPrice);
        order.setOrderItemList(orderItemList);
        order.setTotalNumber(totalNumber);
    }

    /**
     * @see #getSaleCount(Product)
     * 通过相关的Product对象获取相关的评论数量...
     */
    public int getSaleCount(Product product) {
        List<OrderItem> orderItemList = findByProduct(product);
        int result = 0;
        for (OrderItem orderItem : orderItemList) {
            if (null != orderItem.getOrder()) {
                if (null != orderItem.getOrder() && null != orderItem.getOrder().getPayDate()) {
                    result += orderItem.getNumber();
                }
            }
        }
        return result;
    }

    /**
     * @see #findByOrder(Order)
     * 通过order获取其下的订单项.
     */
    @Cacheable(key = "'orderItems-oid-' + #p0.id")
    public List<OrderItem> findByOrder(Order order) {
        return orderItemDao.findByOrderOrderByIdDesc(order);
    }

    /**
     * @see #findByProduct(Product)
     * 通过Product对象获取OrderItem 列表对象List<Object>
     */
    @Cacheable(key = "'orderItems-pid-' + #p0.id")
    public List<OrderItem> findByProduct(Product product) {
        return orderItemDao.findByProduct(product);
    }

    /**
     * @see #findByUser(User)
     * 通过相关的User对象获取相关的订单项列表.....
     */
    @Cacheable(key = "'orderItems-uid-' + #p0.id")
    public List<OrderItem> findByUser(User user) {
        return orderItemDao.findByUserAndOrderIsNull(user);
    }
}

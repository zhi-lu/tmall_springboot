package com.luzhi.tmall.service;

import com.luzhi.tmall.dao.OrderDAO;
import com.luzhi.tmall.pojo.Order;
import com.luzhi.tmall.pojo.OrderItem;
import com.luzhi.tmall.pojo.User;
import com.luzhi.tmall.util.Page4Navigator;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * Order 的 Service 层..............
 * 里面有一些细节需要进行处理
 */
@Service
@CacheConfig(cacheNames = "orders")
public class OrderService {
    public static final String WAIT_PAY = "waitPay";
    public static final String WAIT_DELIVERY = "waitDelivery";
    public static final String WAIT_CONFIRM = "waitConfirm";
    public static final String WAIT_REVIEW = "waitReview";
    public static final String FINISH = "finish";
    public static final String DELETE = "delete";

    @Resource
    OrderDAO orderDao;

    @Resource
    OrderItemService orderItemService;

    /**
     * @see #list(int, int, int)
     * 排序进行查找
     */
    @Cacheable(key = "'orders-page-' + #p0 + '-' + #p1")
    public Page4Navigator<Order> list(int start, int size, int navigatePages) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Order> pageFromJpa = orderDao.findAll(pageable);
        return new Page4Navigator<>(pageFromJpa, navigatePages);
    }

    /**
     * @see #removeOrderFromOrderItem(List)
     * 获取order 对象list. 除去 order 对象下 orderItem 的"order"对象
     * SpringMVC(MVC)会使用RESTFUL注解把order对象json化.
     * 不使用@JsonIgnoreProperties 在对redis整合时会出现一些问题........
     * order下有orderItem对象.orderItem也会json化. orderItem 也有order ..........
     * 禁止套娃.
     */
    public void removeOrderFromOrderItem(List<Order> orderList) {
        for (Order order : orderList) {
            removeOrderFromOrderItem(order);
        }
    }

    /**
     * @see #removeOrderFromOrderItem(Order)
     * 该重载方法具体如何将orderItem 对象下的order属性设置为null
     * 将 private 改为public为计算订单的总金额........
     */
    public void removeOrderFromOrderItem(Order order) {
        List<OrderItem> orderItemList = order.getOrderItemList();
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrder(null);
        }
    }

    /**
     * @see #get(int)
     * 获取order对象
     */
    @Cacheable(key = "'orders-ono-' + #p0")
    public Order get(int oid) {
        return orderDao.findOne(oid);
    }

    /**
     * @see #update(Order)
     * 更新对象
     */
    @CacheEvict(allEntries = true)
    public void update(Order order) {
        orderDao.save(order);
    }

    /**
     * @see #update
     * 添加对象
     */
    @CacheEvict(allEntries = true)
    public void add(Order order) {
        orderDao.save(order);
    }

    /**
     * @see #add(Order, List)
     * 生成订单
     * <p>
     * 通过注解@Transactional 对事务进行管理
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
    public float add(Order order, List<OrderItem> orderItemList) {

        float total = 0.00f;
        add(order);
        // 遍历订单项获取总金额.......
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrder(order);
            orderItemService.update(orderItem);
            total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
        }
        return total;
    }

    /**
     * 返回"处理好的"订单列表.无以及delete状态的订单...
     *
     * @see #listByUserWithOutDeleted(User)
     * 对该订单价格和数量,图片的处理交由请看
     * @see OrderItemService#fill(Order)
     */
    public List<Order> listByUserWithOutDeleted(User user) {
        List<Order> orderList = findByUserAndNotDeleted(user);
        orderItemService.fill(orderList);
        return orderList;
    }

    /**
     * 具体实现order dao 层中规定的jpa方法
     *
     * @see #findByUserAndNotDeleted(User)
     * 详情请看:
     * @see OrderDAO#findByUserAndStatusNotOrderByIdDesc(User, String)
     */
    @Cacheable(key = "'orders-uid-' + #p0.id")
    public List<Order> findByUserAndNotDeleted(User user) {
        return orderDao.findByUserAndStatusNotOrderByIdDesc(user, OrderService.DELETE);
    }

    /**
     * 计算已经确认的支付order用户所支付的总金额
     *
     * @param order 获取Order对象通过OrderItem遍历获取的其下的OrderItem列表
     *              获取并设置order的总金额。。
     * @see #calc(Order)
     */
    public void calc(Order order) {
        List<OrderItem> orderItemList = order.getOrderItemList();
        float totalPrice = 0.00f;
        for (OrderItem orderItem : orderItemList) {
            totalPrice += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
        }
        order.setTotalPrice(totalPrice);
    }
}

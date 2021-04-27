package com.luzhi.tmall.web;

import com.luzhi.tmall.pojo.Order;
import com.luzhi.tmall.service.OrderItemService;
import com.luzhi.tmall.service.OrderService;
import com.luzhi.tmall.util.Page4Navigator;
import com.luzhi.tmall.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 生成订单控制层,只提供查询.
 * 删除和增加交由由前端完成..
 */
@RestController
public class OrderController {

    @Resource
    OrderService orderService;
    @Resource
    OrderItemService orderItemService;

    /**
     * @see #list(int, int)
     * 通过在Service层实现的order 和 orderItem Service层方法
     * 获取相关的产品数量和金额除去"套娃"。。。。。。。。。。.....
     */
    @GetMapping("/orders")
    public Page4Navigator<Order> list(@RequestParam(value = "start", defaultValue = "0") int start,
                                      @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {

        start = Math.max(0, start);
        Page4Navigator<Order> orderPage4Navigator = orderService.list(start, size, 5);
        orderItemService.fill(orderPage4Navigator.getContent());
        orderService.removeOrderFromOrderItem(orderPage4Navigator.getContent());
        return orderPage4Navigator;
    }

    /**
     * @see #deliveryOrder(int)
     * 修改相关属性
     */
    @SuppressWarnings("RedundantThrows")
    @PutMapping("deliveryOrder/{oid}")
    public Object deliveryOrder(@PathVariable("oid") int oid) throws IOException {
        Order order = orderService.get(oid);
        order.setDeliveryDate(new Date());
        order.setStatus(OrderService.WAIT_CONFIRM);
        orderService.update(order);
        return Result.success();
    }
}

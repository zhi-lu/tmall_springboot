package com.luzhi.tmall.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import javax.persistence.*;
import java.util.Date;

import com.luzhi.tmall.service.OrderService;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 创建一个order类的pojo层
 */
@Entity
@Table(name = "order_")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@SuppressWarnings("unused")
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @Column(name = "orderCode")
    private String orderCode;

    @Column(name = "address")
    private String address;

    @Column(name = "post")
    private String post;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "userMessage")
    private String userMessage;

    @Column(name = "createDate")
    private Date createDate;

    @Column(name = "payDate")
    private Date payDate;

    @Column(name = "deliveryDate")
    private Date deliveryDate;

    @Column(name = "confirmDate")
    private Date confirmDate;

    @Column(name = "status")
    private String status;
    /**
     * @see #orderItemList
     * 该参数表示订单下的订单项目
     */
    @Transient
    private List<OrderItem> orderItemList;

    /**
     * @see #totalNumber
     * 该参数表示该订单的总计数量
     */
    @Transient
    private int totalNumber;

    /**
     * @see #totalPrice
     * 该参数表示订单的总计价格
     */
    @Transient
    private float totalPrice;
    /**
     * @see #statusDesc
     * 该参数表示订单的状态
     */
    @Transient
    private String statusDesc;

    public String getStatusDesc() {
        if (null != statusDesc) {
            return statusDesc;
        }
        String desc;
        switch (status) {
            case OrderService.WAIT_PAY:
                desc = "等待支付";
                break;
            case OrderService.WAIT_DELIVERY:
                desc = "等待发货";
                break;
            case OrderService.WAIT_CONFIRM:
                desc = "等待收货";
                break;
            case OrderService.WAIT_REVIEW:
                desc = "等待评价";
                break;
            case OrderService.FINISH:
                desc = "完成";
                break;
            case OrderService.DELETE:
                desc = "删除";
                break;
            default:
                desc = "未知";
        }
        statusDesc = desc;
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile() {
        return mobile;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public String getPost() {
        return post;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

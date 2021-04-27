package com.luzhi.tmall.web;

import com.luzhi.tmall.comparator.*;
import com.luzhi.tmall.pojo.*;
import com.luzhi.tmall.service.*;
import com.luzhi.tmall.util.Result;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/26
 * 创建相关的REST,与前端数据交互的控制器。
 * 当然交互是一定需要相关的json像退出这样的功能就不要在这里进行实现了..
 */
@RestController
public class ForeRestController {

    @Resource
    CategoryService categoryService;

    @Resource
    ProductService productService;

    @Resource
    UserService userService;

    @Resource
    ProductImageService productImageService;

    @Resource
    ReviewService reviewService;

    @Resource
    OrderService orderService;

    @Resource
    OrderItemService orderItemService;

    @Resource
    PropertyValueService propertyValueService;

    /**
     * @see #home()
     * 前端交互,获取数据。进行相关的排版。
     * 每个Service层方法在实现的Service已经阐述过了.
     * 前后端使用json进行交互.
     */
    @GetMapping(value = "/foreHome")
    public Object home() {
        List<Category> categoryList = categoryService.list();
        productService.fill(categoryList);
        productService.fillByRow(categoryList);
        categoryService.removeCategoryFromProduct(categoryList);
        return categoryList;
    }

    /**
     * @see #register(User)
     * 注册界面的处理,检查和保存注册用户.
     * 下面是进行Shiro加密:
     * 随机生成密码盐
     * @see SecureRandomNumberGenerator#nextBytes().toString()
     * 使用什么算法进行加密和加密几次加密的对象和盐 {@link SimpleHash}
     */
    @PostMapping(value = "/foreRegister")
    public Object register(@RequestBody User user) {
        String name = user.getName();
        String password = user.getPassword();
        name = HtmlUtils.htmlEscape(name);
        if (userService.isExist(name)) {
            String message = "（＞﹏＜）用户名已经被使用过了,请重新创建用户名呀（o>▽<）!";
            return Result.fail(message);
        }
        // 设置密码盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 加密过后的密码...
        String encodedPassword = new SimpleHash("md5", password, salt, 2).toString();
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.add(user);
        return Result.success();
    }

    /**
     * @see #login(User, HttpSession)
     * 进行登录界面,把前端获取的user对象注入到userParam中
     * 在进行相关的转义,为当前的session设置相关对象属性。。。
     * 以下为Shiro加密过后
     * 使用{@link SecurityUtils}生成subject对象,封装用户的数据打包成@{@link UsernamePasswordToken}一个令牌
     * @see #login(User, HttpSession) 将用户对token于已经定义好{@link com.luzhi.tmall.realm.JpaRealm}
     * Realm中进行对比....
     */
    @PostMapping(value = "/foreLogin")
    public Object login(@RequestBody User userParam, HttpSession session) {
        String name = userParam.getName();
        // 先对用户名进行转义
        name = HtmlUtils.htmlEscape(name);
        // 判断是否存在该用户
        if (!userService.isExist(name)) {
            return Result.fail("（＞﹏＜）用户名不存在,请去注册呀（o>▽<）!");
        }
        User encryptUser = userService.getByName(name);
        // 判断用户的盐是否存在
        if (null == encryptUser.getSalt()) {
            Map<?, ?> objectMap = encryptByShiro(encryptUser.getPassword());
            // 设置相关的盐和密码
            encryptUser.setSalt(objectMap.get("salt").toString());
            encryptUser.setPassword(objectMap.get("encryptPassword").toString());
            // 更新到数据库中
            userService.add(encryptUser);
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(name, userParam.getPassword());
        try {
            subject.login(usernamePasswordToken);
            session.setAttribute("user", encryptUser);
            return Result.success();
        } catch (AuthenticationException e) {
            return Result.fail("账号密码不存在- m（_ _）m");
        }
    }

    /**
     * @see #product(int)
     * 获取产品的销量评论和相关的图片
     * 把相关的键值对写入Map中.
     * 为什么对象是相对与HashMap<>(){@link HashMap}对象.因为(非线程安全类)可以容纳(Key and Value)null；
     * 比HashTable<>(){@link Hashtable},(线程安全类, synchronized)相度来说较为宽松.
     * 比较符合实际web开发......
     * 再由Result通过success 交给浏览器进行处理..
     * 使用Map是由于浏览器比较好接受,而且返回的是多个结合.....
     * 详情请见
     * @see HashMap#put(Object, Object)
     * @see java.util.Hashtable#put(Object, Object)
     */
    @GetMapping(value = "/foreProduct/{pid}")
    public Object product(@PathVariable(value = "pid") int pid) {
        Product product = productService.get(pid);

        List<ProductImage> productSingleImage = productImageService.listSingleProductImage(product);
        List<ProductImage> productDetailImages = productImageService.listDetailProductImages(product);
        product.setProductSingleImages(productSingleImage);
        product.setProductDetailImages(productDetailImages);

        List<PropertyValue> propertyValueList = propertyValueService.findByProductOrderByIdDesc(product);
        List<Review> reviewList = reviewService.list(product);
        productService.setSaleAndReviewNumber(product);
        productImageService.setFirstProductImage(product);

        Map<String, Object> objectMap = new HashMap<>(1024);
        objectMap.put("product", product);
        objectMap.put("propertyValueList", propertyValueList);
        objectMap.put("reviewList", reviewList);

        return Result.success(objectMap);
    }

    /**
     * @see #checkLogin()
     * 检查是否登录.
     * 通过Shiro session中是否存在User {@link SecurityUtils#getSubject()}
     */
    @GetMapping(value = "foreCheckLogin")
    public Object checkLogin() {
        // 通过Shiro session中是否存在User
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return Result.success();
        }
        return Result.fail("未登录吖--（┬＿┬）");
    }

    /**
     * @param sort 选择是否要进行排序, sort=null,不进行排序.
     *             sort!=null 选择进行排序。。。。。。。
     * @see #category(int, String)
     * 创建相关分类界面,通过由前端提交category id 属性.....
     * 具体请看
     * @see List#sort(Comparator)
     */
    @GetMapping(value = "foreCategory/{cid}")
    public Object category(@PathVariable(value = "cid") int cid, String sort) {
        Category category = categoryService.get(cid);
        // 填充对象
        productService.fill(category);
        productService.setSaleAndReviewNumber(category.getProducts());
        // 禁止套娃
        categoryService.removeCategoryFromProduct(category);
        // 比较器具体实现
        if (sort != null) {
            switch (sort) {
                case "review":
                    category.getProducts().sort(new ProductReviewComparator());
                    break;
                case "date":
                    category.getProducts().sort(new ProductDateComparator());
                    break;
                case "price":
                    category.getProducts().sort(new ProductPriceComparator());
                    break;
                case "saleCount":
                    category.getProducts().sort(new ProductSaleComparator());
                    break;
                case "all":
                    category.getProducts().sort(new ProductAllComparator());
                    break;
                default:
                    break;
            }
        }
        return category;
    }

    /**
     * @throws javax.persistence.EntityNotFoundException;
     * @see #search(String)
     * 通过关键字获取相关查询的结果
     * 详情请看
     * @see ProductService#search(String, int, int)
     */
    @PostMapping(value = "foreSearch")
    public Object search(String keyword) throws EntityNotFoundException {
        if (null == keyword) {
            // 最好留下一个空格,不然会造成空异常
            keyword = "_";
        }
        // 从0开始查询,每20个一组........
        List<Product> productList = productService.search(keyword, 0, 20);
        // 为获取的product列表设置相关的属性........
        productImageService.setFirstProductImages(productList);
        productService.setSaleAndReviewNumber(productList);
        return productList;
    }

    /**
     * 生成相关的订单
     *
     * @see #buyOne(int, int, HttpSession)
     * 详情请看:
     * @see ForeRestController#buyOneAndAddCart(int, int, HttpSession)
     */
    @GetMapping(value = "foreBuyOne")
    public Object buyOne(int pid, int num, HttpSession session) {
        return buyOneAndAddCart(pid, num, session);
    }

    /**
     * 具体实现
     *
     * @param pid     通过 Product ID 获取相关的Product对象
     * @param num     获取生成订单产品的个数
     * @param session 通过session获取相关的User对象
     * @see #buyOneAndAddCart(int, int, HttpSession)
     */
    private int buyOneAndAddCart(int pid, int num, HttpSession session) {

        Product product = productService.get(pid);
        // 订单号
        int oiid = 0;
        User user = (User) session.getAttribute("user");
        // 创建found 布尔值
        boolean found = false;
        // 遍历通过User对象获取的OrderItem对象列表
        List<OrderItem> orderItemList = orderItemService.findByUser(user);

        for (OrderItem orderItem : orderItemList) {
            // 如果购物车中含有该商品
            if (orderItem.getProduct().getId() == product.getId()) {
                // 在原有的基础添加或删除商品数量
                orderItem.setNumber(orderItem.getNumber() + num);
                // 更新该订单对象
                orderItemService.update(orderItem);
                // 如果订单存在则不生成相关新的订单
                found = true;
                // 发现有该订单
                oiid = orderItem.getId();
                break;
            }
        }
        // 生成相关新的订单....
        if (!found) {
            OrderItem orderItemCreate = new OrderItem();
            orderItemCreate.setUser(user);
            orderItemCreate.setProduct(product);
            orderItemCreate.setNumber(num);
            orderItemService.add(orderItemCreate);
            oiid = orderItemCreate.getId();
        }
        return oiid;
    }

    /**
     * @param oiid    为什么是String[] 数组类,在订单的关系中要处理多个在购物车中的订单
     * @param session 创建相关的session对话..
     *                设置相关的ois泛型属性集合
     *                为每个订单设置相关的图片信息
     *                详情请看:
     * @see #buy(String[], HttpSession)
     * @see ProductImageService#setFirstProductImagesOnOrderItems(List)
     */
    @GetMapping(value = "foreBuy")
    public Object buy(String[] oiid, HttpSession session) {
        List<OrderItem> orderItemList = new ArrayList<>();
        float total = 0;

        for (String string : oiid) {
            int id = Integer.parseInt(string);
            OrderItem orderItem = orderItemService.get(id);
            total = orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            orderItemList.add(orderItem);
        }
        productImageService.setFirstProductImagesOnOrderItems(orderItemList);
        session.setAttribute("orderItemList", orderItemList);

        Map<String, Object> objectMap = new HashMap<>(512);
        objectMap.put("orderItemList", orderItemList);
        objectMap.put("total", total);
        return Result.success(objectMap);
    }

    /**
     * //"重复"购买行为与添加到购物车是相同的
     *
     * @param pid     获取商品的id值
     * @param num     获取购买数量
     * @param session 获取当前的session
     * @see #cart(int, int, HttpSession)
     * 详情请看...
     * @see ForeRestController#buyOneAndAddCart(int, int, HttpSession)
     */
    @GetMapping(value = "foreAddCart")
    public Object cart(int pid, int num, HttpSession session) {
        buyOneAndAddCart(pid, num, session);
        return Result.success();
    }

    /**
     * // 购物车页面
     *
     * @param session 通过session会话获取相关的User对象
     * @see #cart(HttpSession)
     * 详情请看:
     * @see ProductImageService#setFirstProductImagesOnOrderItems(List)
     */
    @GetMapping(value = "foreCart")
    public Object cart(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItemList = orderItemService.findByUser(user);
        productImageService.setFirstProductImagesOnOrderItems(orderItemList);
        return orderItemList;
    }

    /**
     * // 改变订单页面中购买产品的数量
     *
     * @param session 获取当前session对象,获取登录信息
     * @param pid     获取修改产品对id值
     * @param num     目前修改对数量
     * @see #changeOrderItem(HttpSession, int, int)
     * 详情请见:
     * @see OrderItemService#findByUser(User)
     */
    @GetMapping(value = "foreChangeOrderItem")
    public Object changeOrderItem(HttpSession session, int pid, int num) {
        User user = (User) session.getAttribute("user");
        // 如果session中User为空
        if (null == user) {
            return Result.fail("未登录吖--（┬＿┬）");
        }
        // 通过User获取订单
        List<OrderItem> orderItemList = orderItemService.findByUser(user);
        // 遍历设置相关的订单项
        for (OrderItem orderItem : orderItemList) {
            // 响应前端做修改.
            if (orderItem.getProduct().getId() == pid) {
                orderItem.setNumber(num);
                orderItemService.update(orderItem);
                break;
            }
        }
        return Result.success();
    }

    /**
     * //删除订单项
     *
     * @param session 获取当前session再获取user对象
     * @param oiid    订单id
     * @see #deleteOrderItem(HttpSession, int)
     * 删除功能详见与:
     * @see OrderItemService#delete(int)
     */
    @GetMapping(value = "foreDeleteOrderItem")
    public Object deleteOrderItem(HttpSession session, int oiid) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录吖--（┬＿┬）");
        }
        orderItemService.delete(oiid);
        return Result.success();
    }

    /**
     * // 为生成的订单设置属性..
     *
     * @param session 通过session获取User对象
     * @param order   接受前端产生的Order
     * @see #createOrder(Order, HttpSession)
     * 获取支付的金额请看:
     * @see OrderService#add(Order, List)
     */
    @PostMapping(value = "foreCreateOrder")
    public Object createOrder(@RequestBody Order order, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录吖--（┬＿┬）");
        }
        // 随机生成相关的订单码,获取的
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(1000);
        // 设置相关的属性
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setUser(user);
        // 设置为等待支付
        order.setStatus(OrderService.WAIT_PAY);
        //noinspection unchecked
        List<OrderItem> orderItemList = (List<OrderItem>) session.getAttribute("orderItemList");
        float total = orderService.add(order, orderItemList);
        // Map 存放数据便于前端获取...
        Map<String, Object> objectMap = new HashMap<>(512);
        objectMap.put("oid", order.getId());
        objectMap.put("total", total);
        return Result.success(objectMap);
    }

    /**
     * // 支付完成后修改订单状态
     *
     * @param oid 通过前端传来的订单id
     * @see #payed(int)
     */
    @GetMapping(value = "forePayed")
    public Object payed(int oid) {
        Order order = orderService.get(oid);
        // 支付完后修改订单状态
        order.setStatus(OrderService.WAIT_DELIVERY);
        order.setPayDate(new Date());
        orderService.update(order);
        return order;
    }

    /**
     * // 展示前端订单....
     *
     * @param session 获取当前前端session(会话)进一步获取user
     * @see #bought(HttpSession)
     */
    @GetMapping(value = "foreBought")
    public Object bought(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录吖--（┬＿┬）");
        }
        List<Order> orderList = orderService.listByUserWithOutDeleted(user);
        // 禁止套娃
        orderService.removeOrderFromOrderItem(orderList);
        return orderList;
    }

    /**
     * // 为支付完成的订单设置相关的对象...
     *
     * @param oid 通过前端axios Get方法提交的url 中的oid
     *            获取当前order对像......
     *            详情请见:
     * @see #confirmPay(int)
     * @see OrderService#calc(Order)
     */
    @GetMapping(value = "foreConfirmPay")
    public Object confirmPay(int oid) {
        Order order = orderService.get(oid);
        // 填充对象
        orderItemService.fill(order);
        // 计算总金额
        orderService.calc(order);
        // 禁止套娃
        orderService.removeOrderFromOrderItem(order);
        return order;
    }

    /**
     * // 订单确认设置...
     *
     * @param oid 通过前端传来的oid获取相关order对象
     * @see #orderConfirm(int)
     */
    @GetMapping(value = "foreOrderConfirmed")
    public Object orderConfirm(int oid) {
        Order order = orderService.get(oid);
        // 修改状态
        order.setStatus(OrderService.WAIT_REVIEW);
        // 修改确认支付时间
        order.setConfirmDate(new Date());
        // 更新
        orderService.update(order);
        return Result.success();
    }

    /**
     * // 响应删除,"删除"在前端的页面
     *
     * @param oid 通过axios put提交的oid获取相关order对象并进行操作...
     * @see #deleteOrder(int)
     */
    @PutMapping(value = "foreDeleteOrder")
    public Object deleteOrder(int oid) {
        // 获取对象
        Order order = orderService.get(oid);
        // 设置订单状态为删除状态
        order.setStatus(OrderService.DELETE);
        // 更新到数据库中
        orderService.update(order);
        return Result.success();
    }

    /**
     * // 评论界面,
     *
     * @param oid 通过axios中get方法获取当前oid值
     *            再获取相关order对象..
     *            详情请看:
     * @see #review(int)
     * @see HashMap#put(Object, Object)
     */
    @GetMapping(value = "foreReview")
    public Object review(int oid) {
        Order order = orderService.get(oid);
        // 填充对象
        orderItemService.fill(order);
        // 禁止套娃
        orderService.removeOrderFromOrderItem(order);
        // 获取订单的第一个对象设置评论
        Product product = order.getOrderItemList().get(0).getProduct();
        // 获取这个产品评论集合
        List<Review> reviewList = reviewService.list(product);
        // 设置销售评论和销量
        productService.setSaleAndReviewNumber(product);
        Map<String, Object> objectMap = new HashMap<>(512);
        // 设置Map对象
        objectMap.put("product", product);
        objectMap.put("order", order);
        objectMap.put("reviewList", reviewList);
        return Result.success(objectMap);
    }

    /**
     * 把当前的评论保存的数据库中
     *
     * @param session 通过HttpSession获取当前User对象
     * @param oid     获取axios的post方法oid
     * @param pid     包括相关的product id (pid)
     * @param content 评论属性,(注意要进行转义)......
     * @see #doReview(HttpSession, int, int, String)
     * 更新订单状态请看:
     * @see OrderService#update(Order)
     * 保存到数据库中:
     * @see ReviewService#add(Review)
     */
    @PostMapping(value = "foreDoReview")
    public Object doReview(HttpSession session, int oid, int pid, String content) {
        // 获取当前对象order
        Order order = orderService.get(oid);
        // 改变目前订单的状态
        order.setStatus(OrderService.FINISH);
        // 更新到数据库中
        orderService.update(order);

        Product product = productService.get(pid);
        // 进行转义
        content = HtmlUtils.htmlEscape(content);
        // 获取当前的user对象
        User user = (User) session.getAttribute("user");
        Review review = new Review();
        // 设置Review的一系列属性.....
        review.setCreateDate(new Date());
        review.setUser(user);
        review.setProduct(product);
        review.setContent(content);
        // 把评论保存到数据库中
        reviewService.add(review);
        return Result.success();
    }

    /**
     * 对已经注册但没有salt和加密的用户进行设置相关的盐
     *
     * @see #encryptByShiro(String)
     * 设置相关的盐 {@link SecureRandomNumberGenerator#nextBytes()}
     * 对密码进行设置加密 {@link SimpleHash}使用{@link HashMap#put(Object, Object)}
     * 返回设置好的对象..
     */
    private static Map<?, ?> encryptByShiro(String password) {
        // 生成盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 设置加密算法,加密的对象,salt,加密的次数
        String encryptPassword = new SimpleHash("md5", password, salt, 2).toString();
        Map<String, Object> objectMap = new HashMap<>(512);
        objectMap.put("salt", salt);
        objectMap.put("encryptPassword", encryptPassword);
        return objectMap;
    }

}

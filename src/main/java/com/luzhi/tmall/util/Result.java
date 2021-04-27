package com.luzhi.tmall.util;

/**
 * @author apple
 * @version jdk1.8
 * // TODO: 2021/3/21
 * REST控件,统一的REST响应,包括成功,错误信息,也包括数据信息.
 * 这里面可以包括错误代码，错误姓名包括数据
 * //此为工具类
 */
@SuppressWarnings("unused")
public class Result {

    /**
     * @see #SUCCESS_CODE
     * @see #FAIL_CODE
     * # SUCCESS_CODE 活动状态为成功的 code 代表为0
     * # FAIL_CODE 活动状态为失败的 CODE 为1
     */
    public static final int SUCCESS_CODE = 0;
    public static final int FAIL_CODE = 1;

    int code;

    Object data;

    String message;

    public Result(int code, Object data, String message) {

        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * @see #success()
     * 该静态方法获取成功 CODE = 0.
     */
    public static Result success() {
        return new Result(SUCCESS_CODE, null, null);
    }

    /**
     * @see #success(Object)
     * 该静态方法获取成功Code = 1 接受数据传递
     */
    public static Result success(Object data) {
        return new Result(SUCCESS_CODE, data, "");
    }

    /**
     * @see #fail(String)
     * 该静态方法如果失败 code = 1 , 接受错误信息。。。。
     */
    public static Result fail(String message) {
        return new Result(FAIL_CODE, null, message);
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

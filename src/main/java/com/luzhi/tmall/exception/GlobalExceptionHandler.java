package com.luzhi.tmall.exception;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 对于异常的相关处理...
 */

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(value = Exception.class)
	public String defaultErrorHandler(Exception e) throws Exception {
		// 打印错误回溯.
		e.printStackTrace();
		// 使用范型获取异常的类对象
		Class<?> constraintViolationException = Class.forName("org.hibernate.exception.ConstraintViolationException");
		// 判断原因不为空和与之异常的类对象和已获取的类对象 == 大多数直接删除绕过外键约束.
		if (null != e.getCause() && constraintViolationException == e.getCause().getClass()) {

			return "违反了约束，多半是外键约束";
		}
		return e.getMessage();
	}
}


/**   
 * @Title: AopLog.java 
 * @Package com.cardlan.pay.aop
 * @Description: 切面类包 
 * @author YiShan   
 * @date 2018年10月8日 下午6:19:52 
 * @version V1.0   
 */
package com.cardlan.sharding.jdbc.common.aop;

import java.lang.annotation.*;

/** 
 * @ClassName: AopLog 
 * @Description: 切面日志输出注解
 * @author YiShan
 * @date 2018年10月8日 下午6:19:52  
 * @Company 深圳市卡联科技股份有限公司 
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AopLog
{
	/*
	 * 方法路径
	 */
	String value();
	
}

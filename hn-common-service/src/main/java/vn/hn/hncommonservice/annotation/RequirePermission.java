package vn.hn.hncommonservice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation để định nghĩa permission cho endpoint Sẽ tự động tạo permission trong database khi app startup Usage:
 *
 * @RequirePermission(value = "CoreUserCreate", description = "Create new user")
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
	
	/**
	 * Tên permission (VD: CoreUserCreate, ProductCreate)
	 */
	String name();
	
	/**
	 * Mô tả permission
	 */
	String description() default "";
	
	/**
	 * Service prefix (tự động lấy từ application name nếu không set)
	 */
	String service() default "";
}
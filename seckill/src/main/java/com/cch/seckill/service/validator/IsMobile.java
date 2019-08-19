package com.cch.seckill.service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;

@Documented
@Constraint(validatedBy = { IsMobileValidator.class })
public @interface IsMobile {

    boolean required() default true;

    String message() default "手机号码格式错误";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default{ };

}

package com.unleqitq.jeat.internal;

import java.lang.annotation.*;

/**
 * Marks a class, method, field or constructor as internal use only. <br>
 * This means that the class, method, field or constructor is not intended to be used by the user of the library.
 */
@InternalUse
@Documented
@Target ({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention (RetentionPolicy.CLASS)
public @interface InternalUse {
}

package ch.marcus_schulte.dbc;

import java.lang.annotation.*;

/**
 * @author marcus
 * @since 05.07.13
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
@Inherited
public @interface IsInvariant {
}

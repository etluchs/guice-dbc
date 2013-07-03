package ch.marcus_schulte.dbc;

import java.lang.annotation.*;

/**
 * @author marcus
 * @since 03.07.13
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Inherited
public @interface HasInvariant {
    Class<? extends Invariant<?>> value();
}

package ch.marcus_schulte.dbc;

/**
 * @author marcus
 * @since 03.07.13
 */
public interface Invariant<T> {
    boolean holdsFor( T instance );
}

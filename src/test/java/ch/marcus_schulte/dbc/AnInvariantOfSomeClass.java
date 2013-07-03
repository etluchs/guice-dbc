package ch.marcus_schulte.dbc;

/**
 * @author marcus
 * @since 03.07.13
 */
public class AnInvariantOfSomeClass implements Invariant<SomeClass> {
    @Override
    public boolean holdsFor(SomeClass instance) {
        return instance.z == instance.x + instance.y;
    }

    @Override
    public String toString() {
        return "z = x+y";
    }
}

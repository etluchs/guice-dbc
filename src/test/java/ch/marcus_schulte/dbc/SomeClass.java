package ch.marcus_schulte.dbc;

/**
 * @author marcus
 * @since 03.07.13
 */

public class SomeClass implements SomeContract {
     int x;
     int y;
     int z;

    @Override
    public void firr(int f){
        x = 1*f;
        y = 2*f;
        z = 3*f;
    }

    @Override
    public void furr(int u) {
        x -= u;
        y -= u;
        z -= u;
    }


}

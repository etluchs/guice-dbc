package ch.marcus_schulte.dbc;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

/**
 * @author marcus
 * @since 03.07.13
 */
public class InvariantCheckingTest {

    @Inject
    private SomeClass someClass;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                install(new DbcModule());
                bind(SomeContract.class).to(SomeClass.class);
            }
        });
        injector.injectMembers(this);
    }

    @Test(expected = IllegalStateException.class)
    public void violation_is_reported()
    {
         someClass.furr(1);
    }

    @Test
    public void nothing_happens_when_the_invariant_holds() throws Exception {

        someClass.firr(5);

    }
}

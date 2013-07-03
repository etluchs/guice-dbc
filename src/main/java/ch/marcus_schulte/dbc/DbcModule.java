package ch.marcus_schulte.dbc;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matchers;

/**
 * @author marcus
 * @since 03.07.13
 */
public class DbcModule extends AbstractModule {
    @Override
    protected void configure() {
        bindInterceptor( new HasContractMatcher(), Matchers.any(),
                new InvariantCheckingInterceptor() );
    }

    private static class HasContractMatcher extends AbstractMatcher<Class> {
        @Override
        public boolean matches(Class aClass) {
            if ( aClass.getAnnotation(HasInvariant.class) != null )
                return true;
            for (Class anIf : aClass.getInterfaces()) {
                if ( matches(anIf) )
                    return true;
            }
            return false;
        }
    }
}

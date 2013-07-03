package ch.marcus_schulte.dbc;

import com.google.inject.internal.util.$Lists;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author marcus
 * @since 03.07.13
 */
public class InvariantCheckingInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Collection<Invariant<?>> invariants = findInvariants(invocation);
        try {
            return invocation.proceed();
        } finally {
            for (Invariant invariant : invariants) {
                if ( ! invariant.holdsFor( invocation.getThis() ) )
                    throw new IllegalStateException("Violation of invariant "+invariant+" after invoking method "
                            + invocation.getMethod().getName() +" with arguments "
                            + Arrays.deepToString(invocation.getArguments())+ " on "+invocation.getThis());
            }
        }
    }

    private Collection<Invariant<?>> findInvariants(MethodInvocation invocation) throws InstantiationException, IllegalAccessException {

        Collection<Invariant<?>> result = $Lists.newArrayList();
        Class<?> aClass = invocation.getMethod().getDeclaringClass();
        return addInvariantsFromClass(result, aClass);
    }

    private Collection<Invariant<?>> addInvariantsFromClass(Collection<Invariant<?>> result, Class<?> aClass) throws InstantiationException, IllegalAccessException {
        HasInvariant hasInvariant = invariantAnnotation(aClass);
        if ( hasInvariant != null )
            result.add( hasInvariant.value().newInstance() );

        for (Class<?> anIf : aClass.getInterfaces()) {
            addInvariantsFromClass(result, anIf);
        }
        return result;
    }

    private HasInvariant invariantAnnotation(Class<?> aClass) {
        return aClass
                .getAnnotation(HasInvariant.class);
    }
}

package ch.marcus_schulte.dbc;

import com.google.inject.internal.util.$Lists;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author marcus
 * @since 03.07.13
 */
public class InvariantCheckingInterceptor implements MethodInterceptor {

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Collection<Invariant<?>> invariants = findInvariants(invocation);
        Object result = invocation.proceed();

        if (invocation.getMethod().getAnnotation(IsInvariant.class) == null)
            for (Invariant invariant : invariants) {
                if (!invariant.holdsFor(invocation.getThis()))
                    throw new IllegalStateException("Violation of invariant " + invariant
                            + " after invoking method " + invocation.getMethod().getName()
                            + " with arguments " + Arrays.deepToString(invocation.getArguments())
                            + " on " + invocation.getThis());
            }
        return result;
    }

    private Collection<Invariant<?>> findInvariants(final MethodInvocation invocation) throws InstantiationException, IllegalAccessException {

        Collection<Invariant<?>> result = $Lists.newArrayList();
        Class<?> aClass = invocation.getMethod().getDeclaringClass();
        addInvariantsFromClass(result, aClass);

        addMethodInvariants(invocation, result, aClass);

        return result;
    }

    private void addMethodInvariants(final MethodInvocation invocation, Collection<Invariant<?>> result, Class<?> aClass) {
        for (final Method method : aClass.getMethods()) {
            IsInvariant isInvariant = method.getAnnotation(IsInvariant.class);
            if (isInvariant != null)
                result.add(new Invariant<Object>() {
                    @Override
                    public boolean holdsFor(Object instance) {
                        try {
                            return (Boolean) method.invoke(invocation.getThis());
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public String toString() {
                        return "invariant " + method.getName();
                    }
                });
        }
    }

    private void addInvariantsFromClass(Collection<Invariant<?>> result, Class<?> aClass) throws InstantiationException, IllegalAccessException {
        HasInvariant hasInvariant = invariantAnnotation(aClass);
        if (hasInvariant != null)
            result.add(hasInvariant.value().newInstance());

        for (Class<?> anIf : aClass.getInterfaces()) {
            addInvariantsFromClass(result, anIf);
        }
    }

    private HasInvariant invariantAnnotation(Class<?> aClass) {
        return aClass
                .getAnnotation(HasInvariant.class);
    }
}

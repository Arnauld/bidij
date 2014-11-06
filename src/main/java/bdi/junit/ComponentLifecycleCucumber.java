package bdi.junit;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import gherkin.TagExpression;
import gherkin.formatter.model.Tag;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ComponentLifecycleCucumber extends ComponentLifecycle {

    private List<String> tags = new ArrayList<>();

    public ComponentLifecycleCucumber(String... tags) {
        this.tags.addAll(Arrays.asList(tags));
    }

    @Override
    public void before(List<Object> components) {
        invoke(components, Before.class,
                ComponentLifecycleCucumber::beforeOrderOf,
                ComponentLifecycleCucumber::beforeTags);
    }

    @Override
    public void after(List<Object> components) {
        invoke(components, After.class,
                ComponentLifecycleCucumber::afterOrderOf,
                ComponentLifecycleCucumber::afterTags);
    }

    private void invoke(List<Object> components,
                        Class<? extends Annotation> annotation,
                        ToIntFunction<Invocation> orderOf,
                        Function<Annotation, String[]> tagsOf) {
        List<Invocation> invocationList = new ArrayList<>();
        for (Object component : components) {
            collectInvocation(invocationList, component.getClass(), component, annotation, tagsOf);
        }

        Collections.sort(invocationList, comparator(orderOf));
        for (Invocation invocation : invocationList) {
            invocation.invoke();
        }
    }

    private void collectInvocation(List<Invocation> invocationList,
                                   Class<? extends Object> aClass,
                                   Object component,
                                   Class<? extends Annotation> annotationClass,
                                   Function<Annotation, String[]> tagsOf) {
        for (Method method : aClass.getDeclaredMethods()) {
            Annotation annotation = method.getAnnotation(annotationClass);
            if (annotation != null) {
                String[] tags = tagsOf.apply(annotation);
                TagExpression expression = new TagExpression(Arrays.asList(tags));
                if (expression.evaluate(tags()))
                    invocationList.add(new Invocation(annotation, component, method));
            }
        }
    }

    private Collection<Tag> tags() {
        return tags.stream().map(t -> new Tag(t, -1)).collect(Collectors.toList());
    }

    private static class Invocation {
        final Annotation annotation;
        final Object instance;
        final Method method;

        public Invocation(Annotation annotation, Object instance, Method method) {

            this.annotation = annotation;
            this.instance = instance;
            this.method = method;
        }

        public void invoke() {
            try {
                method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static int beforeOrderOf(Invocation invocation) {
        return ((Before) invocation.annotation).order();
    }

    private static String[] beforeTags(Annotation annotation) {
        return ((Before) annotation).value();
    }

    private static int afterOrderOf(Invocation invocation) {
        return ((After) invocation.annotation).order();
    }

    private static String[] afterTags(Annotation annotation) {
        return ((After) annotation).value();
    }

    private Comparator<Invocation> comparator(ToIntFunction<Invocation> orderOf) {
        return (o1, o2) -> Integer.compare(orderOf.applyAsInt(o1), orderOf.applyAsInt(o2));
    }
}

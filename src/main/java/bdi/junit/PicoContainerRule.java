package bdi.junit;

import org.junit.rules.ExternalResource;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PicoContainerRule extends ExternalResource {

    private final PicoContainer context = new PicoContainer();
    private ComponentLifecycle lifecycle = new ComponentLifecycle();
    private boolean started;

    public PicoContainerRule(Class<?>... components) {
        declare(components);
    }

    public PicoContainerRule using(ComponentLifecycle lifecycle) {
        checkIfNotStarted();
        this.lifecycle = lifecycle;
        return this;
    }

    public PicoContainerRule declare(Class<?>... components) {
        checkIfNotStarted();
        for (Class<?> component : components) {
            context.addClass(component);
        }
        return this;
    }

    private void checkIfNotStarted() {
        if (started)
            throw new IllegalStateException("Container already started");
    }

    public <T> T get(Class<T> type) {
        if (!started)
            startContext();
        return context.getInstance(type);
    }

    @Override
    protected void before() throws Throwable {
    }

    private void startContext() {
        started = true;
        context.start();
        lifecycle.before(context.underlying().getComponents());
    }

    @Override
    protected void after() {
        lifecycle.after(context.underlying().getComponents());
        context.stop();
    }
}

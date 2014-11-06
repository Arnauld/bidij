package bdi.junit;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PicoContainer {
    private MutablePicoContainer pico;
    private final Set<Class<?>> classes = new HashSet<>();

    public MutablePicoContainer underlying() {
        return pico;
    }

    public void start() {
        pico = new PicoBuilder().withCaching().build();
        classes.forEach(pico::addComponent);
        pico.start();
    }

    public void stop() {
        pico.stop();
        pico.dispose();
    }

    public void addClass(Class<?> clazz) {
        if (classes.add(clazz)) {
            addConstructorDependencies(clazz);
        }
    }

    public <T> T getInstance(Class<T> type) {
        return pico.getComponent(type);
    }

    private void addConstructorDependencies(Class<?> clazz) {
        for (Constructor constructor : clazz.getConstructors()) {
            for (Class paramClazz : constructor.getParameterTypes()) {
                addClass(paramClazz);
            }
        }
    }

}

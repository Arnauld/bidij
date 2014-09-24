package bdi.glue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class GlobalWorld {

    private Map<Object, Object> context = new HashMap<>();

    public void put(Object key, Object value) {
        context.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Object key) {
        return (T) context.get(key);
    }
}

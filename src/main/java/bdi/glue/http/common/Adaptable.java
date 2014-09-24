package bdi.glue.http.common;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface Adaptable {
    <T> T getAdapter(Class<T> type);
}

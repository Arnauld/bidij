package bdi.glue.http.common;

import java.net.URI;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface HttpGateway extends Adaptable {

    HttpResponse invoke(HttpRequestBuilder req);

    void defineHost(URI uri);
}

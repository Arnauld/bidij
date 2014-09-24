package bdi.glue.http.common;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface HttpGateway extends Adaptable {

    HttpResponse invoke(HttpRequestBuilder req);

}

package bdi.glue.http.httpclient;

import bdi.glue.http.common.HttpException;
import bdi.glue.http.common.HttpGateway;
import bdi.glue.http.common.HttpMethod;
import bdi.glue.http.common.HttpRequestBuilder;
import bdi.glue.http.common.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UnknownFormatConversionException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HttpClientGateway implements HttpGateway {

    private Logger log = LoggerFactory.getLogger(HttpClientGateway.class);

    private final CloseableHttpClient closeableHttpClient;
    private final CookieStore cookieStore;

    public HttpClientGateway(CloseableHttpClient closeableHttpClient, CookieStore cookieStore) {
        this.closeableHttpClient = closeableHttpClient;
        this.cookieStore = cookieStore;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAdapter(Class<T> type) {
        if (type.isAssignableFrom(CloseableHttpResponse.class))
            return (T) closeableHttpClient;
        throw new UnknownFormatConversionException("For type: " + type);
    }

    @Override
    public HttpResponse invoke(HttpRequestBuilder req) {
        HttpMethod m = HttpMethod.lookup(req.getMethodAsString());

        HttpRequestBase requestBase;
        switch (m) {
            case GET:
                requestBase = new HttpGet(req.generateUrl());
                break;
            case PUT:
                requestBase = new HttpPut(req.generateUrl());
                break;
            case POST:
                requestBase = new HttpPost(req.generateUrl());
                break;
            case DELETE:
                requestBase = new HttpDelete(req.generateUrl());
                break;
            case HEAD:
                requestBase = new HttpHead(req.generateUrl());
                break;
            case OPTIONS:
                requestBase = new HttpOptions(req.generateUrl());
                break;
            case TRACE:
                requestBase = new HttpTrace(req.generateUrl());
                break;
            case PATCH:
                requestBase = new HttpPatch(req.generateUrl());
                break;
            default:
                throw new IllegalArgumentException("Unsupported method " + m + "'");
        }

        try {
            CloseableHttpResponse lastResponse = closeableHttpClient.execute(requestBase);
            log.debug("{}", lastResponse.getStatusLine());
            return new HttpResponseAdapter(lastResponse, cookieStore);
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

}

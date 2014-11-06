package bdi.glue.http.httpclient;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HttpClientWorld {
    private final List<Function<HttpClientBuilder, HttpClientBuilder>> builderConfigurers = Lists.newArrayList();
    private final List<Function<CloseableHttpClient, CloseableHttpClient>> clientConfigurers = Lists.newArrayList();

    private BasicCookieStore cookieStore;
    private CloseableHttpClient httpclient;
    private HttpClientBuilder httpClientBuilder;
    private HttpClientContext httpClientContext;

    public BasicCookieStore cookieStore() {
        if (cookieStore == null) {
            cookieStore = new BasicCookieStore();
        }
        return cookieStore;
    }

    public void setCookieStore(BasicCookieStore cookieStore) {
        if (httpClientBuilder != null || httpclient != null) {
            throw new IllegalStateException("HttpClientBuilder or HttpClient already instanciated");
        }
        this.cookieStore = cookieStore;
    }

    public CloseableHttpClient httpClient() {
        if (httpclient == null) {
            httpclient = getHttpClientBuilder().build();
            for (Function<CloseableHttpClient, CloseableHttpClient> configurer : clientConfigurers) {
                httpclient = configurer.apply(httpclient);
            }
        }
        return httpclient;
    }

    public void setHttpclient(CloseableHttpClient httpclient) {
        this.httpclient = httpclient;
    }

    public HttpClientBuilder getHttpClientBuilder() {
        if (httpClientBuilder == null) {
            httpClientBuilder = HttpClients
                    .custom()
                    .setDefaultCookieStore(cookieStore());
            for (Function<HttpClientBuilder, HttpClientBuilder> configurer : builderConfigurers) {
                httpClientBuilder = configurer.apply(httpClientBuilder);
                if (httpClientBuilder == null) {
                    throw new IllegalStateException("Configurer returns null... (" + configurer + ")");
                }
            }
        }
        return httpClientBuilder;
    }

    public void registerBuilderConfigurer(Function<HttpClientBuilder, HttpClientBuilder> configurer) {
        if (httpClientBuilder != null || httpclient != null) {
            throw new IllegalStateException("HttpClientBuilder or HttpClient already instanciated");
        }
        builderConfigurers.add(configurer);
    }

    public void registerClientConfigurer(Function<CloseableHttpClient, CloseableHttpClient> configurer) {
        if (httpClientBuilder != null || httpclient != null) {
            throw new IllegalStateException("HttpClientBuilder or HttpClient already instanciated");
        }
        clientConfigurers.add(configurer);
    }

    public HttpClientContext httpClientContext() {
        if (httpClientContext == null) {
            httpClientContext = HttpClientContext.create();
        }
        return httpClientContext;
    }

    public void setHttpClientContext(HttpClientContext httpContext) {
        this.httpClientContext = httpContext;
    }
}

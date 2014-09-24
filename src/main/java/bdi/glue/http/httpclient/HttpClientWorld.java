package bdi.glue.http.httpclient;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HttpClientWorld {
    private List<Function<HttpClientBuilder, HttpClientBuilder>> httpConfigurers = Lists.newArrayList();


    private BasicCookieStore cookieStore;
    private CloseableHttpClient httpclient;

    public BasicCookieStore cookieStore() {
        if (cookieStore == null)
            cookieStore = new BasicCookieStore();
        return cookieStore;
    }

    public void setCookieStore(BasicCookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public CloseableHttpClient httpClient() {
        if (httpclient == null) {
            HttpClientBuilder builder =
                    HttpClients
                            .custom()
                            .setDefaultCookieStore(cookieStore());
            for (Function<HttpClientBuilder, HttpClientBuilder> configurer : httpConfigurers) {
                builder = configurer.apply(builder);
                if (builder == null)
                    throw new IllegalStateException("Configurer returns null... (" + configurer + ")");
            }
            httpclient = builder.build();
        }
        return httpclient;
    }

    public void register(Function<HttpClientBuilder, HttpClientBuilder> configurer) {
        httpConfigurers.add(configurer);
    }

    public void setHttpclient(CloseableHttpClient httpclient) {
        this.httpclient = httpclient;
    }
}

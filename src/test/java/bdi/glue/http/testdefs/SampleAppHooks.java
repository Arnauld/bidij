package bdi.glue.http.testdefs;

import bdi.glue.http.httpclient.HttpClientWorld;
import cucumber.api.java.Before;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SampleAppHooks {

    private HttpClientWorld httpClientWorld;
    private boolean allowAllHostname;

    public SampleAppHooks(HttpClientWorld httpClientWorld) {
        this.httpClientWorld = httpClientWorld;
    }

    @Before(value = "@secure", order = 10000)
    public void configureAuthentication() {
        httpClientWorld.registerClientConfigurer(client -> client);
        httpClientWorld.registerBuilderConfigurer(builder -> configureSSL(builder));
    }

    @Before(value = "@secure__allow_all_hostname", order = 100)
    public void configureAllowAllHostname() {
        allowAllHostname = true;
    }

    private HttpClientBuilder configureSSL(HttpClientBuilder clientBuilder) {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),
                    allowAllHostname ?
                            SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER :
                            SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            return clientBuilder.setSSLSocketFactory(sslsf);
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
}

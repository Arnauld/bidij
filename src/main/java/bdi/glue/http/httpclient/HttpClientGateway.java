package bdi.glue.http.httpclient;

import bdi.glue.http.common.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HttpClientGateway implements HttpGateway {

    private static final Charset UTF8 = Charset.forName("utf8");

    private final Logger log = LoggerFactory.getLogger(HttpClientGateway.class);

    private final HttpClientWorld httpClientWorld;
    private URI hostUri;

    public HttpClientGateway(HttpClientWorld httpClientWorld) {
        this.httpClientWorld = httpClientWorld;
    }

    @Override
    public void defineHost(URI hostUri) {
        this.hostUri = hostUri;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAdapter(Class<T> type) {
        if (type.equals(CloseableHttpClient.class)) {
            return (T) httpClientWorld.httpClient();
        }
        if (type.isAssignableFrom(CookieStore.class)) {
            return (T) httpClientWorld.cookieStore();
        }
        if (type.isAssignableFrom(HttpClientContext.class)) {
            return (T) httpClientWorld.httpClientContext();
        }

        return null;
    }

    @Override
    public HttpResponse invoke(HttpRequestBuilder req) {
        HttpMethod m = HttpMethod.lookup(req.getMethodAsString());
        String requestPath = req.getRequestPath();

        log.info("Building request to invoke {} on {}", m, requestPath);

        HttpHost httpHost = getHttpHost();
        URIBuilder uriBuilder = getUriBuilder(req, httpHost);

        configureParameters(req, uriBuilder);
        URI uri;
        try {
            uri = uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Fail to build URI " + uriBuilder);
        }

        HttpRequestBase requestBase = createHttpRequestBase(m);
        configureBody(req, m, requestBase);
        requestBase.setURI(uri);

        HttpClientContext context = httpClientWorld.httpClientContext();
        configureBasicAuth(req, httpHost, context);
        configureHeaders(req, requestBase);

        configureCookies(req);

        try {
            CloseableHttpClient closeableHttpClient = httpClientWorld.httpClient();

            CloseableHttpResponse lastResponse =
                    (httpHost != null) ?
                            closeableHttpClient.execute(
                                    httpHost,
                                    requestBase,
                                    context) :
                            closeableHttpClient.execute(
                                    requestBase,
                                    context);

            log.debug("{}", lastResponse.getStatusLine());
            return new HttpResponseAdapter(lastResponse, httpClientWorld.cookieStore());
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

    private void configureCookies(HttpRequestBuilder req) {
        BasicCookieStore cookieStore = httpClientWorld.cookieStore();

        // no remove cookie method
        List<org.apache.http.cookie.Cookie> cookies = cookieStore.getCookies();
        cookieStore.clear();
        List<String> cookiesToRemove = req.getCookiesToRemove();
        cookies.stream()
                .filter(cookie -> !cookiesToRemove.contains(cookie.getName()))
                .forEach(cookieStore::addCookie);

        for (Cookie cookie : req.getCookies()) {
            cookieStore.addCookie(new BasicClientCookie(cookie.name(), cookie.value()));
        }
    }

    private void configureBody(HttpRequestBuilder req, HttpMethod m, HttpRequestBase requestBase) {
        String body = req.getBody();
        if (body == null)
            return;

        if (!(requestBase instanceof HttpEntityEnclosingRequestBase)) {
            throw new IllegalArgumentException("Cannot attach body on " + m + " request");
        }

        HttpEntity entity;
        String mimeType = req.getContentType();
        if (mimeType != null) {
            ContentType contentType = ContentType.create(mimeType, UTF8);
            entity = new ByteArrayEntity(body.getBytes(UTF8), contentType);
        } else {
            entity = new ByteArrayEntity(body.getBytes(UTF8));
        }
        ((HttpEntityEnclosingRequestBase) requestBase).setEntity(entity);
    }

    private URIBuilder getUriBuilder(HttpRequestBuilder req, HttpHost httpHost) {
        URIBuilder uriBuilder = new URIBuilder();
        if (httpHost != null) {
            uriBuilder = uriBuilder
                    .setScheme(httpHost.getSchemeName())
                    .setHost(httpHost.toHostString())
                    .setPort(httpHost.getPort());
        }
        return uriBuilder.setPath(req.getRequestPath());
    }

    private void configureParameters(HttpRequestBuilder req, URIBuilder uriBuilder) {
        for (Parameter parameter : req.getParameters()) {
            uriBuilder = uriBuilder.setParameter(parameter.getParameterName(), parameter.getParameterValue());
        }
    }

    private HttpRequestBase createHttpRequestBase(HttpMethod m) {
        switch (m) {
            case GET:
                return new HttpGet();
            case PUT:
                return new HttpPut();
            case POST:
                return new HttpPost();
            case DELETE:
                return new HttpDelete();
            case HEAD:
                return new HttpHead();
            case OPTIONS:
                return new HttpOptions();
            case TRACE:
                return new HttpTrace();
//            case CONNECT:
//                return new HttpConnect()
            case PATCH:
                return new HttpPatch();
            default:
                throw new IllegalArgumentException("Unsupported method " + m + "'");
        }
    }

    private void configureHeaders(HttpRequestBuilder req,
                                  HttpRequestBase requestBase) {

        if (req.getContentType() != null) {
            requestBase.setHeader(HttpHeaders.CONTENT_TYPE, req.getContentType());
        }
        requestBase.setHeaders(convert(req.getHeaders()));
    }

    private void configureBasicAuth(HttpRequestBuilder req,
                                    HttpHost targetHost,
                                    HttpClientContext context) {
        if (req.getUsername() == null && req.getPassword() == null) {
            return;
        }

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                new UsernamePasswordCredentials(req.getUsername(), req.getPassword()));

        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();

        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        // Add AuthCache to the execution context
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);
    }

    private HttpHost getHttpHost() {
        HttpHost httpHost = null;
        if (hostUri != null) {
            httpHost = new HttpHost(hostUri.getHost(), hostUri.getPort(), hostUri.getScheme());
        }
        return httpHost;
    }

    private static org.apache.http.Header[] convert(List<bdi.glue.http.common.Header> headers) {
        org.apache.http.Header[] array = new org.apache.http.Header[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            bdi.glue.http.common.Header h = headers.get(i);
            array[i] = new BasicHeader(h.name, h.value);
        }
        return array;
    }

}

package bdi.glue.http.common;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.StringAssert;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class RawHttpStepdefs {

    private final HttpWorld httpWorld;

    public RawHttpStepdefs(HttpWorld httpWorld) {
        this.httpWorld = httpWorld;
    }

    //-------------------------------------------------------------------------
    //   / ____|_   _\ \    / /  ____| \ | |
    //  | |  __  | |  \ \  / /| |__  |  \| |
    //  | | |_ | | |   \ \/ / |  __| | . ` |
    //  | |__| |_| |_   \  /  | |____| |\  |
    //   \_____|_____|   \/   |______|_| \_|
    //-------------------------------------------------------------------------

    @Given("^an host set to \"([^\"]*)\"$")
    public void defineHost(String uriAsString) throws Throwable {
        URI uri = new URI(uriAsString);
        httpWorld
                .getHttpGateway()
                .defineHost(uri);
    }

    @Given("^the request's header \"([^\"]*)\" has been set to \"([^\"]*)\"$")
    public void defineRequestHeader(String headerName, String headerValue) {
        httpWorld
                .currentRequestBuilder()
                .header(new Header(headerName, headerValue));
    }

    @Given("^(?:a|the) \"([^\"]*)\" header set to \"([^\"]*)\"$")
    public void defineRequestHeaderAlt(String headerName, String headerValue) {
        defineRequestHeader(headerName, headerValue);
    }

    @Given("^the following request's header:$")
    public void defineRequestHeaders(List<Header> headers) {
        httpWorld
                .currentRequestBuilder()
                .headers(headers);
    }

    @Given("^the request method has been set to (.*)$")
    public void defineRequestMethod(String methodAsString) {
        httpWorld
                .currentRequestBuilder()
                .method(methodAsString);
    }

    @Given("^the request has no cookie named (.*)$")
    public void removeCookieFromHttpRequest(String cookieName) {
        httpWorld
                .currentRequestBuilder()
                .cookieToRemove(cookieName);
    }

    @Given("^the request cookie \"([^\"]*)\" has been set to \"([^\"]*)\"$")
    public void assertRequestCookieShouldBeEqualTo(String cookieName, String cookieValue) {
        httpWorld
                .currentRequestBuilder()
                .cookie(cookieName, cookieValue);
    }

    @Given("^basic auth credentials set to \"([^\"]*)\" and \"([^\"]*)\"$")
    public void defineBasicAuthCredentials(String username, String password) throws Throwable {
        httpWorld
                .currentRequestBuilder()
                .basicAuthCredentials(username, password);
    }

    /**
     * Defined in the HTTP/1.1 Standard, section 14.1, the Accept: header lists the MIME Types of the
     * media that the agent is willing to process. It is comma-separated lists of MIME type, each
     * combined with a quality factor, as parameters giving the relative degree of preference between
     * the different MIME Types lists.
     * <p/>
     * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Content_negotiation">Content negotiation - MDN</a>
     * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec12.html">rfc2616 section 12</a>
     *
     * @param contentTypes comma-separated lists of MIME type (e.g. "application/json" or "text/html" ...)
     * @throws Throwable
     */
    @Given("^a content format negotiation set to \"([^\"]*)\"$")
    public void defineAcceptHeader(String contentTypes) throws Throwable {
        httpWorld
                .currentRequestBuilder()
                .header(new Header("Accept", contentTypes));
    }

    //-------------------------------------------------------------------------
    //    \ \        / / |  | |  ____| \ | |
    //     \ \  /\  / /| |__| | |__  |  \| |
    //      \ \/  \/ / |  __  |  __| | . ` |
    //       \  /\  /  | |  | | |____| |\  |
    //        \/  \/   |_|  |_|______|_| \_|
    //-------------------------------------------------------------------------

    @When("^a request is made to \"([^\"]+)\"$")
    public void invokeGET(String url) {
        invokeUsingMethod("GET", url);
    }

    @When("^a ([a-zA-Z]+) request is made to \"([^\"]+)\"$")
    public void invokeUsingMethod(String methodAsString, String url) {
        HttpRequestBuilder req = httpWorld
                .currentRequestBuilder()
                .method(methodAsString)
                .requestPath(url);
        invoke(req);
    }

    @When("^a ([a-zA-Z]+) request is made to \"([^\"]+)\" with the following parameters:$")
    public void invokeUsingMethod(String methodAsString, String url, List<Parameter> parameters) {
        HttpRequestBuilder req = httpWorld
                .currentRequestBuilder()
                .method(methodAsString)
                .requestPath(url)
                .parameters(parameters);
        invoke(req);
    }


    @When("^a ([a-zA-Z]+) request is made to \"([^\"]+)\" with content type \"([^\"]+)\" with:$")
    public void invokeUsingMethod(String methodAsString, String url, String contentType, String body) {
        HttpRequestBuilder req = httpWorld
                .currentRequestBuilder()
                .method(methodAsString)
                .requestPath(url)
                .contentType(contentType)
                .body(body);
        invoke(req);
    }

    @When("^a json ([a-zA-Z]+) request is made to \"([^\"]+)\"  with:$")
    public void invokeJsonUsingMethod(String methodAsString, String url, String body) {
        invokeUsingMethod(methodAsString, url, "application/json", body);
    }

    @When("^I follow redirect$")
    public void followRedirect() {
        throw new UnsupportedOperationException();
    }

    private void invoke(HttpRequestBuilder req) {
        HttpResponse response = httpWorld.getHttpGateway().invoke(req);
        httpWorld.lastResponse(response);
    }

    //-------------------------------------------------------------------------
    //   _______ _    _ ______ _   _
    //  |__   __| |  | |  ____| \ | |
    //     | |  | |__| | |__  |  \| |
    //     | |  |  __  |  __| | . ` |
    //     | |  | |  | | |____| |\  |
    //     |_|  |_|  |_|______|_| \_|
    //-------------------------------------------------------------------------

    @Then("^the response status code should be (\\d+)$")
    public void assertResponseCodeIs(int expectedCode) {
        HttpResponse response = httpWorld.lastResponse();
        assertThat(response.statusCode()).isEqualTo(expectedCode);
    }

    @Then("^the response status code should be (\\d+) \\((.+)\\)$")
    public void assertResponseCodeIs(int expectedCode, String explanation) {
        HttpResponse response = httpWorld.lastResponse();
        assertThat(response.statusCode()).isEqualTo(expectedCode);

        HttpStatus httpStatus = HttpStatus.valueOf(response.statusCode());
        assertThat(httpStatus.name().replace("_", " ")).isEqualToIgnoringCase(explanation);
    }

    @Then("^the response should have the cookie (.*)$")
    public void assertResponseCookieIsPresent(String cookieName) {
        HttpResponse response = httpWorld.lastResponse();
        List<Cookie> cookies = response.getCookies(cookieName);
        assertThat(cookies).isNotEmpty();
    }

    @Then("^the response's cookie \"([^\"]*)\" should be set to \"([^\"]*)\"$")
    public void assertResponseCookieShouldBeEqualTo(String cookieName, String expectedValue) {
        HttpResponse response = httpWorld.lastResponse();
        List<Cookie> cookies = response.getCookies(cookieName);
        assertThat(cookies).isNotEmpty();
        Optional<Cookie> optional = cookies.stream().filter((c) -> c.value().equals(expectedValue)).findFirst();
        assertThat(optional.isPresent())
                .describedAs("None matches: " + cookies)
                .isTrue();
    }

    @Then("^the response's body should (start with|end with|match|contain|be) \"(.*)\"$")
    public void assertResponseBodySatisfies(String comparator, String expectedText) {
        HttpResponse response = httpWorld.lastResponse();
        StringAssert stringAssert = assertThat(response.bodyAsText());
        switch (comparator) {
            case "match":
                stringAssert.matches(expectedText);
                break;
            case "contain":
                stringAssert.contains(expectedText);
                break;
            case "be":
                stringAssert.isEqualTo(expectedText);
                break;
            case "start with":
                stringAssert.startsWith(expectedText);
                break;
            case "end with":
                stringAssert.endsWith(expectedText);
                break;
            default:
                throw new IllegalArgumentException("Unsupported comparator '" + comparator + "'");
        }
    }

    @Then("^the response's body should (match|contain|be):$")
    public void assertResponseBodySatisfiesPyString(String comparator, String expectedText) {
        assertResponseBodySatisfies(comparator, expectedText);
    }

    @Then("^the response's body should not (match|contain|be) \"([^\"]*)\"$")
    public void assertResponseBodyDoNotSatisfy(String comparator, String expectedText) {
        HttpResponse response = httpWorld.lastResponse();
        StringAssert stringAssert = assertThat(response.bodyAsText());
        switch (comparator) {
            case "match":
                stringAssert.doesNotMatch(expectedText);
                break;
            case "contain":
                stringAssert.doesNotContain(expectedText);
                break;
            case "be":
                stringAssert.isNotEqualTo(expectedText);
                break;
            default:
                throw new IllegalArgumentException("Unsupported comparator '" + comparator + "'");
        }
    }

    @Then("^the json response's body should (contain|be):$")
    public void assertResponseJsonBodySatisfies(String comparator, String expectedText) throws JSONException {
        HttpResponse response = httpWorld.lastResponse();
        String actual = response.bodyAsText();

        JSONCompareMode mode = JSONCompareMode.LENIENT;
        if (comparator.equals("be"))
            mode = JSONCompareMode.NON_EXTENSIBLE;

        JSONCompare.compareJSON(expectedText, actual, mode);
    }

    @Then("^the response's header \"([^\"]*)\" should be set to \"([^\"]*)\"$")
    public void assertResponseHeaderValueIsEqualTo(String headerName, String headerValue) {
        throw new UnsupportedOperationException();
    }

    @Then("^the response's header \"([^\"]*)\" should not be set to \"([^\"]*)\"$")
    public void assertResponseHeaderValueIsNotEqualTo(String headerName, String headerValue) {
        throw new UnsupportedOperationException();
    }

    @Then("^the response should indicate a redirect$")
    public void assertResponseHasBeenRedirected() {
        throw new UnsupportedOperationException();
    }

    @Then("^the response should not indicate a redirect$")
    public void assertResponseHasNotBeenRedirected() {
        throw new UnsupportedOperationException();
    }

}

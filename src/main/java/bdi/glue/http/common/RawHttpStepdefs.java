package bdi.glue.http.common;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.StringAssert;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;

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
        // nothing to do
    }

    @Given("^the request cookie \"([^\"]*)\" has been set to \"([^\"]*)\"$")
    public void assertRequestCookieShouldBeEqualTo(String cookieName, String cookieValue) {
        httpWorld
                .currentRequestBuilder()
                .cookie(cookieName, cookieValue);
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
                .url(url);
        invoke(req);
    }

    @When("^a ([a-zA-Z]+) request is made to \"([^\"]+)\" with content type \"([^\"]+)\" with:$")
    public void invokeUsingMethod(String methodAsString, String url, String contentType, String body) {
        HttpRequestBuilder req = httpWorld
                .currentRequestBuilder()
                .method(methodAsString)
                .url(url)
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

    @Then("^the response's body should (match|contain|be) \"([^\"]*)\"$")
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
        if(comparator.equals("be"))
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

package bdi.glue.http.common;

import bdi.glue.util.StringAssertions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.net.URI;
import java.net.URISyntaxException;
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
    public void given_a_host_set_to(String uriAsString) throws URISyntaxException {
        URI uri = new URI(uriAsString);
        httpWorld
                .getHttpGateway()
                .defineHost(uri);
    }

    @Given("^the request's header \"([^\"]*)\" has been set to \"([^\"]*)\"$")
    public void given_request_header_set_to(String headerName, String headerValue) {
        httpWorld
                .currentRequestBuilder()
                .header(new Header(headerName, headerValue));
    }

    @Given("^(?:a|the) \"([^\"]*)\" header set to \"([^\"]*)\"$")
    public void given_request_header_set_to_(String headerName, String headerValue) {
        given_request_header_set_to(headerName, headerValue);
    }

    @Given("^the following request's header:$")
    public void given_request_headers_set_to(List<Header> headers) {
        httpWorld
                .currentRequestBuilder()
                .headers(headers);
    }

    @Given("^the request method has been set to (.*)$")
    public void given_request_method_set_to(String methodAsString) {
        httpWorld
                .currentRequestBuilder()
                .method(methodAsString);
    }

    @Given("^the request has no cookie named (.*)$")
    public void given_no_cookie_named(String cookieName) {
        httpWorld
                .currentRequestBuilder()
                .cookieToRemove(cookieName);
    }

    @Given("^the request cookie \"([^\"]*)\" has been set to \"([^\"]*)\"$")
    public void given_cookie_set_to(String cookieName, String cookieValue) {
        httpWorld
                .currentRequestBuilder()
                .cookie(cookieName, cookieValue);
    }

    @Given("^basic auth credentials set to \"([^\"]*)\" and \"([^\"]*)\"$")
    public void given_basic_auth_credentials_set_to(String username, String password) {
        httpWorld
                .currentRequestBuilder()
                .basicAuthCredentials(username, password);
    }

    /**
     * Defined in the HTTP/1.1 Standard, section 14.1, the Accept: header lists the MIME Types of the
     * media that the agent is willing to process. It is comma-separated lists of MIME type, each
     * combined with a quality factor, as parameters giving the relative degree of preference between
     * the different MIME Types lists.
     *
     *
     * * [Content negotiation - MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Content_negotiation)
     * * [rfc2616 section 12](http://www.w3.org/Protocols/rfc2616/rfc2616-sec12.html)
     *
     * @param contentTypes comma-separated lists of MIME type (e.g. "application/json" or "text/html" ...)
     */
    @Given("^a content format negotiation set to \"([^\"]*)\"$")
    public void given_content_format_negociation_set_to(String contentTypes) {
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
    public void when_a_GET_request_is_made_to(String url) {
        when_request_is_made_to("GET", url);
    }

    @When("^a ([a-zA-Z]+) request is made to \"([^\"]+)\"$")
    public void when_request_is_made_to(String methodAsString, String url) {
        HttpRequestBuilder req = httpWorld
                .currentRequestBuilder()
                .method(methodAsString)
                .requestPath(url);
        invoke(req);
    }

    @When("^a ([a-zA-Z]+) request is made to \"([^\"]+)\" with the following parameters:$")
    public void when_request_is_made_to_with_parameters(String methodAsString, String url, List<Parameter> parameters) {
        HttpRequestBuilder req = httpWorld
                .currentRequestBuilder()
                .method(methodAsString)
                .requestPath(url)
                .parameters(parameters);
        invoke(req);
    }


    @When("^a ([a-zA-Z]+) request is made to \"([^\"]+)\" with content type \"([^\"]+)\" with:$")
    public void when_request_is_made_to_with_content_type_and_body(String methodAsString, String url, String contentType, String body) {
        HttpRequestBuilder req = httpWorld
                .currentRequestBuilder()
                .method(methodAsString)
                .requestPath(url)
                .contentType(contentType)
                .body(body);
        invoke(req);
    }

    @When("^a json ([a-zA-Z]+) request is made to \"([^\"]+)\"  with:$")
    public void when_request_is_made_to_with_json_body(String methodAsString, String url, String body) {
        when_request_is_made_to_with_content_type_and_body(methodAsString, url, "application/json", body);
    }

    @When("^I follow redirect$")
    public void when_redirect_is_followed() {
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
    public void then_response_status_code_should_be(int expectedCode) {
        HttpResponse response = httpWorld.lastResponse();
        assertThat(response.statusCode()).isEqualTo(expectedCode);
    }

    @Then("^the response status code should be (\\d+) \\((.+)\\)$")
    public void then_response_status_code_should_be(int expectedCode, String explanation) {
        HttpResponse response = httpWorld.lastResponse();
        assertThat(response.statusCode()).isEqualTo(expectedCode);

        HttpStatus httpStatus = HttpStatus.valueOf(response.statusCode());
        assertThat(httpStatus.name().replace("_", " ")).isEqualToIgnoringCase(explanation);
    }

    @Then("^the response should have the cookie (.*)$")
    public void then_response_should_have_the_cookie_named(String cookieName) {
        HttpResponse response = httpWorld.lastResponse();
        List<Cookie> cookies = response.getCookies(cookieName);
        assertThat(cookies).isNotEmpty();
    }

    @Then("^the response's cookie \"([^\"]*)\" should be set to \"([^\"]*)\"$")
    public void then_response_should_have_the_cookie_set_to(String cookieName, String expectedValue) {
        HttpResponse response = httpWorld.lastResponse();
        List<Cookie> cookies = response.getCookies(cookieName);
        assertThat(cookies).isNotEmpty();
        Optional<Cookie> optional = cookies.stream().filter((c) -> c.value().equals(expectedValue)).findFirst();
        assertThat(optional.isPresent())
                .describedAs("None matches: " + cookies)
                .isTrue();
    }

    @Then("^the response's body should (start with|end with|match|contain|be) \"(.*)\"$")
    public void then_response_body_should_satisfy(String comparator, String expectedText) {
        HttpResponse response = httpWorld.lastResponse();

        StringAssertions.Mode mode = StringAssertions.lookupMode(comparator);
        StringAssertions.apply(mode, response.bodyAsText(), expectedText);
    }

    @Then("^the response's body should (match|contain|be):$")
    public void then_response_body_should_satisfy_(String comparator, String expectedText) {
        then_response_body_should_satisfy(comparator, expectedText);
    }

    @Then("^the response's body should not (match|contain|be) \"([^\"]*)\"$")
    public void then_response_body_should_not_satisfy(String comparator, String expectedText) {
        HttpResponse response = httpWorld.lastResponse();

        StringAssertions.Mode mode = StringAssertions.lookupMode(comparator);
        StringAssertions.apply(mode.negate(), response.bodyAsText(), expectedText);
    }

    @Then("^the json response's body should (contain|be):$")
    public void then_response_json_body_should_satisfy(String comparator, String expectedText) throws JSONException {
        HttpResponse response = httpWorld.lastResponse();
        String actual = response.bodyAsText();

        JSONCompareMode mode = JSONCompareMode.LENIENT;
        if (comparator.equals("be"))
            mode = JSONCompareMode.NON_EXTENSIBLE;

        JSONCompare.compareJSON(expectedText, actual, mode);
    }

    @Then("^the response's header \"([^\"]*)\" should be set to \"([^\"]*)\"$")
    public void then_response_header_should_be_set_to(String headerName, String headerValue) {
        throw new UnsupportedOperationException();
    }

    @Then("^the response's header \"([^\"]*)\" should not be set to \"([^\"]*)\"$")
    public void then_response_header_should_not_be_set_to(String headerName, String headerValue) {
        throw new UnsupportedOperationException();
    }

    @Then("^the response should indicate a redirect$")
    public void then_response_should_indicate_a_redirect() {
        throw new UnsupportedOperationException();
    }

    @Then("^the response should not indicate a redirect$")
    public void then_response_should_not_indicate_a_redirect() {
        throw new UnsupportedOperationException();
    }

}

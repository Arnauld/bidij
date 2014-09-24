package bdi.glue.http.common;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Header {
    public String name;
    public String value;

    public Header() {
    }

    public Header(String name, String value) {
        this.name = name;
        this.value = value;
    }
}

package bdi.glue.testdefs;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class User {
    public final Integer id;
    public final String firstname;
    public final String lastname;

    public User(Integer id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User(int id, User user) {
        this.id = id;
        this.firstname = user.firstname;
        this.lastname = user.lastname;
    }
}

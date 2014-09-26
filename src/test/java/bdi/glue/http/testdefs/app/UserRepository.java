package bdi.glue.http.testdefs.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class UserRepository {
    private AtomicInteger idGen = new AtomicInteger();
    private Map<Integer, User> users = new ConcurrentHashMap<>();

    public UserRepository() {
        this.users = new HashMap<>();
        initUsers();
    }

    public int newUserId() {
        return idGen.incrementAndGet();
    }

    protected void initUsers() {
        for (String n : Arrays.asList(
                "Lugh Aranrhod",
                "Brighid Meadhbh",
                "Belenos Clídna",
                "Ailill Bedwyr",
                "Brigid Toadsquawk",
                "Medraut Eigyr",
                "Muirenn Maeve",
                "Cian Finnguala",
                "Brighid Lugh",
                "Fionnghuala Lugos",
                "Isolde Feidlimid",
                "Muirne Diarmaid",
                "Brigid Aodhán",
                "Aranrhod Tristan"

        )) {
            String[] split = n.split(" ");
            addUser(new User(newUserId(), split[0], split[1]));
        }
    }

    public Optional<User> get(Integer id) {
        if(id==null)
            return Optional.empty();

        User user = users.get(id);
        return Optional.ofNullable(user);
    }

    public User addUser(User user) {
        if(user.id == null)
            user = new User(newUserId(), user);
        users.put(user.id, user);
        return user;
    }

    public List<User> all() {
        return new ArrayList<>(users.values());
    }
}

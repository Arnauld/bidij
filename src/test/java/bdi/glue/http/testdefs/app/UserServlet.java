package bdi.glue.http.testdefs.app;

import bdi.glue.http.common.HttpStatus;
import bdi.glue.testdefs.User;
import bdi.glue.testdefs.UserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class UserServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(UserServlet.class);
    private final UserRepository userRepository = new UserRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Handle {} | {} | {}",
                req.getProtocol(),
                req.getMethod(),
                req.getRequestURI());

        String body = IOUtils.toString(req.getReader());
        logger.info("Body '{}'", body);

        User user = getGson().fromJson(body, User.class);
        if(user==null) {
            resp.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        Optional<User> previous = userRepository.get(user.id);
        User userAdded = userRepository.addUser(user);

        int status = previous.isPresent() ? SC_OK : SC_CREATED;

        writeLastAcessCookie(resp);
        writeJsonResponse(resp, status, userAdded);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Handle {} | {} | {}",
                req.getProtocol(),
                req.getMethod(),
                req.getRequestURI());

        List<User> users = null;
        boolean firstOnly = false;

        String path = req.getRequestURI();
        if (path.equals("/users/all")) {
            users = userRepository.all();
        }

        int status = SC_OK;
        Object ret = firstOnly ? users.get(0) : users;

        writeLastAcessCookie(resp);
        writeJsonResponse(resp, status, ret);
    }

    private void writeLastAcessCookie(HttpServletResponse resp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        resp.addCookie(new Cookie("last-access", sdf.format(new Date())));
    }

    private void writeJsonResponse(HttpServletResponse resp, int status, Object ret) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(status);
        Gson gson = getGson();
        gson.toJson(ret, resp.getWriter());
    }

    private Gson getGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }
}

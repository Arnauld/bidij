package bdi.glue.http.testdefs;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SampleApp extends AbstractHandler {

    private Logger logger = LoggerFactory.getLogger(SampleApp.class);

    private AtomicInteger idGen = new AtomicInteger();

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException {

        String method = baseRequest.getMethod();
        String path = baseRequest.getPathInfo();

        logger.info("Handle {} ",
                baseRequest.getProtocol(),
                baseRequest.getMethod(),
                baseRequest.getPathInfo());

        if (path.endsWith("cookie")) {
            response.addCookie(new Cookie("auth-token", "12345678-ABCDEF"));
        }

        if (method.equalsIgnoreCase("post") && path.endsWith("/users")) {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            response.getWriter().println("{" +
                    "'status':'ok', " +
                    "'timestamp':" + System.currentTimeMillis() + "," +
                    "'id':" + idGen.incrementAndGet() +
                    "}");
            return;
        }

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("<h1>Hello World</h1>");
    }

    public static Server start(int port) throws Exception {
        Server server = new Server(port);
        server.setHandler(new SampleApp());
        server.start();
        return server;
    }

    public static void main(String[] args) throws Exception {
        start(8080).join();
    }
}

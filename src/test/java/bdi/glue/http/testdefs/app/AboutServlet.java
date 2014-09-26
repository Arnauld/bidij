package bdi.glue.http.testdefs.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class AboutServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(AboutServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        String path = req.getRequestURI();

        logger.info("Handle {} | {} | {}",
                req.getProtocol(),
                req.getMethod(),
                req.getRequestURI());

        int status = HttpServletResponse.SC_OK;

        About about = new About();

        String accept = req.getHeader("Accept");
        ContentType contentType = ContentType.parse(accept);
        if (contentType.getMimeType().equals("application/json")) {
            resp.setContentType("application/json");
            resp.setStatus(status);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(about, resp.getWriter());
        } else if (contentType.getMimeType().equals("text/html")) {

            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(status);
            String html = ("<html><body>" +
                    "<ul>" +
                    "<li>version: $version</li>" +
                    "<li>timestamp: timestamp</li>" +
                    "</ul>" +
                    "</body></html>")
                    .replace("$version", about.version)
                    .replace("$timestamp", "" + about.timestamp);
            resp.getWriter().println(html);
        }
    }


}

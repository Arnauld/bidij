package bdi.glue.http.testdefs.app;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SampleApp {

    public static void main(String[] args) throws Exception {
        new SampleApp().start(8080).join();
    }

    public Server start(int port) throws Exception {
        Server server = new Server(port);
        configureHandler(server);
        server.start();
        return server;
    }

    public Server start(int port,
                        int securedPort,
                        String keyStorePath) throws Exception {
        Server server = new Server();
        configureSslSupport(port, securedPort, keyStorePath, server);
        configureHandler(server);
        server.start();
        return server;
    }

    private void configureSslSupport(int port, int securedPort, String keyStorePath, Server server) {
        // HTTP Configuration
        // HttpConfiguration is a collection of configuration information appropriate for http and https. The default
        // scheme for http is <code>http</code> of course, as the default for secured http is <code>https</code> but
        // we show setting the scheme to show it can be done.  The port for secured communication is also set here.
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(securedPort);
        http_config.setOutputBufferSize(32768);

        // HTTP connector
        // The first server connector we create is the one for http, passing in the http configuration we configured
        // above so it can get things like the output buffer size, etc. We also set the port (8080) and configure an
        // idle timeout.
        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
        http.setPort(port);
        http.setIdleTimeout(30000);

        // SSL Context Factory for HTTPS and SPDY
        // SSL requires a certificate so we configure a factory for ssl contents with information pointing to what
        // keystore the ssl connection needs to know about. Much more configuration is available the ssl context,
        // including things like choosing the particular certificate out of a keystore to be used.
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(keyStorePath);
        sslContextFactory.setKeyStorePassword("OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4");
        sslContextFactory.setKeyManagerPassword("OBF:1u2u1wml1z7s1z7a1wnl1u2g");
//        sslContextFactory.setKeyStorePassword("OBF:1y0y1vn61vnw1zsv1vn61vnw1y0s");
//        sslContextFactory.setKeyManagerPassword("OBF:1y0y1vn61vnw1zsv1vn61vnw1y0s");

        // HTTPS Configuration
        // A new HttpConfiguration object is needed for the next connector and you can pass the old one as an
        // argument to effectively clone the contents. On this HttpConfiguration object we add a
        // SecureRequestCustomizer which is how a new connector is able to resolve the https connection before
        // handing control over to the Jetty Server.
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        // HTTPS connector
        // We create a second ServerConnector, passing in the http configuration we just made along with the
        // previously created ssl context factory. Next we set the port and a longer idle timeout.
        ServerConnector https = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                new HttpConnectionFactory(https_config));
        https.setPort(securedPort);
        https.setIdleTimeout(500000);

        // Here you see the server having multiple connectors registered with it, now requests can flow into the server
        // from both http and https urls to their respective ports and be processed accordingly by jetty. A simple
        // handler is also registered with the server so the example has something to pass requests off to.

        // Set the connectors
        server.setConnectors(new Connector[]{http, https});
    }

    private void configureHandler(Server server) {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setSecurityHandler(basicAuth("carmen", "mccallum", "Private!"));
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new AboutServlet()), "/about");
        context.addServlet(new ServletHolder(new UserServlet()), "/users/*");
        context.addServlet(new ServletHolder(new UserServlet()), "/auth/users/*");
    }

    protected SecurityHandler basicAuth(String username, String password, String realm) {

        HashLoginService l = new HashLoginService();
        l.putUser(username, Credential.getCredential(password), new String[]{"user"});
        l.setName(realm);

        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{"user"});
        constraint.setAuthenticate(true);

        ConstraintMapping cm = new ConstraintMapping();
        cm.setConstraint(constraint);
        cm.setPathSpec("/auth/*");

        ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
        csh.setAuthenticator(new BasicAuthenticator());
        csh.setRealmName("myrealm");
        csh.addConstraintMapping(cm);
        csh.setLoginService(l);

        return csh;

    }
}

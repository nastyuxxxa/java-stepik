package example;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import resources.ResourceServerController;
import resources.ResourceServerControllerMBean;
import resources.ResourceServerI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import resources.TestResource;
import servlets.ResourceServlet;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class Main {
    static final Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            logger.error("Use port as the first argument");
            //System.exit(1);
        }

        String portString = "8080";
        int port = Integer.valueOf(portString);

        logger.info("Starting at http://127.0.0.1:" + portString);

        ResourceServerI resourceServer = new TestResource();

        ResourceServerControllerMBean resourceServerController = new ResourceServerController(resourceServer);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("Admin:type=ResourceServerController");
        mbs.registerMBean(resourceServerController, name);

        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new ResourceServlet(resourceServer)), ResourceServlet.PAGE_URL);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});
        server.setHandler(handlers);

        server.start();
        System.out.println("Server started");
        logger.info("Server started");

        server.join();
    }
}

package servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resources.ResourceServerI;
import sax.ReadXmlFileSAX;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResourceServlet extends HttpServlet {
    static final Logger logger = LogManager.getLogger(ResourceServlet.class.getName());
    public static final String PAGE_URL = "/resources";
    private final ResourceServerI resourceServer;

    public ResourceServlet(ResourceServerI resourceServer) {
        this.resourceServer = resourceServer;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        String path = request.getParameter("path");
        if (!path.isEmpty()) {
            logger.info("Path: {}", path);

            ResourceServerI resourcesFromXml = (ResourceServerI) ReadXmlFileSAX.readXml(path);

            String name = resourceServer.getName();
            int age = resourceServer.getAge();

            resourceServer.setName(resourcesFromXml.getName());
            resourceServer.setAge(resourcesFromXml.getAge());

            logger.info("Name: {}. Age: {}", name, age);

            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            logger.info("Path parameter is empty");
            response.getWriter().println("Path parameter is empty");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

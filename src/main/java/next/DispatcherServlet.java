package next;

import next.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private RequestMapping requestMapping = new RequestMapping();

    @Override
    public void init() throws ServletException {
        requestMapping.initMapping();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUrl = req.getRequestURI();
        log.info("Method: {}, Requst URI: {}", req.getMethod(), requestUrl);

        Controller controller = requestMapping.getController(requestUrl);
        try {
            String viewName = controller.execute(req, resp);
            move(viewName, req, resp);

        } catch (Throwable e) {
            log.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(String viewName, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }
        RequestDispatcher rd = req.getRequestDispatcher(viewName);
        rd.forward(req, resp);
    }
}

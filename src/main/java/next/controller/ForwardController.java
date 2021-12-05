package next.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController implements Controller {

    public final String forwardUrl;

    public ForwardController(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return forwardUrl;
    }
}

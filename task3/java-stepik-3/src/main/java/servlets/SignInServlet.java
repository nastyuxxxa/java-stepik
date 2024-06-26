package servlets;

import accounts.AccountService;
import accounts.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class SignInServlet extends HttpServlet {
    private final AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    private void sendResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(status);
        response.getWriter().println(message);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (login == null || password == null) {
            sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, "");
            return;
        }

        try {
            User user = accountService.getUserByLogin(login);
            if (user == null || !user.getPassword().equals(password)) {
                sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        sendResponse(response, HttpServletResponse.SC_OK, "Authorized: " + login);
    }
}

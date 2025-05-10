package com.bank.servlet;

import com.bank.storage.UserStorage;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        com.bank.model.User user = UserStorage.login(username, password);
        if (user != null) {
            request.getSession().setAttribute("user", user.getUsername());
            response.sendRedirect("home");
        } else {
            request.setAttribute("error", "Błędny login lub hasło");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
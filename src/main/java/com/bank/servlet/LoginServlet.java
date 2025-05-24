package com.bank.servlet;

import com.bank.model.User;
import com.bank.storage.UserStorage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = UserStorage.login(username, password);
        if (user != null) {
            // Если логин успешен — записываем username в сессию
            request.getSession().setAttribute("user", user.getUsername());
            response.sendRedirect("home");
        } else {
            // Иначе показываем ошибку на странице логина
            request.setAttribute("error", "Incorrect username or password");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}

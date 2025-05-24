package com.bank.servlet;

import com.bank.model.User;
import com.bank.storage.UserStorage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    // Выводит информацию о балансе и истории операций

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = (String) request.getSession().getAttribute("user");
        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = UserStorage.getUser(username);
        request.setAttribute("balance", user.getBalance());     // Передаем баланс в JSP
        request.setAttribute("history", user.getHistory());     // Передаем историю операций
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
}

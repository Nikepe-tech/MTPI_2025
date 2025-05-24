package com.bank.servlet;

import com.bank.storage.UserStorage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (UserStorage.register(username, password)) {
            // Если регистрация прошла успешно — редирект на логин
            response.sendRedirect("login.jsp");
        } else {
            // Иначе ошибка: пользователь уже существует
            request.setAttribute("error", "User already exists");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}

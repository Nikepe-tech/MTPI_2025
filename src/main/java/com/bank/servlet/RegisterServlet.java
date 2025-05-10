package com.bank.servlet;

import com.bank.storage.UserStorage;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (UserStorage.register(username, password)) {
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("error", "Użytkownik już istnieje");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
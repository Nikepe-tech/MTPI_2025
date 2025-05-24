package com.bank.servlet;

import com.bank.storage.UserStorage;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/withdraw")
public class WithdrawServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = (String) request.getSession().getAttribute("user");
        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(request.getParameter("amount"));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount");
            request.getRequestDispatcher("withdraw.jsp").forward(request, response);
            return;
        }

        boolean success = UserStorage.getUser(username).withdraw(amount);
        if (success) {
            UserStorage.saveToFile(); // Сохраняем изменения в файл
            response.sendRedirect("home");
        } else {
            request.setAttribute("error", "Insufficient funds or invalid amount");
            request.getRequestDispatcher("withdraw.jsp").forward(request, response);
        }
    }
}

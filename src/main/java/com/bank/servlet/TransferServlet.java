package com.bank.servlet;

import com.bank.storage.UserStorage;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fromUser = (String) request.getSession().getAttribute("user");
        if (fromUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String toUser = request.getParameter("toUsername");
        double amount;

        try {
            amount = Double.parseDouble(request.getParameter("amount"));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount");
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
            return;
        }

        // Пытаемся совершить перевод через UserStorage
        boolean success = UserStorage.transfer(fromUser, toUser, amount);

        if (success) {
            response.sendRedirect("home");
        } else {
            request.setAttribute("error", "Transfer failed. Check recipient username and your balance.");
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
        }
    }
}

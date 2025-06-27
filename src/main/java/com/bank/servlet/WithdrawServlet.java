/**
 * Servlet obsługujący wypłatę środków z konta użytkownika.
 * Sprawdza dostępność środków i wykonuje operację.
 */
package com.bank.servlet;

import com.bank.storage.UserStorage;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/withdraw")
public class WithdrawServlet extends HttpServlet {
    /**
     * Obsługuje żądanie POST z formularza wypłaty.
     * Jeśli użytkownik ma wystarczające środki, aktualizuje saldo.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Pobiera nazwę użytkownika z sesji oraz kwotę wypłaty z formularza
        String username = (String) request.getSession().getAttribute("user");
        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Konwertuje kwotę na wartość liczbową
        double amount;
        try {
            amount = Double.parseDouble(request.getParameter("amount"));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount");
            request.getRequestDispatcher("withdraw.jsp").forward(request, response);
            return;
        }

        // Próbuję wykonać wypłatę środków
        UserStorage storage = new UserStorage();
        boolean success = storage.withdraw(username, amount);
        if (success) {
            // Jeśli wypłata się powiodła
            response.sendRedirect("home");
        } else {
            // Wypłata nie powiodła się — brak wystarczających środków
            request.setAttribute("error", "Insufficient funds or invalid amount");
            request.getRequestDispatcher("withdraw.jsp").forward(request, response);
        }
    }
}

/**
 * Servlet obsługujący przelewy między użytkownikami.
 * Weryfikuje dane i wykonuje transfer środków.
 */
package com.bank.servlet;

import com.bank.storage.UserStorage;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {
    /**
     * Obsługuje żądanie POST z formularza przelewu.
     * Sprawdza poprawność danych i wykonuje przelew.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Pobiera nazwę użytkownika z sesji
        String fromUser = (String) request.getSession().getAttribute("user");
        if (fromUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Pobiera nazwę odbiorcy z formularza
        String toUser = request.getParameter("toUsername");
        double amount;

        // Przetwarza kwotę jako wartość liczbową
        try {
            amount = Double.parseDouble(request.getParameter("amount"));
        } catch (NumberFormatException e) {
            // Obsługa błędnego formatu liczby
            request.setAttribute("error", "Invalid amount");
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
            return;
        }

        // Próbuje wykonać przelew między użytkownikami
        UserStorage storage = new UserStorage();
        boolean success = storage.transfer(fromUser, toUser, amount);

        if (success) {
            // Przelew się powiódł
            response.sendRedirect("home");
        } else {
            // Przelew się nie powiódł — błąd salda lub odbiorcy
            request.setAttribute("error", "Transfer failed. Check recipient username and your balance.");
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
        }
    }
}

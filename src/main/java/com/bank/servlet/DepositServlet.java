/**
 * Servlet obsługujący dodanie środków do konta użytkownika.
 * Wykonuje operację wpłaty i zapisuje transakcję.
 */
package com.bank.servlet;

import com.bank.storage.UserStorage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deposit")
public class DepositServlet extends HttpServlet {
    /**
     * Obsługuje żądanie POST z formularza wpłaty.
     * Sprawdza dane i aktualizuje saldo użytkownika.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Pobiera nazwę użytkownika z sesji oraz kwotę wpłaty z formularza
        String username = (String) request.getSession().getAttribute("user");
        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Konwertuje kwotę na liczbę zmiennoprzecinkową
        double amount = Double.parseDouble(request.getParameter("amount"));
        // Wykonuje operację wpłaty i zapisuje stan konta
        UserStorage storage = new UserStorage();
        storage.deposit(username, amount);

        // Przekierowuje użytkownika do strony głównej
        response.sendRedirect("home");
    }
}

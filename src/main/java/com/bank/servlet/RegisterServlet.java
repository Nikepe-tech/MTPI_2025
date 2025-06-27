/**
 * Servlet obsługujący rejestrację nowych użytkowników.
 * Tworzy konto, jeśli użytkownik o podanej nazwie jeszcze nie istnieje.
 */
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
    /**
     * Obsługuje żądanie POST z formularza rejestracji.
     * Tworzy nowego użytkownika lub zwraca błąd.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Pobiera dane z formularza rejestracji
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Inicjalizuje klasę zarządzającą użytkownikami
        UserStorage storage = new UserStorage();
        if (storage.register(username, password)) {
            // Próbuje zarejestrować użytkownika
            response.sendRedirect("login.jsp");
        } else {
            // Rejestracja się nie powiodła — prawdopodobnie użytkownik już istnieje
            request.setAttribute("error", "User already exists");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}

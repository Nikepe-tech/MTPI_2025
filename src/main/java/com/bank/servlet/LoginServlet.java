/**
 * Servlet obsługujący logowanie użytkownika.
 * Sprawdza dane logowania i zapisuje sesję w przypadku sukcesu.
 */
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
    /**
     * Obsługuje żądanie POST z formularza logowania.
     * Jeśli dane są poprawne, przekierowuje do strony głównej.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Pobiera dane z formularza logowania
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Próbuje zalogować użytkownika przez UserStorage
        UserStorage storage = new UserStorage();
        User user = storage.login(username, password);
        if (user != null) {
            // Ustawia użytkownika w sesji i przekierowuje do strony głównej
            request.getSession().setAttribute("user", user.getUsername());
            response.sendRedirect("home");
        } else {
            // Jeśli logowanie się nie powiodło — przekierowanie z komunikatem
            request.setAttribute("error", "Incorrect username or password");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}

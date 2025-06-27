/**
 * Servlet obsługujący stronę główną użytkownika.
 * Wczytuje dane konta i przekazuje je do widoku JSP.
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

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    /**
     * Obsługuje żądanie GET dla strony głównej.
     * Pobiera saldo i historię transakcji użytkownika.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Pobiera nazwę użytkownika z sesji
        String username = (String) request.getSession().getAttribute("user");
        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Pobiera dane użytkownika z systemu
        UserStorage storage = new UserStorage();
        User user = storage.getUser(username);

        // Przekazuje dane do widoku JSP
        request.setAttribute("balance", user.getBalance());
        request.setAttribute("history", user.getHistory());

        // Przekierowuje do strony głównej
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
}

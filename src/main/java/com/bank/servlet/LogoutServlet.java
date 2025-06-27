/**
 * Servlet obsługujący wylogowanie użytkownika.
 * Usuwa sesję i przekierowuje na stronę logowania.
 */
package com.bank.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    /**
     * Obsługuje żądanie GET wylogowania.
     * Inwaliduje sesję i kieruje na login.jsp.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getSession().invalidate();  // Usuwa sesję użytkownika
        response.sendRedirect("login.jsp"); // Przekierowanie na stronę logowania
    }
}

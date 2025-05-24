package com.bank.servlet;

import com.bank.model.User;
import com.bank.storage.UserStorage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deposit")
public class DepositServlet extends HttpServlet {
    // Обработка POST-запроса пополнения
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = (String) request.getSession().getAttribute("user");
        if (username == null) {
            // Если пользователь не залогинен — редирект на логин
            response.sendRedirect("login.jsp");
            return;
        }

        double amount = Double.parseDouble(request.getParameter("amount")); // Получаем сумму
        User user = UserStorage.getUser(username);  // Получаем пользователя из хранилища
        user.deposit(amount);                        // Пополняем баланс и записываем в историю
        UserStorage.saveUsers();                     // Сохраняем всех пользователей в файл

        response.sendRedirect("home");               // Возвращаем на домашнюю страницу
    }
}

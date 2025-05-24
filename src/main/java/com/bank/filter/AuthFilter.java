package com.bank.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    // Фильтр, который проверяет, авторизован ли пользователь.
    // Если нет — перенаправляет на login.jsp,
    // кроме страниц login и register.

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();

        // Проверяем, есть ли в сессии атрибут "user" — значит пользователь залогинен
        boolean loggedIn = request.getSession().getAttribute("user") != null;

        // Проверяем, что это страница входа или регистрации (чтобы туда пускать без авторизации)
        boolean isLoginOrRegister = uri.endsWith("login.jsp") || uri.endsWith("login")
                || uri.endsWith("register.jsp") || uri.endsWith("register");

        if (!loggedIn && !isLoginOrRegister) {
            // Если не залогинен и пытается открыть другую страницу — редирект на логин
            response.sendRedirect("login.jsp");
            return;
        }

        // Если все ок — передаем запрос дальше
        chain.doFilter(req, res);
    }
}


<!--
  Formularz logowania użytkownika do systemu.
  Po zalogowaniu przekierowuje do strony głównej.
-->

<%@ page contentType="text/html; charset=UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f4f8;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .form-box {
            background-color: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 10px #ccc;
            width: 300px;
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 8px;
            margin-top: 8px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        input[type="submit"] {
            width: 100%;
            padding: 10px;
            background-color: #0056b3;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #003f86;
        }

        .error {
            color: red;
            margin-bottom: 10px;
        }

        .link {
            text-align: center;
            margin-top: 10px;
        }

        .link a {
            color: #0056b3;
            text-decoration: none;
        }

        .link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="form-box">
    <h2>Login</h2>

    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
        <div class="error"><%= error %></div>
    <% } %>

    <form action="login" method="post">
        <label>Username:</label>
        <input type="text" name="username" required>
        <label>Password:</label>
        <input type="password" name="password" required>
        <input type="submit" value="Login">
    </form>

    <div class="link">
        Don't have an account? <a href="register.jsp">Register here</a>
    </div>
</div>
</body>
</html>

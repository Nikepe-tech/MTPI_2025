<!--
  Formularz do wpłaty środków na konto.
  Po zatwierdzeniu przekierowuje na stronę główną.
-->

<%@ page contentType="text/html; charset=UTF-8" %>

<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");

    String username = (String) session.getAttribute("user");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Deposit</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f5f7fa;
            margin: 0;
            padding: 0;
            color: #333;
        }
        .container {
            max-width: 500px;
            margin: 50px auto;
            background: white;
            padding: 30px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            border-radius: 8px;
        }
        h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #2c3e50;
        }
        form {
            display: flex;
            flex-direction: column;
        }
        label {
            margin-bottom: 8px;
            font-weight: 600;
        }
        input[type="number"],
        input[type="text"],
        input[type="password"] {
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }
        input[type="submit"] {
            background-color: #3498db;
            border: none;
            color: white;
            font-weight: 600;
            padding: 12px;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        input[type="submit"]:hover {
            background-color: #2980b9;
        }
        .links {
            text-align: center;
            margin-top: 15px;
        }
        a {
            color: #3498db;
            text-decoration: none;
            font-weight: 600;
        }
        a:hover {
            text-decoration: underline;
        }
        .error-message {
            color: #e74c3c;
            font-weight: 600;
            margin-top: -15px;
            margin-bottom: 15px;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Deposit Money</h2>
    <form action="deposit" method="post">
        <label for="amount">Amount</label>
        <input id="amount" name="amount" type="number" step="0.01" min="0" required />
        <input type="submit" value="Deposit" />
    </form>
    <div class="links">
        <a href="home">Back to Home</a>
    </div>
</div>
</body>
</html>

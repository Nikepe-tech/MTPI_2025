<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.bank.model.User, java.util.List" %>

<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");

    String username = (String) session.getAttribute("user");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    User user = com.bank.storage.UserStorage.getUser(username);
    double balance = user.getBalance();
    List<String> history = user.getHistory();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Bank Home</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 700px;
            margin: 30px auto;
            background: #f0f4f8;
            padding: 20px;
            border-radius: 8px;
        }
        h2 { color: #333; }
        a { margin-right: 15px; text-decoration: none; color: #0056b3; }
        a:hover { text-decoration: underline; }
        .balance { font-size: 1.2em; margin-bottom: 20px; }

        ul {
            background: #fff;
            padding: 15px;
            padding-left: 25px;
            border-radius: 5px;
            box-shadow: 0 0 5px #ccc;
            list-style-position: inside;
        }

        li { margin-bottom: 6px; }

        button {
            padding: 5px 10px;
            background-color: #0056b3;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background-color: #003f86;
        }

        .header-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 30px;
        }
    </style>
</head>
<body>

<h2>Welcome, <%= username %>!</h2>

<p class="balance"><strong>Balance:</strong> <%= balance %> PLN</p>

<nav>
    <a href="deposit.jsp">Deposit money</a>
    <a href="withdraw.jsp">Withdraw money</a>
    <a href="transfer.jsp">Transfer money</a>
    <a href="logout">Logout</a>
</nav>

<div class="header-row">
    <h3 style="margin: 0;">Transaction history:</h3>
    <form action="transactions.jsp" method="get" style="margin: 0;">
        <button type="submit">View full history</button>
    </form>
</div>

<ul>
<%
    if (history == null || history.isEmpty()) {
%>
    <li>No transactions yet.</li>
<%
    } else {
        for (String entry : history) {
%>
    <li><%= entry %></li>
<%
        }
    }
%>
</ul>

</body>
</html>

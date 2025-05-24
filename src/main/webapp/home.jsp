<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>

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
    <meta charset="UTF-8"/>
    <title>Bank Home</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 700px; margin: 30px auto; background: #f0f4f8; padding: 20px; border-radius: 8px; }
        h2 { color: #333; }
        a { margin-right: 15px; text-decoration: none; color: #0056b3; }
        a:hover { text-decoration: underline; }
        .balance { font-size: 1.2em; margin-bottom: 20px; }
        ul { background: #fff; padding: 15px; border-radius: 5px; box-shadow: 0 0 5px #ccc; }
        li { margin-bottom: 6px; }
    </style>
</head>
<body>

<h2>Welcome, <%= username %>!</h2>

<p class="balance"><strong>Balance:</strong> <%= request.getAttribute("balance") %> PLN</p>

<nav>
    <a href="deposit.jsp">Deposit money</a>
    <a href="withdraw.jsp">Withdraw money</a>
    <a href="transfer.jsp">Transfer money</a>
    <a href="logout">Logout</a>
</nav>

<h3>Transaction history:</h3>
<ul>
    <%
        List<String> history = (List<String>) request.getAttribute("history");
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

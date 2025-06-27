<!--
  Strona z pełną historią transakcji użytkownika.
  Pozwala filtrować dane według daty i typu operacji.
-->

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.bank.model.User, com.bank.model.Transaction, java.util.List, java.util.stream.Collectors" %>
<%@ page import="java.time.format.DateTimeFormatter, java.time.LocalDateTime" %>

<%
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
%>

<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");

    String username = (String) session.getAttribute("user");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    com.bank.storage.UserStorage storage = new com.bank.storage.UserStorage();
    User user = storage.getUser(username);

    List<Transaction> all = user.getTransactions();

    String from = request.getParameter("from");
    String to = request.getParameter("to");
    String type = request.getParameter("type");

    List<Transaction> filtered = all;

    if (from != null && to != null && !from.isEmpty() && !to.isEmpty()) {
        java.time.LocalDateTime fromDate = java.time.LocalDateTime.parse(from + "T00:00:00");
        java.time.LocalDateTime toDate = java.time.LocalDateTime.parse(to + "T23:59:59");

        filtered = filtered.stream()
            .filter(t -> !t.getTimestamp().isBefore(fromDate) && !t.getTimestamp().isAfter(toDate))
            .collect(Collectors.toList());
    }

    if (type != null && !type.isEmpty()) {
        filtered = filtered.stream()
            .filter(t -> t.getType().equalsIgnoreCase(type))
            .collect(Collectors.toList());
    }

    filtered.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Transaction History</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 30px auto;
            background: #f0f4f8;
            padding: 20px;
            border-radius: 8px;
        }

        h2 {
            color: #333;
        }

        form {
            margin-bottom: 20px;
            display: flex;
            gap: 10px;
            align-items: center;
            flex-wrap: wrap;
        }

        input[type="date"], select {
            padding: 5px;
            border-radius: 4px;
            border: 1px solid #ccc;
        }

        input[type="submit"] {
            padding: 6px 12px;
            background-color: #0056b3;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #003f86;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: #fff;
            box-shadow: 0 0 5px #ccc;
            border-radius: 5px;
            overflow: hidden;
        }

        th, td {
            padding: 10px;
            border-bottom: 1px solid #ccc;
            text-align: left;
        }

        th {
            background-color: #f0f4f8;
            font-weight: bold;
        }

        tr:last-child td {
            border-bottom: none;
        }

        .back-link {
            display: inline-block;
            margin-top: 20px;
            text-decoration: none;
            color: #0056b3;
        }

        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<h2>Transaction History</h2>

<form method="get">
    <label>From:</label>
    <input type="date" name="from">
    <label>To:</label>
    <input type="date" name="to">
    <label>Type:</label>
    <select name="type">
        <option value="">All</option>
        <option value="Deposit">Deposit</option>
        <option value="Withdrawal">Withdrawal</option>
        <option value="TransferOut">Transfer Sent</option>
        <option value="TransferIn">Transfer Received</option>
    </select>

    <input type="submit" value="Filter">
</form>

<table>
    <tr>
        <th>Date</th>
        <th>Type</th>
        <th>Amount</th>
        <th>Target</th>
    </tr>
    <%
        for (Transaction t : filtered) {
    %>
    <tr>
        <td><%= t.getTimestamp().format(formatter) %></td>
        <td><%= t.getType() %></td>
        <td><%= t.getAmount() %></td>
        <td><%= t.getTargetUser() != null ? t.getTargetUser() : "-" %></td>
    </tr>
    <%
        }
    %>
</table>

<a class="back-link" href="home.jsp">← Back to Home</a>

</body>
</html>

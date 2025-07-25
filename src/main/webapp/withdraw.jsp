<!--
  Formularz do wypłaty środków z konta użytkownika.
  Sprawdza saldo i wykonuje operację.
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
  <meta charset="UTF-8"/>
  <title>Withdraw Money</title>
  <style>
    body { font-family: Arial, sans-serif; max-width: 400px; margin: 40px auto; background: #f0f4f8; padding: 20px; border-radius: 8px; }
    h2 { color: #333; margin-bottom: 20px; }
    form { display: flex; flex-direction: column; }
    input { margin-bottom: 15px; padding: 8px; font-size: 1em; border: 1px solid #ccc; border-radius: 4px; }
    input[type="submit"] { background: #dc3545; border: none; color: white; cursor: pointer; }
    input[type="submit"]:hover { background: #b02a37; }
    .error { color: red; min-height: 20px; margin-bottom: 20px; }
    a { color: #0056b3; text-decoration: none; }
    a:hover { text-decoration: underline; }
  </style>
</head>
<body>

<h2>Withdraw Money</h2>
<form action="withdraw" method="post" autocomplete="off">
  <input type="number" name="amount" step="0.01" min="0.01" placeholder="Amount" required/>
  <input type="submit" value="Withdraw"/>
</form>

<div class="error">
  <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
</div>

<p><a href="home">Back to Home</a></p>

</body>
</html>

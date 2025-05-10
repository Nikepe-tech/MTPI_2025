<%
    String username = (String) session.getAttribute("user");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<p>Witaj, <%= username %>!</p>
<p>Saldo: <%= request.getAttribute("balance") %> zł</p>
<a href="deposit.jsp">Doładuj konto</a>

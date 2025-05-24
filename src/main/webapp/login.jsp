<%@ page contentType="text/html; charset=UTF-8" %>

<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Login</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 400px; margin: 50px auto; background: #f0f4f8; padding: 20px; border-radius: 8px; }
        h2 { color: #333; }
        form { display: flex; flex-direction: column; }
        input { margin: 10px 0; padding: 8px; font-size: 1em; border: 1px solid #ccc; border-radius: 4px; }
        input[type="submit"] { background: #007bff; color: white; border: none; cursor: pointer; }
        input[type="submit"]:hover { background: #0056b3; }
        .error { color: red; min-height: 20px; margin-bottom: 15px; }
        a { color: #0056b3; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>

<h2>Login</h2>
<form action="login" method="post" autocomplete="off">
    <input type="text" name="username" placeholder="Username" required autocomplete="username"/>
    <input type="password" name="password" placeholder="Password" required autocomplete="current-password"/>
    <input type="submit" value="Login"/>
</form>

<div class="error">
    <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
</div>

<p>Don't have an account? <a href="register.jsp">Register here</a></p>

</body>
</html>

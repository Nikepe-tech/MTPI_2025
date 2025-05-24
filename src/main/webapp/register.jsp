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
    <title>Register</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 400px; margin: 50px auto; background: #f0f4f8; padding: 20px; border-radius: 8px; }
        h2 { color: #333; }
        form { display: flex; flex-direction: column; }
        input { margin: 10px 0; padding: 8px; font-size: 1em; border: 1px solid #ccc; border-radius: 4px; }
        input[type="submit"] { background: #28a745; color: white; border: none; cursor: pointer; }
        input[type="submit"]:hover { background: #1e7e34; }
        .error { color: red; min-height: 20px; margin-bottom: 15px; }
        a { color: #0056b3; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>

<h2>Register</h2>
<form action="register" method="post" autocomplete="off">
    <input type="text" name="username" placeholder="Choose username" required autocomplete="username"/>
    <input type="password" name="password" placeholder="Choose password" required autocomplete="new-password"/>
    <input type="submit" value="Register"/>
</form>

<div class="error">
    <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
</div>

<p>Already have an account? <a href="login.jsp">Login here</a></p>

</body>
</html>

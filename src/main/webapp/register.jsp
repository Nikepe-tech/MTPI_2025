<form action="register" method="post">
    Login: <input name="username" /><br/>
    Hasło: <input type="password" name="password" /><br/>
    <input type="submit" value="Zarejestruj" />
    <p style="color:red;">
        <%= request.getAttribute("error") %>
    </p>
</form>
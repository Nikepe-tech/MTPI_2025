<form action="login" method="post">
    Login: <input name="username" /><br/>
    Hasło: <input type="password" name="password" /><br/>
    <input type="submit" value="Zaloguj" />
    <p style="color:red;">
        <%= request.getAttribute("error") %>
    </p>
</form>

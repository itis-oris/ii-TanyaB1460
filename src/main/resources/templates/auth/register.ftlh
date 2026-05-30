<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Регистрация — Jewelry Shop</title>
    <link rel="stylesheet" href="/css/style.css"/>
</head>
<body class="auth-body">
<div class="auth-box">
    <h1>Регистрация</h1>

    <#if error??>
        <div class="msg-err">${error}</div>
    </#if>

    <form action="/register" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <label>Имя пользователя</label>
        <input type="text" name="username" value="${(form.username)!''}"
               placeholder="Имя пользователя" required/>

        <label>Email</label>
        <input type="email" name="email" value="${(form.email)!''}"
               placeholder="email@example.com" required/>

        <label>Пароль</label>
        <input type="password" name="password" placeholder="Пароль" required/>

        <label>Подтвердите пароль</label>
        <input type="password" name="confirmPassword" placeholder="Повторите пароль" required/>

        <label>Роль</label>
        <select name="role">
            <option value="CUSTOMER">Покупатель</option>
            <option value="MAKER">Мастер</option>
        </select>

        <br/>
        <button type="submit" class="btn btn-primary btn-full">Зарегистрироваться</button>
    </form>

    <p>Уже есть аккаунт? <a href="/login">Войти</a></p>
</div>
<script src="/js/main.js"></script>
</body>
</html>
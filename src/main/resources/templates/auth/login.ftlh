<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Вход — Jewelry Shop</title>
    <link rel="stylesheet" href="/css/style.css"/>
</head>
<body class="auth-body">
<div class="auth-box">
    <h1>Jewelry Shop</h1>

    <#if logout??>
        <div class="msg-ok">Вы вышли из аккаунта</div>
    </#if>

    <#if error??>
        <div class="msg-err">${error}</div>
    </#if>

    <form action="/login" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <label>Email</label>
        <input type="email" name="username" placeholder="email@example.com" required/>

        <label>Пароль</label>
        <input type="password" name="password" placeholder="Пароль" required/>

        <button type="submit" class="btn btn-primary btn-full">Войти</button>
    </form>

    <p>Нет аккаунта? <a href="/register">Зарегистрироваться</a></p>
</div>
<script src="/js/main.js"></script>
</body>
</html>
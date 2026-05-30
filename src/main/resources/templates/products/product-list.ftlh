<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Мои товары — Jewelry Shop</title>
    <link rel="stylesheet" href="/css/style.css"/>
</head>
<body>
<nav>
    <a href="/catalog">Jewelry Shop</a>
    <a href="/catalog">Каталог</a>
    <#if isMaker>
        <a href="/products/my">Мои товары</a>
        <a href="/products/new">+ Добавить</a>
    </#if>
    <#if isAuthenticated>
        <a href="/cart">Корзина</a>
        <form action="/logout" method="post" style="display:inline">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit">Выйти</button>
        </form>
    <#else>
        <a href="/login">Войти</a>
        <a href="/register">Регистрация</a>
    </#if>
</nav>

<div class="container">
    <h1>Мои товары &nbsp;<a href="/products/new" class="btn btn-primary">+ Добавить</a></h1>

    <#if success??>
        <div class="msg-ok">${success}</div>
    </#if>

    <#if products?has_content>
        <table>
            <thead>
            <tr><th>Название</th><th>Категория</th><th>Цена (₽)</th><th>Действия</th></tr>
            </thead>
            <tbody>
            <#list products as product>
                <tr>
                    <td><a href="/products/${product.id}">${product.title}</a></td>
                    <td>${(product.category.name)!'—'}</td>
                    <td>${product.price?string["0.00"]}</td>
                    <td>
                        <a href="/products/${product.id}/edit" class="btn btn-sm">Изменить</a>
                        <button class="btn btn-danger btn-sm btn-delete"
                                data-product-id="${product.id}"
                                data-product-title="${product.title}">Удалить</button>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    <#else>
        <p>Товаров нет.</p>
    </#if>
</div>

<form id="deleteForm" method="post" style="display:none;">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>

<script src="/js/main.js"></script>
</body>
</html>

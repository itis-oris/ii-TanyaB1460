<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Корзина — Jewelry Shop</title>
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
    <h1>Корзина</h1>

    <#if success??>
        <div class="msg-ok">${success}</div>
    </#if>
    <#if error??>
        <div class="msg-err">${error}</div>
    </#if>

    <#if cart?has_content>
        <table id="cartTable">
            <thead>
            <tr><th>Товар</th><th>Цена (₽)</th><th>Кол-во</th><th>Сумма (₽)</th><th></th></tr>
            </thead>
            <tbody>
            <#list cart as item>
                <tr id="row-${item.id}">
                    <td><a href="/products/${item.product.id}">${item.product.title}</a></td>
                    <td>${item.product.price?string["0.00"]}</td>
                    <td>${item.quantity}</td>
                    <td>${item.totalPrice?string["0.00"]}</td>
                    <td><button class="btn btn-danger btn-sm btn-remove-cart"
                                data-order-id="${item.id}">Убрать</button></td>
                </tr>
            </#list>
            </tbody>
        </table>

        <p class="cart-total">Итого: ₽ <span id="cartTotal">${total?string["0.00"]}</span></p>

        <form action="/cart/checkout" method="post" onsubmit="return confirm('Оформить заказ?')">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary">Оформить заказ</button>
        </form>
    <#else>
        <p>Корзина пуста. <a href="/catalog">Перейти в каталог</a></p>
    </#if>

    <hr/>
    <h2>История заказов</h2>

    <#if orders?has_content>
        <table>
            <thead>
            <tr><th>Товар</th><th>Кол-во</th><th>Сумма (₽)</th><th>Статус</th><th>Дата</th></tr>
            </thead>
            <tbody>
            <#list orders as order>
                <tr>
                    <td><a href="/products/${order.product.id}">${order.product.title}</a></td>
                    <td>${order.quantity}</td>
                    <td>${order.totalPrice?string["0.00"]}</td>
                    <td>${order.status}</td>
                    <td>${order.createdAtFormatted}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    <#else>
        <p>Заказов нет.</p>
    </#if>
</div>

<div id="toast" class="toast"></div>
<script src="/js/main.js"></script>
</body>
</html>

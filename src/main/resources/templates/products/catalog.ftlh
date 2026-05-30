<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Каталог — Jewelry Shop</title>
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
    <h1>Каталог украшений</h1>

    <form class="filter" method="get" action="/catalog">
        <select name="categoryId">
            <option value="">Все категории</option>
            <#list categories as cat>
                <option value="${cat.id}" <#if selectedCategory?? && cat.id == selectedCategory>selected</#if>>${cat.name}</option>
            </#list>
        </select>
        <input type="number" name="minPrice" placeholder="Цена от" value="${minPrice!''}" min="0"/>
        <input type="number" name="maxPrice" placeholder="Цена до" value="${maxPrice!''}" min="0"/>
        <select name="currency">
            <option value="RUB" <#if currency == 'RUB'>selected</#if>>RUB</option>
            <option value="USD" <#if currency == 'USD'>selected</#if>>USD</option>
            <option value="EUR" <#if currency == 'EUR'>selected</#if>>EUR</option>
        </select>
        <button type="submit" class="btn btn-primary">Найти</button>
        <a href="/catalog" class="btn">Сбросить</a>
    </form>

    <#if products?has_content>
        <div class="grid">
            <#list products as product>
                <div class="card">
                    <p class="card-cat">${(product.category.name)!''}</p>
                    <h3><a href="/products/${product.id}">${product.title}</a></h3>
                    <p class="card-desc">${product.description?truncate(80)}</p>
                    <p class="card-price">
                        <#if currency == 'USD'>$ <#elseif currency == 'EUR'>€ <#else>₽ </#if>${(product.price * rate)?string["0.00"]}
                    </p>
                    <a href="/products/${product.id}" class="btn">Подробнее</a>
                    <#if isAuthenticated>
                        <button class="btn btn-primary btn-add-to-cart"
                                data-product-id="${product.id}"
                                data-product-title="${product.title}">В корзину</button>
                    </#if>
                </div>
            </#list>
        </div>
    <#else>
        <p>Товары не найдены.</p>
    </#if>
</div>

<div id="toast" class="toast"></div>
<script src="/js/main.js"></script>
</body>
</html>
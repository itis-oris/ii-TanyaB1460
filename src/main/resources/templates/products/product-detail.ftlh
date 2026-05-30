<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>${product.title} — Jewelry Shop</title>
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

    <#if success??>
        <div class="msg-ok">${success}</div>
    </#if>

    <#if reviewError??>
        <div class="msg-err">${reviewError}</div>
    </#if>

    <h1>${product.title}</h1>
    <p class="card-cat">${(product.category.name)!''}</p>
    <p>${product.description}</p>
    <p><strong>Цена: ₽ ${product.price?string["0.00"]}</strong></p>

    <#if isAuthenticated>
        <button class="btn btn-primary btn-add-to-cart"
                data-product-id="${product.id}"
                data-product-title="${product.title}">
            В корзину
        </button>
    </#if>

    <a href="/catalog" class="btn">← Назад в каталог</a>

    <hr/>

    <h2>Отзывы</h2>

    <#if reviews?has_content>
        <#list reviews as review>
            <div class="review">
                <div class="review-meta">
                    <strong>${review.author.username}</strong>
                    <#if review.createdAtFormatted?? && review.createdAtFormatted?has_content>
                        <span>${review.createdAtFormatted}</span>
                    </#if>
                </div>
                <p>${review.content}</p>
            </div>
        </#list>
    <#else>
        <p>Отзывов пока нет.</p>
    </#if>

    <#if isAuthenticated>
        <h3>Оставить отзыв</h3>
        <form action="/products/${product.id}/reviews" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <label for="content">Текст отзыва</label>
            <textarea id="content"
                      name="content"
                      rows="4"
                      placeholder="Ваш отзыв..."
                      required>${(reviewForm.content)!''}</textarea>

            <br/>
            <button type="submit" class="btn btn-primary">Отправить</button>
        </form>
    </#if>

</div>

<div id="toast" class="toast"></div>
<script src="/js/main.js"></script>
</body>
</html>
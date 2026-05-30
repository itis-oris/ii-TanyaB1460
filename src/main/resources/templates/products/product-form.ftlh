<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title><#if isEdit>Редактировать товар<#else>Новый товар</#if> — Jewelry Shop</title>
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
    <div class="form-wrap">
        <h1><#if isEdit>Редактировать товар<#else>Добавить товар</#if></h1>


        <form action="<#if isEdit>/products/${productId}/edit<#else>/products/new</#if>" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <label>Название</label>
            <input type="text" name="title" value="${(form.title)!''}" placeholder="Название украшения"/>
            <#if fieldErrors?? && fieldErrors.title??>
                <span class="field-err">${fieldErrors.title}</span>
            </#if>

            <label>Описание</label>
            <textarea name="description" rows="4" placeholder="Описание...">${(form.description)!''}</textarea>

            <label>Цена (₽)</label>
            <input type="number" name="price" value="${(form.price)!''}" step="0.01" min="0.01" placeholder="0.00"/>
            <#if fieldErrors?? && fieldErrors.price??>
                <span class="field-err">${fieldErrors.price}</span>
            </#if>

            <label>Категория</label>
            <select name="categoryId">
                <option value="">Выберите категорию</option>
                <#list categories as cat>
                    <option value="${cat.id}"
                            <#if form.categoryId?? && form.categoryId == cat.id>selected</#if>
                    >${cat.name}</option>
                </#list>
            </select>

            <label>Теги (через запятую)</label>
            <input type="text" name="tags" value="${(form.tags)!''}" placeholder="золото, серебро"/>

            <br/>
            <button type="submit" class="btn btn-primary">
                <#if isEdit>Сохранить<#else>Создать</#if>
            </button>
            <a href="/products/my" class="btn">Отмена</a>
        </form>
    </div>
</div>

<script src="/js/main.js"></script>
</body>
</html>

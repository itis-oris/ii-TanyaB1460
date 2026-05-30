const $ = (selector) => document.querySelector(selector);
const $$ = (selector) => document.querySelectorAll(selector);

const csrf = {
    token: $('meta[name="_csrf"]')?.content,
    header: $('meta[name="_csrf_header"]')?.content
};

function showToast(message, isError = false) {
    const toast = $('#toast');
    if (!toast) return;

    toast.textContent = message;
    toast.className = isError ? 'toast error' : 'toast';
    toast.style.display = 'block';

    setTimeout(() => {
        toast.style.display = 'none';
    }, 3000);
}

function postForm(url, data) {
    const headers = {
        'Content-Type': 'application/x-www-form-urlencoded'
    };

    if (csrf.header && csrf.token) {
        headers[csrf.header] = csrf.token;
    }

    return fetch(url, {
        method: 'POST',
        headers,
        body: new URLSearchParams(data)
    }).then(res => res.json());
}

document.addEventListener('DOMContentLoaded', () => {
    $$('.btn-add-to-cart').forEach(btn => {
        btn.addEventListener('click', () => {
            postForm('/cart/add', {
                productId: btn.dataset.productId,
                quantity: 1
            })
                .then(data => showToast(
                    data.success ? 'Товар добавлен в корзину' : data.message,
                    !data.success
                ))
                .catch(() => showToast('Ошибка соединения', true));
        });
    });

    $$('.btn-remove-cart').forEach(btn => {
        btn.addEventListener('click', () => {
            if (!confirm('Удалить из корзины?')) return;

            const orderId = btn.dataset.orderId;

            postForm('/cart/remove', { orderId })
                .then(data => {
                    if (!data.success) {
                        showToast(data.message, true);
                        return;
                    }

                    document.getElementById(`row-${orderId}`)?.remove();
                    showToast('Удалено');
                })
                .catch(() => showToast('Ошибка соединения', true));
        });
    });

    $$('.btn-delete').forEach(btn => {
        btn.addEventListener('click', () => {
            if (!confirm('Удалить товар?')) return;

            const form = $('#deleteForm');
            if (!form) return;

            form.action = `/products/${btn.dataset.productId}/delete`;
            form.submit();
        });
    });
});
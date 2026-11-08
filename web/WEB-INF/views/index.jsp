<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Coffee Shop</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
    <h1>&#9749; The Corner Coffee Shop</h1>
    <p class="tagline">Pick your coffee, we'll have it ready in a minute.</p>

    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <div class="menu-grid">
        <c:forEach var="coffee" items="${coffees}">
            <form class="coffee-card" data-type="${coffee.type}" action="${pageContext.request.contextPath}/coffeeshop" method="post">
                <svg class="cup" viewBox="0 0 64 64" xmlns="http://www.w3.org/2000/svg">
                    <path d="M14 22h36l-3 26a8 8 0 0 1-8 7H25a8 8 0 0 1-8-7l-3-26z" fill="#fff" style="stroke:var(--coffee-dark);stroke-width:2"/>
                    <rect class="cup-liquid" x="18" y="26" width="28" height="10" rx="2"/>
                    <path d="M50 26c6 0 10 4 10 9s-4 9-10 9" fill="none" style="stroke:var(--coffee-dark);stroke-width:2"/>
                    <path d="M20 16c1-3 4-3 5 0M30 16c1-3 4-3 5 0M40 16c1-3 4-3 5 0" style="stroke:var(--coffee-mid);stroke-width:2" fill="none" stroke-linecap="round"/>
                </svg>
                <h3>${coffee.type}</h3>
                <p class="price">$${coffee.price}</p>
                <input type="hidden" name="coffeeType" value="${coffee.type}">
                <button type="submit" class="btn btn-small">Order</button>
            </form>
        </c:forEach>
    </div>

    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/coffeeshop/orders">View Orders</a>
        <a href="${pageContext.request.contextPath}/barista/login">Barista login</a>
    </div>
</div>

<c:if test="${not empty sessionScope.chatSessionId}">
    <a class="chat-fab" href="${pageContext.request.contextPath}/chat" title="Chat about your order">
        <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path d="M4 4h16v12H7l-3 3V4z"/>
        </svg>
    </a>
</c:if>
</body>
</html>

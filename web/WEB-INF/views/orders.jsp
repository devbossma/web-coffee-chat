<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Orders</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
    <h1>Pickup Board</h1>
    <p class="tagline">Every order, live.</p>

    <table class="board">
        <tr>
            <th>Order ID</th>
            <th>Customer ID</th>
            <th>Coffee</th>
            <th>Status</th>
        </tr>
        <c:forEach var="order" items="${orders}">
            <tr>
                <td>${order.id}</td>
                <td>${order.customerId}</td>
                <td>${order.coffeeType}</td>
                <td><span class="status status-${order.status}">${order.status}</span></td>
            </tr>
        </c:forEach>
    </table>

    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/coffeeshop">Back to Coffee Shop</a>
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

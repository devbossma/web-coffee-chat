<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chat with the barista</title>
    <meta http-equiv="refresh" content="5">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
    <h1>Order #${order.id}: ${order.coffeeType}</h1>
    <p class="tagline">Status: <span class="status status-${order.status}">${order.status}</span></p>

    <div class="chat-card">
        <div class="transcript">
            <c:forEach var="message" items="${messages}">
                <div class="bubble ${message.sender}">
                    <span class="who">${message.sender}</span>${message.text}
                </div>
            </c:forEach>
        </div>

        <form class="chat-form" action="${pageContext.request.contextPath}/chat" method="post">
            <input type="text" name="text" placeholder="Type a message..." autofocus>
            <button type="submit" class="btn">Send</button>
        </form>
    </div>

    <p class="tagline"><small>This page refreshes every few seconds. It will take you back to the
        menu on its own once your order is ready and handed over.</small></p>
</div>
</body>
</html>

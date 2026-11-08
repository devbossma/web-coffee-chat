<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chat with the barista</title>
    <meta http-equiv="refresh" content="5">
    <style>
        body { font-family: sans-serif; max-width: 480px; margin: 40px auto; }
        .transcript { border: 1px solid #ddd; padding: 10px; height: 240px; overflow-y: auto; }
        .CUSTOMER { text-align: right; color: #0a5; }
        .BARISTA { text-align: left; color: #333; }
    </style>
</head>
<body>
<h1>Order #${order.id}: ${order.coffeeType}</h1>
<p>Status: <strong>${order.status}</strong></p>

<div class="transcript">
    <c:forEach var="message" items="${messages}">
        <p class="${message.sender}"><strong>${message.sender}:</strong> ${message.text}</p>
    </c:forEach>
</div>

<form action="${pageContext.request.contextPath}/chat" method="post">
    <input type="text" name="text" placeholder="Type a message..." autofocus style="width: 70%;">
    <input type="submit" value="Send">
</form>

<p><small>This page refreshes every few seconds. It will take you back to the menu on its own
    once your order is ready and handed over.</small></p>
</body>
</html>

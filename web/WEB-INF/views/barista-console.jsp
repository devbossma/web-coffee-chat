<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Barista console</title>
    <meta http-equiv="refresh" content="5">
    <style>
        body { font-family: sans-serif; max-width: 720px; margin: 40px auto; display: flex; gap: 24px; }
        .sessions { width: 240px; }
        .sessions a { display: block; padding: 6px; text-decoration: none; }
        .sessions a.active { font-weight: bold; }
        .transcript { border: 1px solid #ddd; padding: 10px; height: 240px; overflow-y: auto; flex: 1; }
        .CUSTOMER { text-align: left; color: #0a5; }
        .BARISTA { text-align: right; color: #333; }
    </style>
</head>
<body>
<div class="sessions">
    <h2>Active chats</h2>
    <c:forEach var="session" items="${sessions}">
        <a href="${pageContext.request.contextPath}/barista/console?sessionId=${session.id}"
           class="${selectedSession != null && selectedSession.id == session.id ? 'active' : ''}">
            #${session.id} - ${session.customerId} (order ${session.orderId})
        </a>
    </c:forEach>
    <p><a href="${pageContext.request.contextPath}/barista/orders">Manage orders</a></p>
</div>

<div style="flex:1;">
    <c:if test="${selectedSession != null}">
        <h2>Chat with ${selectedSession.customerId}</h2>
        <div class="transcript">
            <c:forEach var="message" items="${messages}">
                <p class="${message.sender}"><strong>${message.sender}:</strong> ${message.text}</p>
            </c:forEach>
        </div>
        <form action="${pageContext.request.contextPath}/barista/console" method="post">
            <input type="hidden" name="sessionId" value="${selectedSession.id}">
            <input type="text" name="text" placeholder="Reply..." style="width: 70%;">
            <input type="submit" value="Send">
        </form>
    </c:if>
    <c:if test="${selectedSession == null}">
        <p>Pick a chat on the left.</p>
    </c:if>
</div>
</body>
</html>

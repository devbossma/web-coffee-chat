<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Barista console</title>
    <meta http-equiv="refresh" content="5">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
    <h1>Barista console</h1>

    <div class="console">
        <div class="session-list">
            <c:forEach var="session" items="${sessions}">
                <a href="${pageContext.request.contextPath}/barista/console?sessionId=${session.id}"
                   class="${selectedSession != null && selectedSession.id == session.id ? 'active' : ''}">
                    #${session.id} &middot; ${session.customerId}<br>order ${session.orderId}
                </a>
            </c:forEach>
            <c:if test="${empty sessions}">
                <p class="tagline">No active chats right now.</p>
            </c:if>
        </div>

        <div class="console-chat">
            <c:if test="${selectedSession != null}">
                <div class="chat-card">
                    <h3>${selectedSession.customerId}</h3>
                    <div class="transcript">
                        <c:forEach var="message" items="${messages}">
                            <div class="bubble ${message.sender}">
                                <span class="who">${message.sender}</span>${message.text}
                            </div>
                        </c:forEach>
                    </div>
                    <form class="chat-form" action="${pageContext.request.contextPath}/barista/console" method="post">
                        <input type="hidden" name="sessionId" value="${selectedSession.id}">
                        <input type="text" name="text" placeholder="Reply...">
                        <button type="submit" class="btn">Send</button>
                    </form>
                </div>
            </c:if>
            <c:if test="${selectedSession == null}">
                <p class="tagline">Pick a chat on the left.</p>
            </c:if>
        </div>
    </div>

    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/barista/orders">Manage orders</a>
    </div>
</div>
</body>
</html>

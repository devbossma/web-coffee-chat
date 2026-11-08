<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage orders</title>
    <meta http-equiv="refresh" content="5">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
    <h1>Manage orders</h1>

    <table class="board">
        <tr>
            <th>Order ID</th>
            <th>Customer ID</th>
            <th>Coffee</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <c:forEach var="order" items="${orders}">
            <tr>
                <td>${order.id}</td>
                <td>${order.customerId}</td>
                <td>${order.coffeeType}</td>
                <td><span class="status status-${order.status}">${order.status}</span></td>
                <td>
                    <c:if test="${order.status == 'PLACED'}">
                        <form action="${pageContext.request.contextPath}/barista/orders" method="post">
                            <input type="hidden" name="orderId" value="${order.id}">
                            <input type="hidden" name="action" value="markPaid">
                            <button type="submit" class="btn btn-small">Mark paid</button>
                        </form>
                    </c:if>
                    <c:if test="${order.status == 'PAID'}">
                        <form action="${pageContext.request.contextPath}/barista/orders" method="post">
                            <input type="hidden" name="orderId" value="${order.id}">
                            <input type="hidden" name="action" value="markFulfilled">
                            <button type="submit" class="btn btn-small btn-secondary">Mark fulfilled</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>

    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/barista/console">Back to chat console</a>
        <a href="${pageContext.request.contextPath}/barista/logout">Log out</a>
    </div>
</div>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage orders</title>
    <meta http-equiv="refresh" content="5">
    <style>
        body { font-family: sans-serif; max-width: 640px; margin: 40px auto; }
        table { width: 100%; border-collapse: collapse; }
        th, td { text-align: left; padding: 6px; border-bottom: 1px solid #ddd; }
        form { display: inline; }
    </style>
</head>
<body>
<h1>Manage orders</h1>
<table>
    <tr>
        <th>Order ID</th>
        <th>Customer ID</th>
        <th>Coffee Type</th>
        <th>Status</th>
        <th>Action</th>
    </tr>
    <c:forEach var="order" items="${orders}">
        <tr>
            <td>${order.id}</td>
            <td>${order.customerId}</td>
            <td>${order.coffeeType}</td>
            <td>${order.status}</td>
            <td>
                <c:if test="${order.status == 'PLACED'}">
                    <form action="${pageContext.request.contextPath}/barista/orders" method="post">
                        <input type="hidden" name="orderId" value="${order.id}">
                        <input type="hidden" name="action" value="markPaid">
                        <input type="submit" value="Mark paid">
                    </form>
                </c:if>
                <c:if test="${order.status == 'PAID'}">
                    <form action="${pageContext.request.contextPath}/barista/orders" method="post">
                        <input type="hidden" name="orderId" value="${order.id}">
                        <input type="hidden" name="action" value="markFulfilled">
                        <input type="submit" value="Mark fulfilled">
                    </form>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>
<p><a href="${pageContext.request.contextPath}/barista/console">Back to chat console</a></p>
</body>
</html>

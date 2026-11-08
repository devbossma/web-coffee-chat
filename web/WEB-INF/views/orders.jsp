<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Orders</title>
    <style>
        body { font-family: sans-serif; max-width: 560px; margin: 40px auto; }
        table { width: 100%; border-collapse: collapse; }
        th, td { text-align: left; padding: 6px; border-bottom: 1px solid #ddd; }
    </style>
</head>
<body>
<h1>Current Orders</h1>
<table>
    <tr>
        <th>Order ID</th>
        <th>Customer ID</th>
        <th>Coffee Type</th>
        <th>Status</th>
    </tr>
    <c:forEach var="order" items="${orders}">
        <tr>
            <td>${order.id}</td>
            <td>${order.customerId}</td>
            <td>${order.coffeeType}</td>
            <td>${order.status}</td>
        </tr>
    </c:forEach>
</table>
<p><a href="${pageContext.request.contextPath}/coffeeshop">Back to Coffee Shop</a></p>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Coffee Shop</title>
    <style>
        body { font-family: sans-serif; max-width: 480px; margin: 40px auto; }
        .error { color: #b00020; }
        select, input[type=submit] { font-size: 1rem; padding: 6px; }
    </style>
</head>
<body>
<h1>Welcome to the Coffee Shop</h1>

<c:if test="${not empty error}">
    <p class="error">${error}</p>
</c:if>

<form action="${pageContext.request.contextPath}/coffeeshop" method="post">
    <label for="coffeeType">Choose your coffee:</label><br/>
    <select name="coffeeType" id="coffeeType">
        <c:forEach var="coffee" items="${coffees}">
            <option value="${coffee.type}">${coffee.type} - $${coffee.price}</option>
        </c:forEach>
    </select>
    <input type="submit" value="Order">
</form>

<p><a href="${pageContext.request.contextPath}/coffeeshop/orders">View Orders</a></p>
<p><a href="${pageContext.request.contextPath}/barista/login">Barista login</a></p>
</body>
</html>

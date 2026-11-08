<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Barista login</title>
    <style>
        body { font-family: sans-serif; max-width: 360px; margin: 80px auto; }
        .error { color: #b00020; }
        input[type=text], input[type=password] { width: 100%; padding: 6px; margin-bottom: 8px; }
    </style>
</head>
<body>
<h1>Barista login</h1>

<c:if test="${not empty error}">
    <p class="error">${error}</p>
</c:if>

<form action="${pageContext.request.contextPath}/barista/login" method="post">
    <label for="username">Username</label>
    <input type="text" id="username" name="username">
    <label for="password">Password</label>
    <input type="password" id="password" name="password">
    <input type="submit" value="Log in">
</form>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Login - Fashion Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
    <div class="auth-card">
        <h2>Admin Login</h2>

        <c:if test="${param.error == 'invalid'}">
            <div class="message error">Invalid username or password.</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/login" method="post">
            <input type="text" name="username" placeholder="Username" required>
            <input type="password" name="password" placeholder="Password" required>
            <button type="submit" class="btn">Login</button>
        </form>

        <p class="link"><a href="${pageContext.request.contextPath}/home">Back to Store</a></p>
    </div>
</body>
</html>

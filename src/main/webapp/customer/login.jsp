<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Fashion Store - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
    <div class="auth-card">
        <h2>Login</h2>

        <c:if test="${param.success == 'registered'}">
            <div class="message success">Registration successful. Please log in.</div>
        </c:if>
        <c:if test="${param.error == 'invalid'}">
            <div class="message error">Invalid email or password.</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <input type="email" name="email" placeholder="Email" required>
            <input type="password" name="password" placeholder="Password" required>
            <button type="submit" class="btn">Login</button>
        </form>

        <p class="link">Don't have an account? <a href="${pageContext.request.contextPath}/customer/register.jsp">Register</a></p>
        <p class="link"><a href="${pageContext.request.contextPath}/home">Back to Home</a></p>
    </div>
</body>
</html>

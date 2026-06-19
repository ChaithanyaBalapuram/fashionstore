<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Fashion Store - Register</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
    <div class="auth-card">
        <h2>Create Account</h2>

        <c:if test="${param.error == 'email'}">
            <div class="message error">This email is already registered.</div>
        </c:if>
        <c:if test="${param.error == 'password'}">
            <div class="message error">Password must be at least 6 characters.</div>
        </c:if>
        <c:if test="${param.error == 'failed'}">
            <div class="message error">Registration failed. Please try again.</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <input type="text" name="firstName" placeholder="First Name" required>
            <input type="text" name="lastName" placeholder="Last Name" required>
            <input type="email" name="email" placeholder="Email" required>
            <input type="text" name="phone" placeholder="Phone Number" required>
            <input type="password" name="password" placeholder="Password" required>
            <textarea name="address" placeholder="Address"></textarea>
            <button type="submit" class="btn">Register</button>
        </form>

        <p class="link">Already have an account? <a href="${pageContext.request.contextPath}/customer/login.jsp">Login</a></p>
        <p class="link"><a href="${pageContext.request.contextPath}/home">Back to Home</a></p>
    </div>
</body>
</html>

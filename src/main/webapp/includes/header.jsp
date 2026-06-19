<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fashion Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="site-header">
    <div class="header-inner">
        <a class="logo" href="${pageContext.request.contextPath}/home">Fashion Store</a>
        <nav class="main-nav">
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <a href="${pageContext.request.contextPath}/products">Shop</a>
            <c:if test="${not empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/orders">My Orders</a>
                <a href="${pageContext.request.contextPath}/profile">Profile</a>
                <a href="${pageContext.request.contextPath}/cart">Cart <c:if test="${cartCount > 0}">(${cartCount})</c:if></a>
            </c:if>
        </nav>
        <div class="header-actions">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <span class="user-greeting">Hi, ${sessionScope.user.firstName}</span>
                    <a class="btn btn-sm" href="${pageContext.request.contextPath}/logout">Logout</a>
                </c:when>
                <c:otherwise>
                    <a class="btn btn-sm btn-secondary" href="${pageContext.request.contextPath}/customer/login.jsp">Login</a>
                    <a class="btn btn-sm" href="${pageContext.request.contextPath}/customer/register.jsp">Register</a>
                </c:otherwise>
            </c:choose>
            <a class="btn btn-sm btn-outline" href="${pageContext.request.contextPath}/admin/login">Admin</a>
        </div>
    </div>
</header>
<main class="main-content">

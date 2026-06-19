<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - Fashion Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="site-header admin-header">
    <div class="header-inner">
        <a class="logo" href="${pageContext.request.contextPath}/admin/dashboard">Admin Panel</a>
        <nav class="main-nav">
            <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/products">Products</a>
            <a href="${pageContext.request.contextPath}/admin/orders">Orders</a>
        </nav>
        <div class="header-actions">
            <span class="user-greeting">${sessionScope.admin.username}</span>
            <a class="btn btn-sm" href="${pageContext.request.contextPath}/admin/logout">Logout</a>
        </div>
    </div>
</header>

<main class="main-content">
    <section class="section">
        <h1>Dashboard</h1>
        <div class="stats-grid">
            <div class="stat-card">
                <h3>${products.size()}</h3>
                <p>Total Products</p>
            </div>
            <div class="stat-card">
                <h3>${orders.size()}</h3>
                <p>Total Orders</p>
            </div>
        </div>

        <h2>Recent Orders</h2>
        <c:choose>
            <c:when test="${not empty orders}">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Order #</th>
                            <th>Customer</th>
                            <th>Amount</th>
                            <th>Status</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" items="${orders}" begin="0" end="4">
                            <tr>
                                <td>${order.orderNumber}</td>
                                <td>${order.customerName}</td>
                                <td>&#8377;${order.totalAmount}</td>
                                <td><span class="status-badge status-${order.orderStatus}">${order.orderStatus}</span></td>
                                <td>${order.orderDate}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p class="empty-msg">No orders yet.</p>
            </c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>

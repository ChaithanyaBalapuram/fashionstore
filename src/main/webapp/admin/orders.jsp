<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Orders - Fashion Store</title>
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
        <h1>Manage Orders</h1>

        <c:choose>
            <c:when test="${not empty orders}">
                <c:forEach var="order" items="${orders}">
                    <div class="order-card">
                        <div class="order-header">
                            <div>
                                <strong>Order #${order.orderNumber}</strong>
                                <span class="order-date">${order.orderDate}</span>
                            </div>
                            <span class="status-badge status-${order.orderStatus}">${order.orderStatus}</span>
                        </div>
                        <p><strong>Customer:</strong> ${order.customerName}</p>
                        <p><strong>Phone:</strong> ${order.phone}</p>
                        <p><strong>Total:</strong> &#8377;${order.totalAmount}</p>
                        <p><strong>Payment:</strong> ${order.paymentMethod}</p>
                        <p><strong>Address:</strong> ${order.shippingAddress}</p>

                        <table class="data-table compact">
                            <thead>
                                <tr><th>Product</th><th>Variant</th><th>Qty</th><th>Price</th></tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${order.items}">
                                    <tr>
                                        <td>${item.productName}</td>
                                        <td>${item.color} / ${item.size}</td>
                                        <td>${item.quantity}</td>
                                        <td>&#8377;${item.price}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <c:if test="${not empty order.tracking}">
                        <div class="tracking-timeline">
                            <h4>Tracking History</h4>
                            <c:forEach var="track" items="${order.tracking}">
                                <div class="tracking-step">
                                    <strong>${track.status}</strong> - ${track.remarks}
                                    <small>${track.updatedAt} (${track.updatedBy})</small>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/admin/orders" method="post" class="status-form">
                            <input type="hidden" name="orderId" value="${order.orderId}">
                            <select name="status">
                                <option value="Pending" ${order.orderStatus == 'Pending' ? 'selected' : ''}>Pending</option>
                                <option value="Confirmed" ${order.orderStatus == 'Confirmed' ? 'selected' : ''}>Confirmed</option>
                                <option value="Shipped" ${order.orderStatus == 'Shipped' ? 'selected' : ''}>Shipped</option>
                                <option value="Delivered" ${order.orderStatus == 'Delivered' ? 'selected' : ''}>Delivered</option>
                                <option value="Cancelled" ${order.orderStatus == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                            </select>
                            <button type="submit" class="btn btn-sm">Update Status</button>
                        </form>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p class="empty-msg">No orders yet.</p>
            </c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="../includes/header.jsp" %>

<section class="section">
    <h1>My Orders</h1>

    <c:if test="${param.success == 'placed'}">
        <div class="message success">Your order has been placed successfully!</div>
    </c:if>
    <c:if test="${param.success == 'cancelled'}">
        <div class="message success">Order cancelled successfully.</div>
    </c:if>
    <c:if test="${param.error == 'cancel'}">
        <div class="message error">Could not cancel order. Only pending orders can be cancelled.</div>
    </c:if>

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
                            <h4>Order Tracking</h4>
                            <c:forEach var="track" items="${order.tracking}">
                                <div class="tracking-step">
                                    <strong>${track.status}</strong> - ${track.remarks}
                                    <small>${track.updatedAt} by ${track.updatedBy}</small>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>

                    <c:if test="${order.orderStatus == 'Pending'}">
                        <form action="${pageContext.request.contextPath}/orders" method="post" class="inline-form" style="margin-top:12px">
                            <input type="hidden" name="orderId" value="${order.orderId}">
                            <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Cancel this order?')">Cancel Order</button>
                        </form>
                    </c:if>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p class="empty-msg">You have no orders yet.</p>
            <a class="btn" href="${pageContext.request.contextPath}/products">Start Shopping</a>
        </c:otherwise>
    </c:choose>
</section>

<%@ include file="../includes/footer.jsp" %>

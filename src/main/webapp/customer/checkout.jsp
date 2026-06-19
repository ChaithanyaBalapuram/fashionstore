<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="../includes/header.jsp" %>

<section class="section">
    <h1>Checkout</h1>

    <c:if test="${param.error == 'failed'}">
        <div class="message error">Order placement failed. Please try again.</div>
    </c:if>
    <c:if test="${param.error == 'validation'}">
        <div class="message error">Please fill in all required shipping details.</div>
    </c:if>

    <div class="checkout-layout">
        <div class="checkout-form">
            <h2>Shipping Details</h2>
            <form action="${pageContext.request.contextPath}/checkout" method="post">
                <div class="form-group">
                    <label>Shipping Address</label>
                    <textarea name="shippingAddress" required>${user.address}</textarea>
                </div>
                <div class="form-group">
                    <label>Phone</label>
                    <input type="text" name="phone" value="${user.phone}" required>
                </div>
                <div class="form-group">
                    <label>Payment Method</label>
                    <p><strong>Cash on Delivery (COD)</strong></p>
                    <p class="hint">Pay when your order is delivered.</p>
                </div>
                <button type="submit" class="btn btn-lg">Place Order</button>
            </form>
        </div>

        <div class="order-summary">
            <h2>Order Summary</h2>
            <c:forEach var="item" items="${cartItems}">
                <div class="summary-item">
                    <span>${item.productName} (${item.color}/${item.size}) x ${item.quantity}</span>
                    <span>&#8377;${item.subtotal}</span>
                </div>
            </c:forEach>
            <div class="summary-total">
                <strong>Total</strong>
                <strong>&#8377;${cartTotal}</strong>
            </div>
        </div>
    </div>
</section>

<%@ include file="../includes/footer.jsp" %>

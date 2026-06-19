<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="../includes/header.jsp" %>

<section class="section">
    <h1>Shopping Cart</h1>

    <c:if test="${param.error == 'stock'}">
        <div class="message error">Some items are out of stock. Please update your cart.</div>
    </c:if>

    <c:choose>
        <c:when test="${not empty cartItems}">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Variant</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Subtotal</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${cartItems}">
                        <tr>
                            <td>
                                <strong>${item.productName}</strong><br>
                                <small>${item.brand}</small>
                            </td>
                            <td>${item.color} / ${item.size}</td>
                            <td>&#8377;${item.price}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/cart" method="post" class="inline-form">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="cartId" value="${item.cartId}">
                                    <input type="number" name="quantity" value="${item.quantity}" min="1" max="99">
                                    <button type="submit" class="btn btn-sm">Update</button>
                                </form>
                            </td>
                            <td>&#8377;${item.subtotal}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/cart" method="post" class="inline-form">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="cartId" value="${item.cartId}">
                                    <button type="submit" class="btn btn-sm btn-danger">Remove</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="4" class="text-right"><strong>Total</strong></td>
                        <td colspan="2"><strong>&#8377;${cartTotal}</strong></td>
                    </tr>
                </tfoot>
            </table>

            <div class="cart-actions">
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/products">Continue Shopping</a>
                <a class="btn btn-lg" href="${pageContext.request.contextPath}/checkout">Proceed to Checkout</a>
            </div>
        </c:when>
        <c:otherwise>
            <p class="empty-msg">Your cart is empty.</p>
            <a class="btn" href="${pageContext.request.contextPath}/products">Browse Products</a>
        </c:otherwise>
    </c:choose>
</section>

<%@ include file="../includes/footer.jsp" %>

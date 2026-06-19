<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="includes/header.jsp" %>

<section class="section product-detail-page">
    <div class="product-detail">
        <div class="product-detail-image">
            <c:choose>
                <c:when test="${not empty images}">
                    <div class="image-gallery">
                        <c:forEach var="img" items="${images}">
                            <img src="${pageContext.request.contextPath}/${img.imageUrl}" alt="${product.productName}"
                                 onerror="this.style.display='none'">
                        </c:forEach>
                    </div>
                </c:when>
                <c:when test="${not empty product.thumbnailImage}">
                    <img src="${pageContext.request.contextPath}/${product.thumbnailImage}" alt="${product.productName}">
                </c:when>
                <c:otherwise>
                    <div class="image-placeholder large">${product.brand}</div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="product-detail-info">
            <span class="product-brand">${product.brand}</span>
            <h1>${product.productName}</h1>
            <span class="product-category">${product.categoryName}</span>

            <div class="product-price">
                <span class="price-current">&#8377;${product.effectivePrice}</span>
                <c:if test="${product.discountPrice != null}">
                    <span class="price-original">&#8377;${product.originalPrice}</span>
                </c:if>
            </div>

            <p class="product-description">${product.description}</p>

            <c:if test="${not empty detail}">
                <div class="product-specs">
                    <p><strong>Gender:</strong> ${detail.gender}</p>
                    <p><strong>Material:</strong> ${detail.material}</p>
                    <p><strong>Fit:</strong> ${detail.fitType}</p>
                    <p><strong>Origin:</strong> ${detail.countryOfOrigin}</p>
                    <p><strong>Care:</strong> ${detail.careInstructions}</p>
                </div>
            </c:if>

            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <c:set var="hasStock" value="false"/>
                    <c:forEach var="v" items="${variants}">
                        <c:if test="${v.stock > 0}"><c:set var="hasStock" value="true"/></c:if>
                    </c:forEach>

                    <c:choose>
                        <c:when test="${hasStock}">
                            <form action="${pageContext.request.contextPath}/cart" method="post" class="add-to-cart-form">
                                <input type="hidden" name="action" value="add">

                                <div class="form-group">
                                    <label>Color &amp; Size</label>
                                    <select name="variantId" required>
                                        <c:forEach var="v" items="${variants}">
                                            <c:if test="${v.stock > 0}">
                                                <option value="${v.variantId}">${v.color} - ${v.size} (${v.stock} in stock)</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Quantity</label>
                                    <input type="number" name="quantity" value="1" min="1" max="10" required>
                                </div>

                                <button type="submit" class="btn btn-lg">Add to Cart</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <p class="message error">This product is currently out of stock.</p>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <p class="login-prompt">
                        <a href="${pageContext.request.contextPath}/customer/login.jsp">Login</a> to add items to cart.
                    </p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<%@ include file="includes/footer.jsp" %>

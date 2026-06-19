<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    if (request.getAttribute("featuredProducts") == null) {
        response.sendRedirect(request.getContextPath() + "/home");
        return;
    }
%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="includes/header.jsp" %>

<section class="hero">
    <div class="hero-content">
        <h1>Discover Your Style</h1>
        <p>Shop the latest trends in women's, men's fashion, accessories and footwear.</p>
        <a class="btn btn-lg" href="${pageContext.request.contextPath}/products">Shop Now</a>
    </div>
</section>

<section class="section">
    <h2 class="section-title">Shop by Category</h2>
    <div class="category-grid">
        <c:forEach var="cat" items="${categories}">
            <a class="category-card" href="${pageContext.request.contextPath}/products?category=${cat.categoryId}">
                <h3>${cat.categoryName}</h3>
                <p>${cat.description}</p>
            </a>
        </c:forEach>
    </div>
</section>

<section class="section">
    <h2 class="section-title">Featured Products</h2>
    <div class="product-grid">
        <c:forEach var="p" items="${featuredProducts}">
            <div class="product-card">
                <a href="${pageContext.request.contextPath}/product?id=${p.productId}">
                    <div class="product-image">
                        <c:choose>
                            <c:when test="${not empty p.thumbnailImage}">
                                <img src="${pageContext.request.contextPath}/${p.thumbnailImage}" alt="${p.productName}">
                            </c:when>
                            <c:otherwise>
                                <div class="image-placeholder">${p.brand}</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="product-info">
                        <span class="product-brand">${p.brand}</span>
                        <h3>${p.productName}</h3>
                        <div class="product-price">
                            <span class="price-current">&#8377;${p.effectivePrice}</span>
                            <c:if test="${p.discountPrice != null}">
                                <span class="price-original">&#8377;${p.originalPrice}</span>
                            </c:if>
                        </div>
                    </div>
                </a>
            </div>
        </c:forEach>
    </div>
    <c:if test="${empty featuredProducts}">
        <p class="empty-msg">No featured products yet. Check back soon!</p>
    </c:if>
</section>

<%@ include file="includes/footer.jsp" %>

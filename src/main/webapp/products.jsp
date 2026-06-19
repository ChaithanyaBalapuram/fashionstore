<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="includes/header.jsp" %>

<section class="section">
    <div class="page-header">
        <h1>All Products</h1>
        <form class="search-form" action="${pageContext.request.contextPath}/products" method="get">
            <input type="text" name="search" placeholder="Search products..." value="${search}">
            <button type="submit" class="btn">Search</button>
        </form>
    </div>

    <div class="shop-layout">
        <aside class="sidebar">
            <h3>Categories</h3>
            <ul class="category-list">
                <li><a href="${pageContext.request.contextPath}/products"
                       class="${empty selectedCategory ? 'active' : ''}">All</a></li>
                <c:forEach var="cat" items="${categories}">
                    <li>
                        <a href="${pageContext.request.contextPath}/products?category=${cat.categoryId}"
                           class="${selectedCategory == cat.categoryId ? 'active' : ''}">
                            ${cat.categoryName}
                        </a>
                    </li>
                </c:forEach>
            </ul>

            <h3 style="margin-top:24px">Filters</h3>
            <form action="${pageContext.request.contextPath}/products" method="get" class="filter-form">
                <c:if test="${not empty selectedCategory}">
                    <input type="hidden" name="category" value="${selectedCategory}">
                </c:if>
                <label>Min Price</label>
                <input type="number" name="minPrice" value="${minPrice}" placeholder="0">
                <label>Max Price</label>
                <input type="number" name="maxPrice" value="${maxPrice}" placeholder="10000">
                <label>Sort By</label>
                <select name="sort">
                    <option value="" ${empty sort ? 'selected' : ''}>Newest</option>
                    <option value="price_asc" ${sort == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                    <option value="price_desc" ${sort == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                    <option value="name" ${sort == 'name' ? 'selected' : ''}>Name</option>
                </select>
                <button type="submit" class="btn btn-sm" style="width:100%;margin-top:8px">Apply</button>
            </form>
        </aside>

        <div class="product-grid">
            <c:forEach var="p" items="${products}">
                <div class="product-card">
                    <c:if test="${p.newArrival}"><span class="badge badge-new">New</span></c:if>
                    <c:if test="${p.featured}"><span class="badge badge-featured">Featured</span></c:if>
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
                            <span class="product-category">${p.categoryName}</span>
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
    </div>

    <c:if test="${empty products}">
        <p class="empty-msg">No products found.</p>
    </c:if>
</section>

<%@ include file="includes/footer.jsp" %>

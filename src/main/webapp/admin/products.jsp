<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Products - Fashion Store</title>
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
        <h1>Manage Products</h1>

        <div class="admin-form-card">
            <h2>Add New Product</h2>
            <form action="${pageContext.request.contextPath}/admin/products" method="post" class="admin-form">
                <input type="hidden" name="action" value="add">
                <div class="form-row">
                    <input type="text" name="productName" placeholder="Product Name" required>
                    <input type="text" name="brand" placeholder="Brand" required>
                </div>
                <div class="form-row">
                    <select name="categoryId" required>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.categoryId}">${cat.categoryName}</option>
                        </c:forEach>
                    </select>
                    <input type="number" name="originalPrice" placeholder="Original Price" step="0.01" required>
                    <input type="number" name="discountPrice" placeholder="Discount Price" step="0.01">
                </div>
                <div class="form-row">
                    <input type="number" name="stock" placeholder="Stock" value="10" required>
                    <input type="text" name="color" placeholder="Default Color" value="Black" required>
                    <input type="text" name="size" placeholder="Default Size" value="M" required>
                </div>
                <div class="form-row">
                    <input type="text" name="gender" placeholder="Gender">
                    <input type="text" name="material" placeholder="Material">
                    <input type="text" name="fitType" placeholder="Fit Type">
                </div>
                <textarea name="description" placeholder="Description"></textarea>
                <div class="form-row">
                    <label><input type="checkbox" name="featured"> Featured</label>
                    <label><input type="checkbox" name="newArrival" checked> New Arrival</label>
                </div>
                <button type="submit" class="btn">Add Product</button>
            </form>
        </div>

        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Brand</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${products}">
                    <tr class="${p.active ? '' : 'inactive-row'}">
                        <td>${p.productId}</td>
                        <td>${p.productName}</td>
                        <td>${p.brand}</td>
                        <td>${p.categoryName}</td>
                        <td>&#8377;${p.effectivePrice}</td>
                        <td>${p.stock}</td>
                        <td>${p.active ? 'Active' : 'Inactive'}</td>
                        <td>
                            <a class="btn btn-sm" href="${pageContext.request.contextPath}/admin/product?id=${p.productId}">Edit</a>
                            <c:choose>
                                <c:when test="${p.active}">
                                    <form action="${pageContext.request.contextPath}/admin/products" method="post" class="inline-form">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="productId" value="${p.productId}">
                                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Deactivate this product?')">Deactivate</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/admin/products" method="post" class="inline-form">
                                        <input type="hidden" name="action" value="reactivate">
                                        <input type="hidden" name="productId" value="${p.productId}">
                                        <button type="submit" class="btn btn-sm">Reactivate</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </section>
</main>
</body>
</html>

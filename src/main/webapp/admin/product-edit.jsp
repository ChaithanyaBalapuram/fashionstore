<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Product - Fashion Store</title>
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
        <h1>Edit: ${product.productName}</h1>
        <p><a href="${pageContext.request.contextPath}/admin/products">&larr; Back to Products</a></p>

        <div class="admin-form-card">
            <h2>Product Details</h2>
            <form action="${pageContext.request.contextPath}/admin/product" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="productId" value="${product.productId}">
                <div class="form-row">
                    <input type="text" name="productName" value="${product.productName}" required>
                    <input type="text" name="brand" value="${product.brand}" required>
                </div>
                <div class="form-row">
                    <select name="categoryId" required>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.categoryId}" ${cat.categoryId == product.categoryId ? 'selected' : ''}>${cat.categoryName}</option>
                        </c:forEach>
                    </select>
                    <input type="number" name="originalPrice" value="${product.originalPrice}" step="0.01" required>
                    <input type="number" name="discountPrice" value="${product.discountPrice}" step="0.01">
                </div>
                <div class="form-row">
                    <input type="number" name="stock" value="${product.stock}" required>
                    <label><input type="checkbox" name="featured" ${product.featured ? 'checked' : ''}> Featured</label>
                    <label><input type="checkbox" name="newArrival" ${product.newArrival ? 'checked' : ''}> New Arrival</label>
                    <label><input type="checkbox" name="active" ${product.active ? 'checked' : ''}> Active</label>
                </div>
                <textarea name="description">${product.description}</textarea>
                <h3>Product Specs</h3>
                <div class="form-row">
                    <input type="text" name="gender" value="${detail.gender}" placeholder="Gender">
                    <input type="text" name="material" value="${detail.material}" placeholder="Material">
                    <input type="text" name="fitType" value="${detail.fitType}" placeholder="Fit Type">
                </div>
                <div class="form-row">
                    <input type="text" name="countryOfOrigin" value="${detail.countryOfOrigin}" placeholder="Country of Origin">
                </div>
                <textarea name="careInstructions" placeholder="Care Instructions">${detail.careInstructions}</textarea>
                <button type="submit" class="btn">Save Product</button>
            </form>
        </div>

        <div class="admin-form-card">
            <h2>Variants</h2>
            <table class="data-table compact">
                <thead><tr><th>Color</th><th>Size</th><th>Stock</th><th>Update</th><th>Delete</th></tr></thead>
                <tbody>
                    <c:forEach var="v" items="${variants}">
                        <tr>
                            <td colspan="5">
                                <div class="form-row">
                                    <form action="${pageContext.request.contextPath}/admin/product" method="post" class="inline-form" style="flex:1">
                                        <input type="hidden" name="productId" value="${product.productId}">
                                        <input type="hidden" name="action" value="updateVariant">
                                        <input type="hidden" name="variantId" value="${v.variantId}">
                                        <input type="text" name="color" value="${v.color}">
                                        <input type="text" name="size" value="${v.size}">
                                        <input type="number" name="stock" value="${v.stock}">
                                        <button type="submit" class="btn btn-sm">Update</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/admin/product" method="post" class="inline-form">
                                        <input type="hidden" name="productId" value="${product.productId}">
                                        <input type="hidden" name="action" value="deleteVariant">
                                        <input type="hidden" name="variantId" value="${v.variantId}">
                                        <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <form action="${pageContext.request.contextPath}/admin/product" method="post" style="margin-top:16px">
                <input type="hidden" name="action" value="addVariant">
                <input type="hidden" name="productId" value="${product.productId}">
                <div class="form-row">
                    <input type="text" name="color" placeholder="Color" required>
                    <input type="text" name="size" placeholder="Size" required>
                    <input type="number" name="stock" placeholder="Stock" value="10" required>
                    <button type="submit" class="btn btn-sm">Add Variant</button>
                </div>
            </form>
        </div>

        <div class="admin-form-card">
            <h2>Images</h2>
            <c:if test="${not empty images}">
                <ul>
                    <c:forEach var="img" items="${images}">
                        <li>${img.imageUrl}</li>
                    </c:forEach>
                </ul>
            </c:if>
            <form action="${pageContext.request.contextPath}/admin/product" method="post">
                <input type="hidden" name="action" value="addImage">
                <input type="hidden" name="productId" value="${product.productId}">
                <input type="text" name="imageUrl" placeholder="images/products/photo.jpg" required>
                <button type="submit" class="btn btn-sm">Add Image URL</button>
            </form>
        </div>
    </section>
</main>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="../includes/header.jsp" %>

<section class="section">
    <h1>My Profile</h1>

    <c:if test="${param.success == 'updated'}">
        <div class="message success">Profile updated successfully.</div>
    </c:if>
    <c:if test="${param.success == 'password'}">
        <div class="message success">Password changed successfully.</div>
    </c:if>
    <c:if test="${param.error == 'password'}">
        <div class="message error">Could not change password. Check current password.</div>
    </c:if>
    <c:if test="${param.error == 'failed'}">
        <div class="message error">Profile update failed.</div>
    </c:if>

    <div class="checkout-layout">
        <div class="checkout-form">
            <h2>Personal Information</h2>
            <form action="${pageContext.request.contextPath}/profile" method="post">
                <input type="text" name="firstName" value="${profile.firstName}" placeholder="First Name" required>
                <input type="text" name="lastName" value="${profile.lastName}" placeholder="Last Name" required>
                <input type="email" value="${profile.email}" disabled>
                <input type="text" name="phone" value="${profile.phone}" placeholder="Phone" required>
                <textarea name="address" placeholder="Address">${profile.address}</textarea>
                <button type="submit" class="btn">Save Changes</button>
            </form>
        </div>

        <div class="order-summary">
            <h2>Change Password</h2>
            <form action="${pageContext.request.contextPath}/profile" method="post">
                <input type="hidden" name="action" value="password">
                <input type="password" name="currentPassword" placeholder="Current Password" required>
                <input type="password" name="newPassword" placeholder="New Password (min 6 chars)" required minlength="6">
                <button type="submit" class="btn">Update Password</button>
            </form>
        </div>
    </div>
</section>

<%@ include file="../includes/footer.jsp" %>

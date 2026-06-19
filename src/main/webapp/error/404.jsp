<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="../includes/header.jsp" %>
<section class="section" style="text-align:center;padding:80px 24px">
    <h1>404 - Page Not Found</h1>
    <p>The page you are looking for does not exist.</p>
    <a class="btn" href="${pageContext.request.contextPath}/home">Go Home</a>
</section>
<%@ include file="../includes/footer.jsp" %>

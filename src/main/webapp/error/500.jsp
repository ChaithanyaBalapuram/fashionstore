<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="../includes/header.jsp" %>
<section class="section" style="text-align:center;padding:80px 24px">
    <h1>500 - Server Error</h1>
    <p>Something went wrong. Please try again later.</p>
    <a class="btn" href="${pageContext.request.contextPath}/home">Go Home</a>
</section>
<%@ include file="../includes/footer.jsp" %>

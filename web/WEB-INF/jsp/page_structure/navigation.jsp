<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.user.button.signup" var="signUp"/>
    <fmt:message bundle="${locale}" key="locale.user.button.signin" var="signIn"/>
    <fmt:message bundle="${locale}" key="locale.page.title.home" var="homePage"/>
    <fmt:message bundle="${locale}" key="locale.page.title.services" var="servicesPage"/>
    <fmt:message bundle="${locale}" key="locale.page.title.account" var="accountPage"/>
    <fmt:message bundle="${locale}" key="locale.page.title.orders" var="ordersPage"/>
    <fmt:message bundle="${locale}" key="locale.page.title.users" var="usersPage"/>
</head>
<body>
<a href="${pageContext.request.contextPath}/home">${homePage}</a>
<a href="${pageContext.request.contextPath}/services">${servicesPage}</a>
<c:if test="${empty sessionScope.user}">
    <a href="${pageContext.request.contextPath}/register">${signUp}</a>
    <a href="${pageContext.request.contextPath}/login">${signIn}</a>
</c:if>
<c:if test="${sessionScope.user ne null}">
    <a href="${pageContext.request.contextPath}/orders">${ordersPage}</a>
    <a href="${pageContext.request.contextPath}/account">${accountPage}</a>
</c:if>
<c:if test="${sessionScope.user.status == 'admin'}">
    <a href="app?command=viewUsers">${usersPage}</a>
</c:if>
</body>
</html>

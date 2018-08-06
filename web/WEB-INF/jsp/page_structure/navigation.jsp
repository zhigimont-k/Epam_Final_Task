<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">

    <script src="${pageContext.request.contextPath}/js/support/jquery-3.3.1.min.js"></script>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.user.button.signup" var="signUp"/>
    <fmt:message bundle="${locale}" key="locale.user.button.signin" var="signIn"/>
    <fmt:message bundle="${locale}" key="locale.page.title.home" var="homePage"/>
    <fmt:message bundle="${locale}" key="locale.page.title.services" var="servicesPage"/>
    <fmt:message bundle="${locale}" key="locale.page.title.account" var="accountPage"/>
    <fmt:message bundle="${locale}" key="locale.page.title.orders" var="ordersPage"/>
    <fmt:message bundle="${locale}" key="locale.page.title.users" var="usersPage"/>
    <fmt:message bundle="${locale}" key="locale.user.button.logout" var="logout"/>
</head>
<body>

<div id="navigation">
    <ul>
        <li>
            <a href="${pageContext.request.contextPath}/home">${homePage}</a>
        </li>
        <li>
            <a href="app?command=viewActivities">${servicesPage}</a>
        </li>

        <c:if test="${empty sessionScope.user}">
            <li><a href="${pageContext.request.contextPath}/register">${signUp}</a></li>
            <li><a href="${pageContext.request.contextPath}/login">${signIn}</a></li>
        </c:if>
        <c:if test="${sessionScope.user ne null}">
            <li><a href="app?command=viewUserOrders&pageNumber=1">My orders</a></li>
            <li><a href="app?command=viewUserInfo">account</a></li>
            <li><a href="${pageContext.request.contextPath}/addMoney">Add money to card</a></li>
        </c:if>
        <c:if test="${sessionScope.user ne null && sessionScope.user.status == 'admin'}">
            <li><a href="app?command=viewUsers">${usersPage}</a></li>
            <li><a href="app?command=viewAllOrders&pageNumber=1">${ordersPage}</a></li>
        </c:if>
        <c:if test="${sessionScope.user ne null && sessionScope.user.status ne 'banned'}">
            <li><a href="app?command=createOrder">Make an order</a></li>
        </c:if>
        <c:if test="${sessionScope.user ne null}">
            <li style="float:right"><a href="app?command=logout">${logout}</a></li>
        </c:if>
    </ul>
</div>
</body>
</html>

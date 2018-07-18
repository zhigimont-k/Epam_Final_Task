<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cbb" uri="cbbtaglib" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.user.role.guest" var="guest"/>
    <fmt:message bundle="${locale}" key="locale.user.message.welcome" var="welcomeMessage"/>
    <fmt:message bundle="${locale}" key="locale.user.message.welcomeBack" var="welcomeBackMessage"/>
</head>
<body>
<div>

    <c:choose>
        <c:when test="${not empty sessionScope.user.userName}">
            <cbb:welcome name="${sessionScope.user.userName}" message="${welcomeBackMessage}"/>
        </c:when>
        <c:when test="${not empty sessionScope.user}">
            <cbb:welcome name="${sessionScope.user.login}" message="${welcomeBackMessage}"/>
        </c:when>
        <c:otherwise>
            <cbb:welcome name="${guest}" message="${welcomeMessage}"/>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>

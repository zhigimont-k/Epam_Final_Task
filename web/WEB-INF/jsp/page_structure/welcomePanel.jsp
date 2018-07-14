<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cbb" uri="cbbtaglib" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.user.role.guest" var="guest"/>
</head>
<body>
<div>

    <c:choose>
        <c:when test="${not empty sessionScope.user.userName}">
            <cbb:welcome name="${sessionScope.user.userName}" lang="${locale}"/>
        </c:when>
        <c:when test="${not empty sessionScope.user}">
            <cbb:welcome name="${sessionScope.user.login}" lang="${locale}"/>
        </c:when>
        <c:otherwise>
            <cbb:welcome name="${guest}" lang="${locale}"/>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>

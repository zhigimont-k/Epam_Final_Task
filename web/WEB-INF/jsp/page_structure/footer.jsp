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
    <style>
        .footer {
            position: relative;
            right: 0;
            bottom: 0;
            left: 0;
            background-color: black;
            color: whitesmoke;
            text-align: center;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="footer">
    <div>
        <cbb:copyright projectName="Cat Beauty Bar" message="All rights reserved." year="2018"/>
    </div>
</div>
</body>
</html>

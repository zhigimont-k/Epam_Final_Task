<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.user.text.welcome" var="welcome"/>
</head>
<body>
<jsp:useBean id="user" class="by.epam.web.entity.User" type="by.epam.web.entity.User" scope="session"/>
${welcome}, ${sessionScope.user.userName}
</body>
</html>

<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.user.button.logout" var="logout"/>

</head>
<body>
<form name="logoutForm" method="POST" action="app">
    <input type="hidden" name="command" value="logout"/>
    <input type="submit" value="${logout}"/>

</form>
</body>
</html>

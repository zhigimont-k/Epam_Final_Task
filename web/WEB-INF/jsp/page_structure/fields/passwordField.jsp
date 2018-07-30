<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.user.label.password" var="passwordLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.showPassword" var="showPassword"/>
    <script type="text/javascript" src="../js/inputScript.js"></script>
</head>
<body>
<label>${passwordLabel}
    <input type="password" name="password" id="passwordField" maxlength="32" required/></label>
<input type="checkbox" onclick="togglePasswordVisibility()">${showPassword}
</body>
</html>

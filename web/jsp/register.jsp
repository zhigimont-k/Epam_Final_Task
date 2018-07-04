<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.page.title.registration" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.user.label.login" var="login"/>
    <fmt:message bundle="${locale}" key="locale.user.label.password" var="password"/>
    <fmt:message bundle="${locale}" key="locale.user.label.username" var="userName"/>
    <fmt:message bundle="${locale}" key="locale.user.label.email" var="email"/>
    <fmt:message bundle="${locale}" key="locale.user.label.phonenumber" var="phoneNumber"/>
    <fmt:message bundle="${locale}" key="locale.user.button.signup" var="button"/>

    <title>${pageTitle} | Cat Beauty Bar</title>
</head>
<body>
<form name="registerForm" method="POST" action="app">
    <input type="hidden" name="command" value="register"/>
    ${login}<br/>
    <input type="text" name="login" maxlength="20" minlength="4"/>
    <br/>${password}<br/>
    <input type="password" name="password" maxlength="32" minlength="10"/>
    <br/>
    <br/>${email}<br/>
    <input type="email" name="email" maxlength="40" minlength="10"/>
    <br/>
    <br/>${phoneNumber}<br/>
    <input type="number" name="phoneNumber" maxlength="32" minlength="10"/>
    <br/>
    <br/>${userName}<br/>
    <input type="text" name="userName" maxlength="20" minlength="3"/>
    <br/>
    <input type="submit" value="${button}"/>
    <br/>
</form>
</body>
</html>

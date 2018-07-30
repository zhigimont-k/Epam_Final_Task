<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.page.title.auth" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.user.label.login" var="loginLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.password" var="passwordLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.showPassword" var="showPassword"/>
    <fmt:message bundle="${locale}" key="locale.user.button.signin" var="button"/>

    <fmt:message bundle="${locale}" key="locale.user.text.noAccountYet" var="toRegister"/>
    <fmt:message bundle="${locale}" key="locale.user.button.signup" var="signUp"/>

    <fmt:message bundle="${locale}" key="locale.user.warning.auth.fail" var="authFailMessage"/>

    <fmt:message bundle="${locale}" key="locale.basic.projectname" var="projectName"/>
    <title>${pageTitle} | ${projectName}</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">

    <script type="text/javascript" src="../js/inputScript.js"></script>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<c:if test="${not empty sessionScope.user}">
    <jsp:forward page="/home"/>
</c:if>
<div id="custom-form">
    <div>
        <form name="loginForm" method="POST" action="app">
            <input type="hidden" name="command" value="login"/>
            <label>${loginLabel}
                <input type="text" name="login" maxlength="20" required/></label>
            <label>${passwordLabel}
                <br/>
                <input type="password" name="password" id="passwordField" maxlength="32" required/></label>
            <br/>
            <label><input type="checkbox" onclick="togglePasswordVisibility()">${showPassword}</label>
            <br/>
            <input type="submit" value="${button}"/>
            <br/>
            <c:if test="${authFail == true}">
                ${authFailMessage}
            </c:if>
            <br/>
    </form>

    ${toRegister} <a href="${pageContext.request.contextPath}/register">${signUp}</a>
    <br/>
    <a href="${pageContext.request.contextPath}/resetPassword">Forgot your password?</a>

    </div>
</div>
</body>
</html>

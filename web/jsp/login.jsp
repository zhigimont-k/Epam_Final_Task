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
    <fmt:message bundle="${locale}" key="locale.user.button.signin" var="button"/>

    <fmt:message bundle="${locale}" key="locale.user.text.noAccountYet" var="toRegister"/>
    <fmt:message bundle="${locale}" key="locale.user.button.signup" var="signUp"/>

    <fmt:message bundle="${locale}" key="locale.user.warning.auth.fail" var="authFailMessage"/>

    <fmt:message bundle="${locale}" key="locale.basic.projectname" var="projectName"/>
    <title>${pageTitle} | ${projectName}</title>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<c:choose>
    <c:when test = "${not empty sessionScope.user}">
        <jsp:forward page="/home" />
    </c:when>
</c:choose>
<form name="loginForm" method="POST" action="app">
    <input type="hidden" name="command" value="login"/>
    <label>${loginLabel}
    <input type="text" name="login" maxlength="20" required/></label>
    <label>${passwordLabel}
    <input type="password" name="password" maxlength="32" required/></label>
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
</body>
</html>

<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.page.title.registration" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.user.label.login" var="loginLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.password" var="passwordLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.username" var="userNameLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.email" var="emailLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.phonenumber" var="phoneNumberLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.button.signup" var="button"/>

    <fmt:message bundle="${locale}" key="locale.user.text.haveAccountAlready" var="toLogin"/>
    <fmt:message bundle="${locale}" key="locale.user.button.signin" var="signIn"/>

    <fmt:message bundle="${locale}" key="locale.user.warning.login" var="loginWarning"/>
    <fmt:message bundle="${locale}" key="locale.user.warning.email" var="emailWarning"/>
    <fmt:message bundle="${locale}" key="locale.user.warning.phonenumber" var="phoneNumberWarning"/>

    <fmt:message bundle="${locale}" key="locale.basic.projectname" var="projectName"/>
    <title>${pageTitle} | ${projectName}</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<c:if test = "${not empty sessionScope.user}">
    <jsp:forward page="/home" />
</c:if>
<form name="registerForm" method="POST" action="app">
    <input type="hidden" name="command" value="register"/>
    <label>${loginLabel}
    <input type="text" name="login" maxlength="20" minlength="4" required/></label>
    <c:if test="${loginExists == true}">
        ${loginWarning}
    </c:if>
    <br/>
    <label>${passwordLabel}
    <input type="password" name="password" maxlength="32" minlength="10" required/></label>
    <br/>
    <label>${emailLabel}
    <input type="email" name="email" maxlength="40" minlength="10" required/></label>
    <c:if test="${emailExists == true}">
        ${emailWarning}
    </c:if>
    <br/>
    <label>${phoneNumberLabel}
    <input type="text" name="phoneNumber" maxlength="32" minlength="10"/></label>
    <c:if test="${phoneNumberExists == true}">
        ${phoneNumberWarning}
    </c:if>
    <br/>
    <label>${userNameLabel}
    <input type="text" name="userName" maxlength="20" minlength="3"/></label>
    <br/>
    <input type="submit" value="${button}"/>
</form>

${toLogin} <a href="${pageContext.request.contextPath}/login">${signIn}</a>
</body>
</html>

<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.page.title.account" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.user.label.login" var="loginLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.password.old" var="oldPasswordLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.password.new" var="newPasswordLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.username" var="userNameLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.email" var="emailLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.phonenumber" var="phoneNumberLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.status" var="statusLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.role.admin" var="admin"/>
    <fmt:message bundle="${locale}" key="locale.user.role.user" var="user"/>
    <fmt:message bundle="${locale}" key="locale.user.role.banned" var="banned"/>


    <fmt:message bundle="${locale}" key="locale.user.warning.auth.fail" var="authFailMessage"/>

    <fmt:message bundle="${locale}" key="locale.common.button.update" var="button"/>

    <fmt:message bundle="${locale}" key="locale.basic.projectname" var="projectName"/>
    <title>${pageTitle} | ${projectName}</title>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<c:if test="${empty sessionScope.user}">
    <jsp:forward page="${pageContext.request.contextPath}/login"/>
</c:if>
<form name="updateUserForm" method="POST" action="app">
    <input type="hidden" name="command" value="updateUser"/>
    ${loginLabel}: ${sessionScope.user.login}
        <br/>
    <label>${oldPasswordLabel}:
        <input type="password" name="password" maxlength="32" minlength="10"/></label>
    <br/>
    <label>${newPasswordLabel}:
        <input type="password" name="newPassword" maxlength="32" minlength="10"/></label>
    <br/>
    ${emailLabel}: ${sessionScope.user.email}
    <br/>
    ${phoneNumberLabel}: ${sessionScope.user.phoneNumber}
    <br/>
    <label>${userNameLabel}:
    <input type="text" name="userName" value="${sessionScope.user.userName}"
           maxlength="20" minlength="3"/></label>
    <br/>
    <input type="submit" value="${button}"/>
    <br/>
    <c:if test="${authFail == true}">
        ${authFailMessage}
    </c:if>
</form>
</body>
</html>

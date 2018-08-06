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
    <fmt:message bundle="${locale}" key="locale.user.label.showPassword" var="showPassword"/>
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
        <form name="registerForm" method="POST" action="app">
            <input type="hidden" name="command" value="register"/>
            <label>${loginLabel}*
                <input type="text" name="login" pattern="(\w){4,20}" maxlength="20" minlength="4"
                       pattern="[\w^_]{4,20}" required/></label>
            <c:if test="${loginExists == true}">
                User with this login already exists.
            </c:if>
            <c:if test="${illegalLogin == true}">
                Login should consist of 4-20 latin characters or numbers.
            </c:if>
            <br/>
            <label>${passwordLabel}*
                <br/>
                <input type="password" name="password" id="passwordField" maxlength="32"
                       pattern="[\w^_]{6,32}" required/></label>
            <label><input type="checkbox" onclick="togglePasswordVisibility(passwordField)">${showPassword}</label>
            <br/>
            <c:if test="${illegalPassword == true}">
                Password should consist of 6-32 latin characters or numbers.
            </c:if>
            <label>${emailLabel}*
                <br/>
                <input type="email" name="email" maxlength="50" minlength="5"
                       pattern="([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})"
                       required/></label>
            <c:if test="${emailExists == true}">
                User with this email already exists.
            </c:if>
            <c:if test="${illegalEmail == true}">
                Email should consist of 5-50 characters and contain @ symbol.
            </c:if>
            <br/>
            <label>${phoneNumberLabel}*
                <input type="text" name="phoneNumber" maxlength="13" minlength="13"
                pattern="\+(\d{12})" required/></label>
            <c:if test="${phoneNumberExists == true}">
                User with this phone number already exists.
            </c:if>
            <c:if test="${illegalPhoneNumber == true}">
                Phone number should consist of 13 characters and begin with +.
            </c:if>
            <br/>
            <label>${userNameLabel}
                <input type="text" name="userName" maxlength="40" minlength="2"
                pattern="[\p{L}\s]{2,40}"/></label>
            <br/>
            <c:if test="${illegalUserName == true}">
                Username should consist of 2-40 symbols or left empty.
            </c:if>
            <label>Номер карты*
                <input type="text" name="cardNumber" maxlength="16" minlength="16"
                pattern="\d{16}" required/></label>
            <c:if test="${cardNumberExists == true}">
                User with this card number already exists.
            </c:if>
            <c:if test="${illegalCardNumber == true}">
                Card number should consist of 16 digits.
            </c:if>
            <br/>
            <input type="submit" value="${button}"/>
        </form>

        ${toLogin} <a href="${pageContext.request.contextPath}/login">${signIn}</a>
    </div>
</div>
</body>
</html>

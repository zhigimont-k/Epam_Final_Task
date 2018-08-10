<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
          crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/js/support/jquery-3.3.1.min.js"></script>
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
                <input type="text" name="login" maxlength="20" minlength="4"
                       pattern="[\w^_]{4,20}" required/></label>
            <c:if test="${loginExists == true}">
                <div class="alert alert-danger alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>Warning!</strong> User with this login already exists.
                </div>
            </c:if>
            <br/>
            <label>${passwordLabel}*
                <br/>
                <input type="password" name="password" id="passwordField" maxlength="32"
                       pattern="[\w^_]{6,32}" required/></label>
            <label><input type="checkbox" onclick="togglePasswordVisibility()">${showPassword}</label>
            <br/>
            <label>${emailLabel}*
                <br/>
                <input type="email" name="email" maxlength="50" minlength="5"
                       pattern="([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})"
                       required/></label>
            <c:if test="${emailExists == true}">
                <div class="alert alert-danger alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>Warning!</strong> User with this email already exists.
                </div>
            </c:if>
            <br/>
            <label>${phoneNumberLabel}*
                <input type="text" name="phoneNumber" maxlength="13" minlength="13"
                       pattern="\+(\d{12})" required/></label>
            <c:if test="${phoneNumberExists == true}">
                <div class="alert alert-danger alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>Warning!</strong> User with this phone number already exists.
                </div>
            </c:if>
            <br/>
            <label>${userNameLabel}
                <input type="text" name="userName" maxlength="40" minlength="2"
                       pattern="[\p{L}\s]{2,40}"/></label>
            <br/>
            <label>Номер карты*
                <input type="text" name="cardNumber" maxlength="16" minlength="16"
                       pattern="\d{16}" required/></label>
            <c:if test="${cardNumberExists == true}">
                <div class="alert alert-danger alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>Warning!</strong> User with this card number already exists.
                </div>
            </c:if>
            <br/>
            <input type="submit" value="${button}"/>
            <c:if test="${illegalInput == true}">
                Please check if your input is correct
            </c:if>
        </form>

        ${toLogin} <a href="${pageContext.request.contextPath}/login">${signIn}</a>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>

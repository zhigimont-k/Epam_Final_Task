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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">

    <title>Add money | ${projectName}</title>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<c:if test="${empty sessionScope.user}">
    <jsp:forward page="/home"/>
</c:if>
<div id="custom-form">
    <div>
        <form name="addMoneyForm" method="POST" action="app">
            <input type="hidden" name="command" value="addMoneyToCard"/>
            <label>Card number:
                <input type="text" name="cardNumber" maxlength="16" minlength="16" required/></label>
            <br/>
            <label>Amount of money:
                <input type="text" name="money" minlength="1" maxlength="6" required/></label>
            <br/>
            <input type="submit" value="Add"/>
            <br/>
        </form>
    </div>
</div>
</body>
</html>

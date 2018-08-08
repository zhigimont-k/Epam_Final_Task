<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.service.label.name" var="serviceNameLabel"/>
    <fmt:message bundle="${locale}" key="locale.service.label.description" var="serviceDescriptionLabel"/>
    <fmt:message bundle="${locale}" key="locale.service.label.price" var="servicePriceLabel"/>
    <fmt:message bundle="${locale}" key="locale.common.button.add" var="button"/>

    <fmt:message bundle="${locale}" key="locale.user.warning.login" var="loginWarning"/>
    <fmt:message bundle="${locale}" key="locale.user.warning.email" var="emailWarning"/>
    <fmt:message bundle="${locale}" key="locale.user.warning.phonenumber" var="phoneNumberWarning"/>

    <title>Add service | Cat Beauty Bar</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<c:if test="${sessionScope.user.status ne 'admin'}">
    <jsp:forward page="${pageContext.request.contextPath}/home"/>
</c:if>
<div id="custom-form">
    <div>
        <form name="addServiceForm" method="POST" action="app">
            <input type="hidden" name="command" value="addActivity"/>
            Add a service:<br/>
            <label>${serviceNameLabel}
                <br/>
                <input type="text" name="activityName" maxlength="40" minlength="2"
                       pattern="[\p{L}\s]{2,40}" required/>
            </label>
            <br/>
            <label>${serviceDescriptionLabel}
                <br/>
                <textarea name="activityDescription" maxlength="280" cols="30"
                          rows="10" required></textarea>
            </label>
            <br/>
            <label>${servicePriceLabel}
                <br/>
                <input type="text" name="activityPrice" maxlength="10" minlength="1"
                       pattern="\d{1,10}" required/>
            </label>

            <br/>
            <input type="submit" value="${button}"/>
            <c:if test="${sessionScope.dataExists == true}">
                Service with this name already exists.
            </c:if>
            <c:if test="${sessionScope.illegalInput == true}">
                Please make sure your input is correct.
            </c:if>
        </form>
    </div>
</div>
</body>
</html>

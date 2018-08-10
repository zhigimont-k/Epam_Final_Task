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
</head>
<body>

<c:if test="${sessionScope.user.status ne 'admin'}">
    <jsp:forward page="${pageContext.request.contextPath}/home"/>
</c:if>
<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
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
            <c:if test="${dataExists == true}">
                <div class="alert alert-danger alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>Fail!</strong> Activity with this name already exists.
                </div>
            </c:if>
        </form>
    </div>
</div>
</body>
</html>

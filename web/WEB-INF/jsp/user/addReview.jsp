<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>

    <fmt:message bundle="${locale}" key="locale.page.title.services" var="pageTitle"/>

    <fmt:message bundle="${locale}" key="locale.service.label.name" var="serviceNameLabel"/>
    <fmt:message bundle="${locale}" key="locale.service.label.description" var="serviceDescriptionLabel"/>
    <fmt:message bundle="${locale}" key="locale.service.label.price" var="servicePriceLabel"/>
    <fmt:message bundle="${locale}" key="locale.common.button.add" var="button"/>

    <fmt:message bundle="${locale}" key="locale.basic.projectname" var="projectName"/>
</head>
<body>
<form name="addReviewForm" method="POST" action="app">
    <input type="hidden" name="activityId" value="${activity.id}"/>
    <input type="hidden" name="command" value="addReview"/>
    Write a review:
    <br/>
    <label>Mark:
        <br/>
        <input type="number" name="reviewMark" max="10" min="1" required/>
    </label>

    <br/>
    <label>Comment:
        <br/>
        <textarea name="reviewMessage" maxlength="280" cols="30" rows="10"></textarea>
    </label>

    <br/>
    <input type="submit" value="${button}"/>
</form>
</body>
</html>
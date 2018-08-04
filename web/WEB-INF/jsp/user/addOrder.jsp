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

    <jsp:useBean id="now" class="java.util.Date"/>

</head>
<body>

<c:if test="${empty sessionScope.user && sessionScope.user.status == 'banned'}">
    <jsp:forward page="${pageContext.request.contextPath}/home"/>
</c:if>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<form name="addOrderForm" method="POST" action="app">
    <input type="hidden" name="command" value="viewOrder"/>
    Add an order:

    <br/>
    <label>Choose date and time for your order:
        <br/>Date:
        <input type="date" min="<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" />" name="orderDate" required/>
        <br/>Time:
        <input type="time" name="orderTime"
               min="09:00" max="18:00" required />
    </label>
    <br/>
    Choose services to be included in your order:
    <br/>
    <c:forEach var="activity" items="${activityList}">
        <label><input type="checkbox" name="activityId" value="${activity.id}">
                ${activity.name}<br></label>
        <br/>
    </c:forEach>

    <br/>
    <input type="submit" value="Go to check"/>
    <c:if test="${emptyActivityList == true}">
        Please make sure that service list is not empty.
    </c:if>
</form>
</body>
</html>

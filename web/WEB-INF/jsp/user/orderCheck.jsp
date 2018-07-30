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

</head>
<body>


<c:if test="${empty sessionScope.user && sessionScope.user.status == 'banned'}">
    <jsp:forward page="${pageContext.request.contextPath}/home"/>
</c:if>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<form name="orderCheckForm" method="POST" action="app">
    <input type="hidden" name="command" value="addOrder"/>
   Order info:<br/>

    Time: ${sessionScope.order.dateTime}
    <input type="hidden" name="orderTime" value="${sessionScope.order.dateTime}"/>
    <br/>
    Services:<br/>
    <table>
    <c:forEach var="activity" items="${sessionScope.order.activityList}">
        <tr>
            <input type="hidden" name="activityId" value="${activity.id}"/>
                <td>${activity.name}</td>
                <td>${activity.description}</td>
                <td>${activity.price}</td>
        </tr>
    </c:forEach>
    </table>
    <br/>
    Price: ${sessionScope.order.price}

    <br/>
    <input type="submit" value="Confirm order"/>
</form>
</body>
</html>

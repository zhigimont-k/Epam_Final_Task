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
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>

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
    <fmt:message bundle="${locale}" key="locale.common.button.update" var="button"/>

    <fmt:message bundle="${locale}" key="locale.basic.projectname" var="projectName"/>
    <title>${pageTitle} | ${projectName}</title>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>

<div>
    <input type="hidden" name="activityId" value="${activity.id}"/>
    Activity id: ${activity.id}
    <br/>
    <form name="activityEditForm" method="POST" action="app">
        <input type="hidden" name="command" value="updateActivity"/>
        ${serviceNameLabel}: ${activity.name}
        <br/>
        ${serviceDescriptionLabel}: ${activity.description}
        <br/>
        ${servicePriceLabel}: ${activity.price}
        <br/>
        Status: ${activity.status}
        <br/>
        <c:forEach var="review" items="${reviewList}">
                <form name="reviewListForm" method="POST" action="app">
                    <input type="hidden" name="command" value="editReview"/>
                    <input type="hidden" name="reviewId" value="${review.id}"/>
                    ${review.userLogin}
                    <fmt:formatDate value="${review.creationDate}" type="both" dateStyle="short" timeStyle="short" />
                    ${review.mark}
                    ${review.message}
                    <c:if test="${sessionScope.user.status == 'admin'}">
                        <a href="app?command=deleteReview&reviewId=${review.id}">delete</a>
                    </c:if>
                    <c:if test="${sessionScope.user.id == review.userId}">
                        <a href="app?command=editReview&reviewId=${review.id}">edit</a>
                    </c:if>
                </form>
        </c:forEach>
    </form>
    <c:if test="${sessionScope.user.status == 'admin' || sessionScope.user.status == 'user'}">
        <jsp:include page="/WEB-INF/jsp/user/addReview.jsp"/>
    </c:if>
</div>
</body>
</html>

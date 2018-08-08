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
    <title>${activity.name} | ${projectName}</title>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>

<div>
    <div class="row">
        <div class="column" style="width:30%;">
            <input type="hidden" name="activityId" value="${activity.id}"/>
            <br/>
            ${serviceNameLabel}: ${activity.name}
            <br/>
            ${serviceDescriptionLabel}: ${activity.description}
            <br/>
            ${servicePriceLabel}: ${activity.price}
            <br/>
            Status: ${activity.status}
            <br/><br/>
            <c:if test="${sessionScope.user.status == 'admin' || sessionScope.user.status == 'user'}">
                <jsp:include page="/WEB-INF/jsp/user/addReview.jsp"/>
            </c:if>
        </div>
        <div class="column" style="width:20%;"><br/></div>
        <div class="column">
            <c:forEach var="review" items="${reviewList}">
                <form name="reviewListForm" method="POST" action="app">
                    <input type="hidden" name="command" value="editReview"/>
                    <input type="hidden" name="reviewId" value="${review.id}"/>
                    <img src="image?userId=${review.userId}" height="100px"
                         onerror="this.style.display='none'"/>
                        ${review.userLogin} -
                    <fmt:formatDate value="${review.creationDate}" type="both" dateStyle="short" timeStyle="short"/>
                    <br/>${review.mark}
                    <br/>${review.message}
                    <c:if test="${sessionScope.user.status == 'admin'}">
                        <br/><a href="app?command=deleteReview&reviewId=${review.id}">delete</a>
                    </c:if>
                    <c:if test="${sessionScope.user.id == review.userId}">
                        <br/><a href="app?command=editReview&reviewId=${review.id}">edit</a>
                    </c:if>
                </form>
            </c:forEach>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>

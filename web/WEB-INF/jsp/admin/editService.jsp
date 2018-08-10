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

    <fmt:message bundle="${locale}" key="locale.page.title.services" var="pageTitle"/>

    <fmt:message bundle="${locale}" key="locale.service.label.name" var="serviceNameLabel"/>
    <fmt:message bundle="${locale}" key="locale.service.label.description" var="serviceDescriptionLabel"/>
    <fmt:message bundle="${locale}" key="locale.service.label.price" var="servicePriceLabel"/>
    <fmt:message bundle="${locale}" key="locale.common.button.update" var="button"/>

    <fmt:message bundle="${locale}" key="locale.basic.projectname" var="projectName"/>
    <title>${pageTitle} | ${projectName}</title>
</head>
<body>
<c:if test="${sessionScope.user.status ne 'admin' || empty sessionScope.activity}">
    <jsp:forward page="${pageContext.request.contextPath}/home"/>
</c:if>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>

<div>
    <form name="activityEditForm" method="POST" action="app">
        <input type="hidden" name="command" value="updateActivity"/>
        <input type="hidden" name="activityId" value="${sessionScope.activity.id}"/>
        <label>${serviceNameLabel}
            <br/>
            <input type="text" value="${sessionScope.activity.name}" name="activityName"
                   maxlength="20" minlength="4" required/>
        </label>
        <br/>
        <label>${serviceDescriptionLabel}
            <br/>
            <textarea name="activityDescription" cols="30" rows="10" required>
                ${sessionScope.activity.description}
            </textarea>
        </label>
        <br/>
        <label>${servicePriceLabel}
            <br/>
            <input type="text" value="${sessionScope.activity.price}"
                   name="activityPrice" maxlength="5" minlength="1" required/>
        </label>

        Status:
        <select name="activityStatus">
            <option value="available">available</option>
            <option value="unavailable">unavailable</option>
        </select>

        <br/>
        <input type="submit" value="${button}"/>
        <c:if test="${sessionScope.dataExists == true}">
            <div class="alert alert-danger alert-dismissible">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <strong>Fail!</strong> Activity with this name already exists.
            </div>
        </c:if>
    </form>
</div>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>

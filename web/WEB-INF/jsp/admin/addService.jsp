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
    <script src="${pageContext.request.contextPath}/js/support/jquery-3.3.1.min.js"></script>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:setBundle basename="cbb_info" var="projectInfo"/>

    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>
    <fmt:message bundle="${locale}" key="locale.action.add.service" var="addService"/>
    <fmt:message bundle="${locale}" key="locale.table.name" var="serviceNameLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.description" var="serviceDescriptionLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.price" var="servicePriceLabel"/>
    <fmt:message bundle="${locale}" key="locale.action.add" var="button"/>

    <fmt:message bundle="${locale}" key="locale.message.serviceexists" var="serviceExists"/>

    <title>${addService} | ${projectName}</title>
</head>
<body>

<c:if test="${sessionScope.user.status ne 'admin'}">
    <jsp:forward page="${pageContext.request.contextPath}/home"/>
</c:if>
<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>

<div class="container">
    <div class="row centered-form center-block">
        <div class="container col-md-4 col-md-offset-6">
            <h3>${addService}:</h3>
            <form name="addServiceForm" method="POST" action="app">
                <input type="hidden" name="command" value="addActivity"/>
                <div class="form-group">
                    <label>${serviceNameLabel}:
                        <br/>
                        <input type="text" name="activityName"
                               maxlength="40"
                               minlength="2"
                               pattern="[\p{L}\s]{2,40}" required/>
                    </label>
                </div>
                <div class="form-group">
                    <label>${serviceDescriptionLabel}:
                        <br/>
                        <textarea name="activityDescription"
                                  maxlength="280"
                                  cols="30"
                                  rows="10"
                                  class="form-control noresize"
                                  required></textarea>
                    </label>
                </div>
                <div class="form-group">
                    <label>${servicePriceLabel}:
                        <br/>
                        <input type="text"
                               name="activityPrice"
                               maxlength="10"
                               minlength="1"
                               pattern="\d{1,10}"
                               required/>
                    </label>
                </div>
                <button type="submit" class="btn btn-default">${button}</button>
            </form>
            <c:if test="${dataExists == true}">
                <div class="alert alert-danger alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>${serviceExists}</strong>
                </div>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>

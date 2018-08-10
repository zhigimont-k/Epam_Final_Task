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

    <fmt:message bundle="${locale}" key="locale.page.title.auth" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.user.label.login" var="loginLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.password" var="passwordLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.showPassword" var="showPassword"/>
    <fmt:message bundle="${locale}" key="locale.user.button.signin" var="button"/>

    <fmt:message bundle="${locale}" key="locale.user.text.noAccountYet" var="toRegister"/>
    <fmt:message bundle="${locale}" key="locale.user.button.signup" var="signUp"/>

    <fmt:message bundle="${locale}" key="locale.user.warning.auth.fail" var="authFailMessage"/>

    <fmt:message bundle="${locale}" key="locale.basic.projectname" var="projectName"/>
    <title>${pageTitle} | ${projectName}</title>
    <script type="text/javascript" src="../js/inputScript.js"></script>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<c:if test="${not empty sessionScope.user}">
    <jsp:forward page="${pageContext.request.contextPath}/home"/>
</c:if>
<div class="container">
    <div class="row centered-form center-block">
        <div class="container col-md-4 col-md-offset-6">
            <form name="loginForm" method="POST" action="app">
                <input type="hidden" name="command" value="login"/>
                <div class="form-group">
                    <label>${loginLabel}:
                        <br/>
                        <input type="text"
                               name="login"
                               maxlength="40"
                               pattern="\w+{4, 40}"
                               data-toggle="tooltip" title="hi"
                               required/>
                    </label>
                </div>
                <div class="form-group">
                    <label>${passwordLabel}:
                        <br/>
                        <input type="password" name="password" id="passwordField" maxlength="32" required/>
                    </label>
                    <br/>
                    <label><input type="checkbox" onclick="togglePasswordVisibility()">${showPassword}</label>
                </div>
                <button type="submit" class="btn btn-default">${button}</button>
            </form>
            <c:if test="${authFail == true}">
                <div class="alert alert-danger alert-dismissible">
                        ${authFailMessage}
                </div>
            </c:if>
            <p>${toRegister} <a href="${pageContext.request.contextPath}/register">${signUp}</a></p>
            <p><a href="${pageContext.request.contextPath}/resetPassword">Forgot your password?</a></p>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>

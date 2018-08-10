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

    <fmt:message bundle="${locale}" key="locale.page.title.account" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.user.label.login" var="loginLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.password.old" var="oldPasswordLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.password.new" var="newPasswordLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.showPassword" var="showPassword"/>
    <fmt:message bundle="${locale}" key="locale.user.label.username" var="userNameLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.email" var="emailLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.phonenumber" var="phoneNumberLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.label.status" var="statusLabel"/>
    <fmt:message bundle="${locale}" key="locale.user.role.admin" var="admin"/>
    <fmt:message bundle="${locale}" key="locale.user.role.user" var="user"/>
    <fmt:message bundle="${locale}" key="locale.user.role.banned" var="banned"/>

    <fmt:message bundle="${locale}" key="locale.user.warning.auth.fail" var="authFailMessage"/>

    <fmt:message bundle="${locale}" key="locale.common.button.update" var="button"/>

    <fmt:message bundle="${locale}" key="locale.basic.projectname" var="projectName"/>
    <script type="text/javascript" src="../../../js/inputScript.js"></script>

    <title>${pageTitle} | ${projectName}</title>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<c:if test="${empty sessionScope.user}">
    <jsp:forward page="${pageContext.request.contextPath}/login"/>
</c:if>

<div class="col-lg-2"><br/></div>
<div class="col-lg-4">
    <form name="updateUserForm" method="POST" action="app">
        <input type="hidden" name="command" value="updateUser"/>
        ${loginLabel}: ${sessionScope.user.login}
        <br/>
        <label>${passwordLabel}
            <br/>
            <input type="password" name="password" id="passwordField" maxlength="32"
                   pattern="[\w^_]{6,32}" required/></label>
        <label><input type="checkbox" onclick="togglePasswordVisibility()">${showPassword}</label>
        <br/>

        <label>${newPasswordLabel}:
            <br/>
            <input type="password" name="newPassword" id="newPasswordField" maxlength="32"
                   pattern="[\w^_]{6,32}"/></label>
        <label><input type="checkbox" onclick="toggleNewPasswordVisibility()">${showPassword}</label>
        <br/>
        <br/>
        ${emailLabel}: ${sessionScope.user.email}
        <br/>
        ${phoneNumberLabel}: ${sessionScope.user.phoneNumber}
        <br/>
        Card number: ${sessionScope.user.cardNumber}
        <br/>
        Money on card: ${money}
        <br/>
        <label>${userNameLabel}
            <input type="text" name="userName" minlength="2" maxlength="40"
                   value="${sessionScope.user.userName}"
                   pattern="[\p{L}\s]{2,40}"/></label>
        <br/>
        <br/>
        <input type="submit" value="${button}"/>
        <br/>
        <c:if test="${sessionScope.authFail == true}">
            ${authFailMessage}
        </c:if>
        <c:if test="${sessionScope.illegalInput == true}">
            Please check if your input is correct
        </c:if>
    </form>
</div>
<div class="col-lg-4">
    <img src="image?userId=${sessionScope.user.id}" height="200px"
         onerror="this.style.display='none'"/>
    <br/>
    <form name="updateUserForm" method="POST" action="image" enctype="multipart/form-data">
        <input type="hidden" name="userId" value="${sessionScope.user.id}"/>
        <input type="file" name="photo" size="50" placeholder="Upload Your Image" required/><br><br>
        <input type="submit" value="Save">
    </form>
</div>
<div class="col-lg-2"><br/></div>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>

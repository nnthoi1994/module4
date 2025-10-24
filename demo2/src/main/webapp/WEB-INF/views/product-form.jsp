
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title>Product Form</title>
</head>
<body>
<h1>Product Form</h1>
<form:form modelAttribute="product" method="post" action="${pageContext.request.contextPath}/products/save">
    <form:hidden path="id"/>
    <table>
        <tr><td>Code:</td><td><form:input path="code"/></td></tr>
        <tr><td>Name:</td><td><form:input path="name"/></td></tr>
        <tr><td>Price:</td><td><form:input path="price"/></td></tr>
        <tr><td colspan="2"><input type="submit" value="Save"/></td></tr>
    </table>
</form:form>
<a href="${pageContext.request.contextPath}/products">Back to list</a>
</body>
</html>

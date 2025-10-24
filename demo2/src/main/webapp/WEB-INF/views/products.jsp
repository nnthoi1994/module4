
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Products</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/display/style.jsp">
</head>
<body>
<h1>Products</h1>
<a href="${pageContext.request.contextPath}/products/create">Create new product</a>
<table border="1" cellpadding="6" cellspacing="0">
    <tr><th>ID</th><th>Code</th><th>Name</th><th>Price</th><th>Actions</th></tr>
    <c:forEach items="${products}" var="p">
        <tr>
            <td>${p.id}</td>
            <td>${p.code}</td>
            <td>${p.name}</td>
            <td>${p.price}</td>
            <td>
                <a href="${pageContext.request.contextPath}/products/edit/${p.id}">Edit</a> |
                <a href="${pageContext.request.contextPath}/products/delete/${p.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

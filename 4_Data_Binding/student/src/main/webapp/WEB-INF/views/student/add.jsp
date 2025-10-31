<%--
  Created by IntelliJ IDEA.
  User: Home
  Date: 10/28/2025
  Time: 9:39 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>Thêm mới</h2>
<form:form action="/students/add" modelAttribute="student" method="post">
    ID:
    <form:input path="id"/><br>
    Name:
    <form:input path="name"/><br>
    Gender:
    <form:radiobutton path="gender" value="1"/>Male
    <form:radiobutton path="gender" value="0"/> Female<br>
    Subject:
    <form:checkboxes path="subjects" items="${subjects}"/><br>
    <form:select path="className">
        <form:option value="C06">C06</form:option>
        <form:option value="C07">C07</form:option>
        <form:option value="C07">C08</form:option>
    </form:select><br>
    <form:button>Lưu</form:button>
</form:form>

</body>
</html>
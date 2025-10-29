<%--
  Created by IntelliJ IDEA.
  User: Home
  Date: 10/27/2025
  Time: 3:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Danh sách sinh viên</h1>
<h4 style="color: red">${mess}</h4>
<table border="1">
    <tr>
        <th>STT</th>
        <th>ID</th>
        <th>Name</th>
        <th>Gender</th>
        <th>Subjects</th>
        <th>Class name</th>
        <th>Detail-RP</th>
        <th>Detail-PV</th>
    </tr>
    <c:forEach var="student" items="${studentList}" varStatus="status">
        <tr>
            <td>${status.count}</td>
            <td>${student.id}</td>
            <td>${student.name}</td>
            <td>${student.gender}</td>
            <td>
                <c:forEach var="sub" items="${student.subjects}">
                    <span>${sub}</span>
                </c:forEach>
            </td>
            <td>${student.className}</td>
            <td><a href="students/detail?id=${student.id}">Detail1</a></td>
            <td><a href="students/detail/${student.id}">Detail2</a></td>
        </tr>
    </c:forEach>

</table>
</body>
</html>
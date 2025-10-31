<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Settings</title>
    <style>
        body { font-family: sans-serif; padding: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], select, textarea { width: 300px; padding: 8px; border: 1px solid #ccc; border-radius: 4px; }
        textarea { height: 80px; }
        .btn { padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; }
        .btn-primary { background-color: #007bff; color: white; }
        .btn-secondary { background-color: #6c757d; color: white; }
    </style>
</head>
<body>

<h1>Settings</h1>

<form:form modelAttribute="settings" action="update-settings" method="post">
    <div class="form-group">
        <label>Languages</label>
        <form:select path="language" items="${languages}" />
    </div>

    <div class="form-group">
        <label>Page Size:</label>
        Show <form:select path="pageSize" items="${pageSizes}" /> emails per page
    </div>

    <div class="form-group">
        <label>Spams filter:</label>
        <form:checkbox path="spamsFilter" /> Enable spams filter
    </div>

    <div class="form-group">
        <label>Signature:</label>
        <form:textarea path="signature" />
    </div>

    <button type="submit" class="btn btn-primary">Update</button>
    <button type="button" class="btn btn-secondary">Cancel</button>
</form:form>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update Result</title>
    <style>
        body { font-family: sans-serif; padding: 20px; }
        .info { border: 1px solid #ccc; padding: 15px; border-radius: 5px; background-color: #f9f9f9; width: 400px; }
        p { margin: 10px 0; }
        a { text-decoration: none; }
    </style>
</head>
<body>

<h2>${message}</h2>

<div class="info">
    <h3>Current Settings:</h3>
    <p><strong>Language:</strong> ${updatedSettings.language}</p>
    <p><strong>Page Size:</strong> ${updatedSettings.pageSize}</p>
    <p><strong>Spams Filter:</strong> ${updatedSettings.spamsFilter ? "Enabled" : "Disabled"}</p>
    <p><strong>Signature:</strong> <br> <pre>${updatedSettings.signature}</pre></p>
</div>
<br>
<a href="settings">Back to Settings</a>

</body>
</html>
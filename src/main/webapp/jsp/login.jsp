<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Pahana Edu</title>
</head>
<body>
<h2>Login</h2>

<form action="login" method="post">
    <label>Email:</label>
    <input type="email" name="email" placeholder="Enter your email" required /><br><br>

    <label>Password:</label>
    <input type="password" name="password" placeholder="Enter your password" required /><br><br>

    <button type="submit">Login</button>
</form>

<!-- Show error message if login failed -->
<c:if test="${not empty error}">
    <p style="color:red;">${error}</p>
</c:if>
</body>
</html>

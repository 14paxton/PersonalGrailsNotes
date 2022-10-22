<html>
<head>
<meta name="layout" content="main"/>
</head>
<body>
    <div class="content">
        <g:form action="renderMessage" name="renderMessage">
            Enter Message: <g:textField name="msg" value="${msg}"/>
            <g:submitButton name="render" value="Render Message"/>
        </g:form>
    </div>
</body>
</html>
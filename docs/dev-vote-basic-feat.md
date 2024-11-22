# Notes

## 1. Put CSRF into Headers in Spring 4.0.3 + Spring Security 3.2.3 + Thymeleaf 2.1.2

```html
<!DOCTYPE html>

<html xmlns:th="http://www.thymeleaf.org">

<head>
    <meta name="_csrf" th:content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" th:content="${_csrf.headerName}"/>
    <title>Fileupload Test</title>
</head>
<body>

<p th:text="${msg}"></p>

<form action="#" th:action="@{/fileUpload}" method="post" enctype="multipart/form-data">
    <input type="file" name="myFile"/>
    <input type="submit"/>
</form>

</body>
</html>
```

## 2. Format datetime in javascript

https://blog.csdn.net/qq_38933412/article/details/82879127

```javascript
const formattedTime = moment().format('YYYYMMDDHHmmss');

let endTime = new Date()
endTime.setHours(Number($('#hour').val()))
endTime.setMinutes(Number($('#minute').val()))
moment(endTime).format('yyyy-MM-DD HH:mm:ss')
```

## 3. Sync git remote branches

```(shell)
git remote show origin
git remote prune origin
```
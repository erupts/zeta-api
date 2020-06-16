<html>
<head>
    <title>api文档</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<table border="1">
    <tr>
        <th>key</th>
        <th>缓存时间</th>
        <th>请求地址</th>
        <th>描述</th>
    </tr>
    <#list elements as ele>
        <tr>
            <td>${ele.name}</td>
            <td>${ele.attributeValue('cache')!''}</td>
            <td>${domain + ele.name}</td>
            <td>${ele}</td>
        </tr>
    </#list>
</table>
</body>
</html>
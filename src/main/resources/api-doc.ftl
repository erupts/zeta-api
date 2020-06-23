<html>
<head>
    <title>api document</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="/bootstrap.min.css">
    <style>
        body {
            background: #454d55;
        }
    </style>
</head>
<body>
<table class="table table-hover table-bordered table-dark">
    <#if root.attributeValue('desc')??>
        <tr>
            <th colspan="10">
                ${root.attributeValue('desc')}
            </th>
        </tr>
    </#if>
    <tr>
        <th colspan="10">
            <span class="badge badge-primary">POST</span> &nbsp; (Content-Type: <span
                    class="text-warning">application/json</span> )
        </th>
    </tr>
    <tr>
        <th>接口名称 (title)</th>
        <th>请求地址</th>
        <th>描述 (desc)</th>
        <th>缓存时间 (cache)</th>
        <th>操作</th>
    </tr>
    <#list root.elements() as ele>
        <tr>
            <td>${ele.attributeValue('title')!ele.name}</td>
            <td style="color: #3462bf">${domain}/open-api/sql/${ele.attributeValue('type')!'query'}/${fileName}/${ele.name}</td>
            <td>${ele.attributeValue('desc')!''}</td>
            <td>${ele.attributeValue('cache')!'0'}</td>
            <#--<td><button class="btn btn-primary btn-sm">测试</button></td>-->
        </tr>
    </#list>
</table>
</body>
</html>
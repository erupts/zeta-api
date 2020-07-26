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
        <th style="width: 100px;">标记</th>
        <th>操作</th>
    </tr>
    <#list root.elements() as ele>
        <tr>
            <td>${ele.attributeValue('title')!ele.name}</td>
            <td>
                <a target="_blank" href="${domain}/zeta-api/sql/${fileName}/${ele.name}">
                    ${domain}/zeta-api/sql/${fileName}/${ele.name}
                </a>
            </td>
            <td>${ele.attributeValue('desc')!''}</td>
            <td>${ele.attributeValue('cache')!'0'}</td>
            <td>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="checkbox" id="${ele.name}" value="option1">
                    <label class="form-check-label" for="${ele.name}">标记</label>
                </div>
            </td>
            <#--<td><button class="btn btn-primary btn-sm">测试</button></td>-->
        </tr>
    </#list>
</table>
</body>
</html>
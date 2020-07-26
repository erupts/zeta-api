## 使用方法
1. 创建spring boot项目
2. 指定数据库连接信息
3. 引入zeta-api
4. 在资源目录下创建epi文件夹
5. 在epi目录下创建xml文件
6. xml文件demo
```
<?xml version="1.0" encoding="utf-8" ?>
<sql desc="测试接口">
    <hello-world title="基础使用">
        select 'hello world' $key
    </hello-world>
    <cache cache="5000" title="使用缓存(cache单位毫秒)">
        select 'cache',now() now
    </cache>
    <param title="参数获取与处理">
        select :param param
    </param>
    <condition title="条件处理">
        select
        <if test="param&gt;10">
            'gt 10' param_status
        </if>
        <if test="param&lt;10">
            'lt 10' param_status
        </if>
        <if test="param==10">
            'eq 10' param_status
        </if>
    </condition>
    <insert title="插入数据" type="modify">
        insert into demo(content) values (:content)
    </insert>
</sql>
```

## api文档：  
http://localhost:8080/zeta-api/doc/$demo.html
####文档路径格式
http://{host}:{port}/zeta-api/doc/{文件名}.html

## 请求已定义接口
根据文档中的提示使用postman等工具请求即可，请求类型post,Content-Type: application/json
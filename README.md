原理繁琐的三层架构，化繁为简，简单sql语句轻松搞定http接口开发

## 框架特性
1. 支持缓存，在标签中添加cache属性即可，缓存实现方式caffeine，也可通过实现xyz.erupt.zeta_api.handler.ZetaCache接口，来自定义缓存实现
2. 支持条件判断，仅需在标签中使用if标签即可，if标签test属性支持javaScript表达式
3. handler访问拦截，可通过
4. Api文档动态生成，修改xml标签内容，即可展示接口文档信息

## 使用方法
1. 创建spring boot项目
2. 指定数据库连接信息
3. 加入zeta-api依赖
4. 在资源目录下创建epi文件夹
5. 在epi目录下创建xml文件
6. xml文件示例
```
<?xml version="1.0" encoding="utf-8" ?>
<zeta desc="zeta接口示例">
    <hello-world title="基础使用">
        select 'hello world' $key
    </hello-world>
    <cache cache="5000" title="使用缓存(cache单位毫秒)">
        select 'cache',now() now
    </cache>
    <params title="参数获取与处理">
        <param key="param" default="hellow zeta"/>
        select :param param
    </params>
    <condition title="条件处理">
        <param key="param" default=""/>
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
    <insert title="插入数据">
        <param key="content" default=""/>
        insert into demo(content) values (:content)
    </insert>
</zeta>
```
7. 启动项目
8. 访问文档,查看已创建接口
文档路径：http://${host}/zeta-api/doc/${xml文件名}.html


## application.yml配置项说明
```
zeta-api:
  #是否热读取xml配置，生产环境不要开启此功能
  hotReadXml: true
  #是否开始缓存功能
  enableCache: true
  #是否开启Api文档查询功能
  enableApiDoc: true
  #自定义缓存实现，需实现xyz.erupt.zeta_api.handler.ZetaCache接口
  #cacheHandlerPath: xxx
  #访问白名单，不填表示不建立
  ipWhite:
    - 127.0.0.1
    - 192.168.1.1
    - 192.168.1.187
```

## 接口请求示例
```
// jquery为例
$.ajax({
    type: "POST",
    url: "/zeta-api/sql/xxxxx",
    contentType: "application/json",    //必须有
    data: JSON.stringify({ param: 10}), //必须使用JSON.stringify
    success: function (res) {
        alert(res);
    }
});
```




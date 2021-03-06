# ZetaAPI
[![star](https://gitee.com/erupt/zeta-api/badge/star.svg?theme=dark)](https://gitee.com/erupt/zeta-api)
[![GitHub stars](https://img.shields.io/github/stars/erupts/zeta-api?style=social)](https://github.com/erupts/zeta-api)

## 项目介绍
SQL + XML快速创建Api接口与使用文档，开发速度快如闪电！

## 框架特性
1. 配置简单功能丰富
2. 使用 xml 标签属性就可支持缓存，默认缓存实现为 caffeine，也可自定义缓存实现
3. 支持 if 标签分支判断，处理各种复杂场景
4. 自定义访问拦截，可创建拦截器修改表达式与返回结果
5. 根据 XML 配置，动态生成 Api 文档，支持在线测试
6. 支持xml热更新，无需重启容器，即可读取最新xml配置
7. 支持link标签，单次请求，执行多条SQL语句
8. 支持ip白名单配置

## 使用方法
1. 创建spring boot项目
2. 导入依赖
```xml
<dependency>
    <groupId>xyz.erupt</groupId>
    <artifactId>zeta-api</artifactId>
    <version>0.5.5</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```
3. 配置数据库连接与数据库驱动
4. 创建xml配置文件：/resources/epi/xxx.xml
5. xml文件示范例
``` xml
<?xml version="1.0" encoding="utf-8" ?>
<zeta desc="zeta接口示例">
    <hello-world title="基础使用">
        select 'hello world' key
    </hello-world>
    <cache title="使用缓存(cache单位毫秒)" cache="5000">
        select 'cache' cache,now() now
    </cache>
    <params title="参数获取与处理">
        <param key="param" default="hello zeta"/>
        select :param param
    </params>
    <condition title="条件处理">
        <param key="param" title="数值参数"/>
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
        <param key="content" title="内容"/>
        insert into demo(content) values (:content)
    </insert>
    <links title="同时执行多条SQL">
        select 'hi' hi
        <link id="now">
            select now() now;
        </link>
        <link id="second">
            select '第二条SQL执行结果' result;
        </link>
    </links>
</zeta>
```
启动项目，查看接口文档：http://localhost:8080/zeta-doc/xml文件名.html

演示文档：http://localhost:8080/zeta-doc/$demo.html
  
![img](img/p1.png)
![img](img/p2.png)
![img](img/p3.png)

## application.yml配置项说明
``` yaml
zeta-api:
  #是否热读取xml配置，生产环境不要开启此功能
  hotReadXml: true
  #是否开始缓存功能
  enableCache: true
  #是否开启Api文档查询功能
  enableApiDoc: true
  #自定义缓存实现，需实现xyz.erupt.zeta_api.handler.ZetaCache接口
  cacheHandlerPath: xyz.erupt.xxxx
  #是否打印sql语句
  showSql: true
  #访问白名单，空表示不对ip进行鉴权
  ipWhite:
    - 127.0.0.1
    - 192.168.1.1
    - 192.168.1.187
```

## 接口请求示例
``` javascript
// jquery为例
$.ajax({
    type: "POST",
    url: "/zeta-api/sql/{file}/{name}",
    contentType: "application/json",    //请求内容需要为json
    data: JSON.stringify({ param: 10}), //必须使用JSON.stringify
    success: function (res) {
        alert(res);
    }
});
```

## 项目推荐
Erupt Framework 通用数据管理框架  
仓库地址：https://github.com/erupts/erupt  
官网地址：https://www.erupt.xyz


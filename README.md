# zeta-api

[![star](https://gitee.com/erupt/zeta-api/badge/star.svg?theme=dark)](https://gitee.com/erupt/zeta-api)
[![GitHub stars](https://img.shields.io/github/stars/erupts/zeta-api?style=social)](https://github.com/erupts/zeta-api)

## 项目介绍
配置xml标签快速创建http接口

## 框架特性
1. 支持caffeine缓存，也可自定义缓存策略
2. 支持if条件判断，且if判断支持Js脚本
3. 支持访问拦截，可通过拦截器修改表达式与返回结果
4. 动态生成Api文档，可快捷查看与测试接口

## 使用方法
1. 创建spring boot项目
2. 指定数据库连接信息
3. 添加zeta-api依赖
4. 入口类设置注解扫描路径
@SpringBootApplication(scanBasePackages = "xyz.erupt")
5. 在resources目录下创建epi文件夹
6. 在epi目录下创建xml格式文件
7. xml文件示范例
``` xml
<?xml version="1.0" encoding="utf-8" ?>
<zeta desc="zeta接口示例">
    <hello-world title="基础使用">
        select 'hello world' $key
    </hello-world>

    <cache cache="5000" title="使用缓存(cache单位毫秒)">
        select 'cache',now() now
    </cache>

    <params title="参数获取与处理">
        <param key="param" default="hellow zeta" title="返回值"/>
        select :param param
    </params>

    <condition title="条件处理">
        <param key="param" default="" title="数值"/>
        select
        <if test="param > 10">
            'gt 10' param_status
        </if>
        <if test="param < 10">
            'lt 10' param_status
        </if>
        <if test="param == 10">
            'eq 10' param_status
        </if>
    </condition>

    <insert title="插入数据">
        <param key="content" title="待插入数据"/>
        insert into demo(content) values (:content)
    </insert>
</zeta>
```
8. 启动项目
9. 访问接口文档,查看已创建接口
文档路径：http://${host}/zeta-doc/${xml文件名}.html


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
  #cacheHandlerPath: xxx
  #是否打印sql语句
  showSql: true
  #访问白名单，不填表示不建立
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
    url: "/zeta-api/sql/xxxxx",
    contentType: "application/json",    //必须有
    data: JSON.stringify({ param: 10}), //必须使用JSON.stringify
    success: function (res) {
        alert(res);
    }
});
```




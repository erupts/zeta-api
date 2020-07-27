<html>
<head>
    <title>api document</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="/bootstrap.min.css">
    <style>
        * {
            font-family: "Courier New";
        }

        body {
            background: #454d55;
        }

        .response-block {
            background: #000;
            color: #fff;
            padding: 10px;
            border-radius: .25rem;
            /*white-space: pre-wrap;*/
            /*word-wrap: break-word;*/
        }

        table td {
            vertical-align: middle !important;;
        }
    </style>
</head>
<body>
<div id="main">
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
                <span class="badge badge-primary">POST</span>&nbsp;( Content-Type: <span
                        class="text-warning">application/json</span> )
            </th>
        </tr>
        <tr>
            <th>#</th>
            <th>接口名称 (title)</th>
            <th>请求地址</th>
            <#--<th>参数</th>-->
            <th>描述 (desc)</th>
            <th>缓存时间 (cache)</th>
            <th class="text-center" style="width: 100px;">操作</th>
        </tr>
        <#list root.elements() as ele>
            <#assign path="${domain}/zeta-api/sql/${fileName}/${ele.name}"/>
            <tr>
                <td>${ele_index+1}</td>
                <td>${ele.attributeValue('title')!ele.name}</td>
                <td>
                    <a target="_blank" href="${path}">
                        ${path}
                    </a>
                </td>
                <#--<td>${ele.attributeValue('body')!''}</td>-->
                <td>${ele.attributeValue('desc')!''}</td>
                <td class="text-center">${ele.attributeValue('cache')!'0'}</td>
                <td class="text-center">
                    <#if ele.attributeValue('body')??>
                        <button class="btn btn-outline-warning btn-sm" @click="req({
                    body: ${ele.attributeValue('body')},
                    path:'${path}'
                    })">
                            Test
                        </button>
                    </#if>
                    <#--<button class="btn btn-primary btn-sm" @click="req({-->
                    <#--body: ${ele.attributeValue('body')!'null'},-->
                    <#--path:'${path}'-->
                    <#--})">-->
                    <#--<svg class="bi bi-lightning" width="1em" height="1em" viewBox="0 0 16 16"-->
                    <#--fill="currentColor"-->
                    <#--xmlns="http://www.w3.org/2000/svg">-->
                    <#--<path fill-rule="evenodd"-->
                    <#--d="M11.251.068a.5.5 0 0 1 .227.58L9.677 6.5H13a.5.5 0 0 1 .364.843l-8 8.5a.5.5 0 0 1-.842-.49L6.323 9.5H3a.5.5 0 0 1-.364-.843l8-8.5a.5.5 0 0 1 .615-.09zM4.157 8.5H7a.5.5 0 0 1 .478.647L6.11 13.59l5.732-6.09H9a.5.5 0 0 1-.478-.647L9.89 2.41 4.157 8.5z"/>-->
                    <#--</svg>-->
                    <#--Test-->
                    <#--</button>-->
                </td>
            </tr>
        </#list>
    </table>

    <!-- Modal -->
    <div class="modal fade" id="reqModal" tabindex="-1" role="dialog" aria-labelledby="reqModal" aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel"></h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body" v-if="currApi">
                    <div class="form-group row" v-for="(p,key) in currApi.body">
                        <label for="inputPassword" class="col-sm-2 col-form-label">{{key}}:</label>
                        <div class="col-sm-10">
                            <input class="form-control" v-model="currApi.body[key]">
                        </div>
                    </div>
                    <pre v-if="result" :style="result.success?'color: #fff':'color: #f00'"
                         class="response-block">{{result.content}}</pre>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary btn-sm" @click="send()" :disabled="querying">
                        SEND
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="/jquery.min.js"></script>
<script src="/bootstrap.min.js"></script>
<script src="/vue.min.js"></script>
<script>
    var vue = new Vue({
        el: '#main',
        data: {
            value: true,
            currApi: null,
            result: null,
            querying: false
        },
        methods: {
            req: function (data) {
                this.result = null;
                this.currApi = data;
                $('#reqModal').modal('show')
            },
            send: function () {
                var that = this;
                // for (var key in this.currApi.body) {
                //     if (!this.currApi.body[key]) {
                //         return;
                //     }
                // }
                this.querying = true;
                $.ajax({
                    type: 'POST',
                    url: this.currApi.path,
                    data: JSON.stringify(this.currApi.body),
                    contentType: "application/json",
                    success: function (res) {
                        that.result = null;
                        that.result = {
                            success: true,
                            content: JSON.stringify(res, null, 2)
                        };
                    },
                    error: function (res) {
                        that.result = null;
                        that.result = {
                            success: false,
                            content: JSON.stringify(res, null, 2)
                        };
                    },
                    complete: function (res) {
                        that.querying = false;
                    }
                });
            }
        }
    })
</script>
</body>
</html>
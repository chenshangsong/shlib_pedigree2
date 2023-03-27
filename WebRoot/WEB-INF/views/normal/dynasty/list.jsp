<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/inc/tag.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>华人家谱总目——上海图书馆家谱知识服务平台Beta版</title>
    <c:import url="/WEB-INF/inc/link.jsp" />
    <style>
        .main .panel-body {min-height: 140px;}
        #listView .row{margin-bottom:-20px;}
        #dynastyView .tab-pane{line-height: 30px;padding: 10px 10px 20px;}
    </style>
</head>
<body>
<c:import url="/WEB-INF/inc/header3.jsp">
    <c:param name="menu" value="dynasty"/>
</c:import>
<div class="container main">
    <h2 id="top"><span>上海图书馆家谱朝代分布表</span>
        <a id="btn_back" class="btn btn-default pull-right" style="display: none;"><i class="glyphicon glyphicon-chevron-left"></i>&nbsp;返回家谱朝代分布表</a>
    </h2>
    <div id="listView">
        <br>
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#dynasty_person" role="tab" data-toggle="tab">先祖名人</a></li>
            <li role="presentation"><a href="#dynasty_work" role="tab" data-toggle="tab">家谱文献</a></li>
        </ul>
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane fade in active" id="dynasty_person">
            </div>
            <div role="tabpanel" class="tab-pane fade" id="dynasty_work">
            </div>
        </div>
    </div>
    <div id="dynastyView" style="display:none;">
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#tab_0" role="tab" data-toggle="tab">先祖名人</a></li>
            <li role="presentation"><a href="#tab_1" role="tab" data-toggle="tab">家谱文献</a></li>
        </ul>
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane fade in active" id="tab_0">
                <div id="personDiv">
                    <form id="personForm"><input type="hidden" name="pageSize" value="60"/><input type="hidden" name="uri" /><div tag="data"></div><hr><div tag="pager"></div></form>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane fade" id="tab_1">
                <div id="workDiv">
                    <form id="workForm"><input type="hidden" name="pageSize" value="40"/><input type="hidden" name="uri" /><div tag="data"></div><hr><div tag="pager"></div></form>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/html" id="dynastyTpl">
    <%--{@each dynastys as item}--%>
    <hr>
    <div class="row" uri="@{dynasty.uri}">
        <div class="col-xs-8 h3">
            <a href="@{dynasty.uri}" tag="dynasty">@{dynasty.name}</a>
        </div>
        <%--<div class="col-xs-4"><div class="panel panel-info"><div class="panel-body desc"></div></div></div>--%>
        <div class="col-xs-4"><div class="panel panel-info"><div class="panel-body desc"></div></div></div>
    </div>
    <%--{@/each}--%>
</script>
<script type="text/html" id="personTpl">
    {@each persons as person, i}
    {@if i % 6 == 0}<div class="row">{@/if}
    <div class="col-xs-2">
        <a tag="person" href="@{person.person}">@{person.name}</a>
    </div>
    {@if i % 6 == 5 || i == persons.length - 1}</div>{@/if}
    {@/each}
    {@if persons.length == 0}<div class="row"><div class="col-xs-12">未查询到名人信息！</div></div>{@/if}
</script>
<script type="text/html" id="workTpl">
    {@each works as work, i}
    {@if i % 4 == 0}<div class="row">{@/if}
        <div class="col-xs-3">
            <a tag="work" href="@{work.work}">@{work.title}</a>
        </div>
        {@if i % 4 == 3 || i == works.length - 1}</div>{@/if}
    {@/each}
    {@if works.length == 0}<div class="row"><div class="col-xs-12">未查询到谱籍信息！</div></div>{@/if}
</script>
<c:import url="/WEB-INF/inc/footer.jsp" />
<script src="${ctx}/res/plugin/page.js"></script>
<script src="${ctx}/res/js/normal/dynasty/list.js"></script>
</body>
</html>

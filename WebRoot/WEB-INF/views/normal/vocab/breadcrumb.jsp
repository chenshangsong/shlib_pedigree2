<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container">
    <ol class="breadcrumb">
        <li><a href="${ctx}/view">Home</a></li>
        <li class="dropdown"><a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown">Vocabulary
            <span class="caret"></span>
        </a>
            <ul class="dropdown-menu" role="menu">
                <li role="presentation"><a role="menuitem" tabindex="-1" href="${ctx}/view/model">Model View</a></li>
                <li role="presentation"><a role="menuitem" tabindex="-1" href="${ctx}/view/class">Class View</a></li>
                <li role="presentation"><a role="menuitem" tabindex="-1" href="${ctx}/view/list">List View</a></li>
            </ul>
        </li>
        <li class="active">${param.title}</li>
        <div class="pull-right">
            ${param.toolbar}
            <a href="${ctx}/view/shlgen.rdf" class="btn btn-primary btn-xs" target="_blank">
                <i class="glyphicon glyphicon-share-alt"></i>
                RDF
            </a>
        </div>
    </ol>
</div>
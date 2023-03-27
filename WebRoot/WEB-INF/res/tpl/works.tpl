<div class="panel-group" id="works_accordion" role="tablist" aria-multiselectable="true">
    {@each works as item, i}
    <div class="panel panel-info">
        <div class="panel-heading" role="tab" id="heading_@{i}">
            <h4 class="panel-title">
                <div class="pull-right">【@{item.role}】</div>
               
                <a data-toggle="collapse" style="padding-right: 30px;" data-parent="#works_accordion" href="#collapse_@{i}" aria-expanded="true" aria-controls="collapse_@{i}">
                   <i class="glyphicon glyphicon-resize-vertical"></i>
                  详细
                </a>
                     <i class="glyphicon glyphicon-book"></i>
                     <a style="padding-right: 50px;"  href="${ctx}/service/work/list#uri=@{item.uri}" target="_blank">@{item.dtitle}</a>
                {@each item.places as place}
                    <i class="glyphicon glyphicon-map-marker"></i>
                    <a href="${ctx}/service/place/list#place=@{place.place}" target="_blank">@{place.label}</a>&nbsp;
                {@/each}
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-book"></i>
                <a href='javascript:void(0);' class='dendrogram' check='@{item.uri}'>谱系图</a>
            </h4>
        </div>
        <div id="collapse_@{i}" class="panel-collapse collapse {@if i==0}in{@/if}" role="tabpanel" aria-labelledby="heading_@{i}">
            <div class="panel-body">
                <p>【责任者】 @{item.creator}</p>
                <p>【摘要】&nbsp;&nbsp;&nbsp; @{item.note}</p>
                {@if instances && instances.length > 0}
                <ul class="nav nav-tabs" role="tablist">
                    {@each instances as instance, st}
                    <li role="presentation" class="@{st == 0 ? 'active':''}"><a href="#tab_@{st}" role="tab" data-toggle="tab">@{instance['ab']}</a></li>
                    {@/each}
                </ul>
                <div class="tab-content">
                    {@each instances as instance, st}
                    <div role="tabpanel" class="tab-pane @{st == 0 ? ' fade in active':''}" id="tab_@{st}">
                        纂修时间：@{instance['temporal']}<br>
                        版本：@{instance['edition']}<br>
                        数量：@{instance['extent']}<br>
                        馆藏地：@{instance['org']}（@{instance['place']}）<br>
                    </div>
                    {@/each}
                </div>
                {@/if}
            </div>
        </div>
    </div>
    {@/each}
</div>
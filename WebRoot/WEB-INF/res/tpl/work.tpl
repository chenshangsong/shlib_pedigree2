<ul class="work">
    <li style="line-height: 30px;">
        <span class="h4" style="padding-right: 50px;">
            <a href="${ctx}/service/work/list#uri=@{uri}" target="_blank">@{title}</a>
        </span>
        {@each places as place}
            <a href="${ctx}/service/place/list#place=@{place.place}" target="_blank">@{place.label}</a>&nbsp;
        {@/each}
        <br>
        <p>【责任者】 @{creator}</p>
        <p>【摘要】 &nbsp;&nbsp;&nbsp;@{note}</p>
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
    </li>
</ul>
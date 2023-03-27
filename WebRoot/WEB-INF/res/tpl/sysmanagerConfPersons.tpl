{@each persons as person, i}
{@if i % 6 == 0}<div class="row">{@/if}
    <div class="col-xs-2 person">
        <div class="name">@{person.name}</div>
        <a onClick="aClick(this)" auri="@{person.uri}" aname="@{person.name}" href="javascript:void(0)" ><i class="icon-ok-sign"></i></a>
    </div>
    {@if i % 6 == 5 || i == persons.length - 1}</div>{@/if}
{@/each}
{@if persons.length == 0}<div class="row"><div class="col-xs-12">未查询到相关信息！</div></div>{@/if}
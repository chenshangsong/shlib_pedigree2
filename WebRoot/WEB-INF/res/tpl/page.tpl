<nav class="pull-right">
    <ul class="pagination" pagecount="@{pageCount}"><!--&laquo;&raquo;-->
        <li class="{@if pageth == 1 || pageCount == 1}disabled{@/if}"><a page="prev">上一页</a></li>
        {@each pageths as it, i}
        {@if i > 0 && (pageths[i] - pageths[i - 1]) > 1}
        <li class="disabled"><a>...</a></li>
        {@/if}
        {@if it > 0}
        <li class="{@if pageth == it}active{@/if}"><a page="@{it}">@{it}</a></li>
        {@/if}
        {@/each}
        <li class="{@if pageCount - pageth <= 0}disabled{@/if}"><a page="next">下一页</a></li>
    </ul>
</nav>
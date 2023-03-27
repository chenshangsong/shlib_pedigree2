<div class="table-responsive">
    <table class="table table-bordered">
        <colgroup>
            <col class="col-xs-3">
            <col class="col-xs-2">
            <col class="col-xs-7">
        </colgroup>
        <tr>
            <th>S</th>
            <th>P</th>
            <th>O</th>
        </tr>
        {@each (rdfs||triples) as item}
        <tr>
            <td>@{item.s}</td>
            <td>@{item.p}</td>
            <td>@{item.o}</td>
        </tr>
        {@/each}
    </table>
</div>
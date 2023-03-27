<div style="overflow: auto;">
    <div class="pull-left">
        <img src="../../res/anonymous.png" class="photo thumbnail">
        
    </div>
       <div> <a href="${ctx}/personManager/dataContentlist?id=@{uri}&dataTypeId=5" ><i class="icon-edit"></i></a>
   </a>
   </div>
    <div class="pull-left" style="padding-left:20px;line-height: 35px;">
        @{name}
          {@if courtesyName || orderOfSeniority || pseudonym || posthumousName}（ 
         @{courtesyName?'字：'+courtesyName+'；':''}   
        @{orderOfSeniority?'行：'+orderOfSeniority+'；':''}
        @{pseudonym?'号：'+pseudonym+'；':''}
        @{posthumousName?' 谥号：'+posthumousName:''}     
                       ）{@/if}<br>
          
        朝代：@{dynasty?dynasty:'不详'}<br>
    </div>
</div>
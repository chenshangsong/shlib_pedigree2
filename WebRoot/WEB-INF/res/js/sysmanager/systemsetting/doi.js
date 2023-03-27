$(function() {

	$('#btn_open').click(function() {
		showLoading();
		var doi = $('#doi').val();
		$.ajax({   
		    url:ctx + "/systemSetting/updDoi",   
		     type:'post', 
		     data: {
		    	 doi:doi,
		    	 flg:"0"
		     },
		     async : false, 
		     error:function(){   
		        alert('服务器正忙,请稍候再尝试');
		        hideLoading();
		     },   
		     success:function(data){
		    	 getResult(data);
		    	 hideLoading();
		     }
		 });
	});
	
	$('#btn_close').click(function() {
		showLoading();
		var doi = $('#doi').val();
		$.ajax({   
//		    url:ctx + "/systemSetting/updDoi?doi=" + doi + "&flg=1",
		    url:ctx + "/systemSetting/updDoi",
		     type:'post',  
		     data: {
		    	 doi:doi,
		    	 flg:"1"
		     },
		     async : false, 
		     error:function(){   
		        alert('服务器正忙,请稍候再尝试');
		        hideLoading();
		     },   
		     success:function(data){
		    	 getResult(data);
		    	 hideLoading();
		     }
		 });
	});
	
	function getResult(data) {
		
		var html = "";
        $.each(data, function(i){
            var item = data[i];
            html += "<tr class='odd gradeA'>";
            html += "<td>";
            html += i;
            html += "</td>";
            html += "<td>";
            html += item;
            html += "</td>";
        	html += "</tr>";
        });

        $("#originDiv").html(html);
	}

})

$(function(){
	
	var note = $('#note'),
		ts = new Date(2012, 0, 1),
		newYear = true;
	
	var systime;  
	  
	if(window.name==''){  
	  
		systime = parseInt($('#r1').val());  
	  
	}else{  
	  
		systime = window.name;  
	  
	}  
	
	
	//var  systime = parseInt($('#r1').val());
	
	if((new Date()) > ts){
		// The new year is here! Count towards something else.
		// Notice the *1000 at the end - time must be in milliseconds
		ts = (new Date()).getTime() + systime;
		newYear = false;
	}
		
	$('#countdown').countdown({
		timestamp	: ts,
		callback	: function(days, hours, minutes, seconds){
			
			var message = "";
			
			message += days + " day" + ( days==1 ? '':'s' ) + ", ";
			message += hours + " hour" + ( hours==1 ? '':'s' ) + ", ";
			message += minutes + " minute" + ( minutes==1 ? '':'s' ) + " and ";
			message += seconds + " second" + ( seconds==1 ? '':'s' ) + " <br />";
			
			if (minutes==0 && seconds==0) {
				window.location.href='https://jpv1.library.sh.cn';
			}
			
			
			
			note.html(message);
		}
	});
	
});

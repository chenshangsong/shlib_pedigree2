$(function(){
	
	var colors = [
		'616161','616161','616161','616161','616161',
		'616161','616161','616161','616161','616161',
		'616161','616161','616161','616161','616161',
		'616161','616161','616161','616161','616161',
		'616161','616161','616161','616161','616161',
		'616161','616161','616161','616161','616161'
	];
	
	var rad2deg = 180/Math.PI;
	var deg = 0;
	var bars = $('#bars');
	
	for(var i=0;i<colors.length;i++){
		
		deg = i*12;
		
		// Create the colorbars
		
		$('<div class="colorBar">').css({
			backgroundColor: '#'+colors[i],
			transform:'rotate('+deg+'deg)',
			top: -Math.sin(deg/rad2deg)*80+100,
			left: Math.cos((180 - deg)/rad2deg)*80+100,
		}).appendTo(bars);
	}
	
	var colorBars = bars.find('.colorBar');
	var numBars = 0, lastNum = -1;
	
	$('#control').knobKnob({
		snap : 10,
		value: 154,
		turn : function(ratio){
			numBars = Math.round(colorBars.length*ratio);
			
			// Update the dom only when the number of active bars
			// changes, instead of on every move
			
			if(numBars == lastNum){
				return false;
			}
			lastNum = numBars;
			
			colorBars.removeClass('active').slice(0, numBars).addClass('active');
		}
	});
	
});

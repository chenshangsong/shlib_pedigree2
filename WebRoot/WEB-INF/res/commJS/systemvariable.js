/**
 * 系统变量
 */
/*规范数据类型*/
jQuery.dataTypeGroup = {  
		//根据key得到name
		getName:function(key){ 
			var value;
			switch (key) {
				case '1':
					value='堂号';
					break;
				case '2':
					value='姓氏';
					break;
				case '3':
					value='机构';
					break;
				case '4':
					value='先祖';
					break;
				case '5':
					value='名人';
					break;
				case '6':
					value='纂修者';
					break;
				case '7':
					value='谱籍';
					break;
				case '8':
					value='谱名';
					break;
				case '9':
					value='取值词表';
					break;
				case '10':
					value='书目';
					break;
				default:
					value='';
					break;
				}
			return value;
		 },
        unSubmit:'0',  //待提交
        unApproval:'1',  //待审核
        pass:'2', //通过
        unPass:'3' //不通过
};   
/**
 * 父级URI
 */
jQuery.dataUriGroup = {  
		//根据key得到name
		getName:function(key){ 
			var value;
			switch (key) {
				case '1':
					value='ancestraltempleManager/list';
					break;
				case '2':
					value='familyNameManager/familyName';
					break;
				case '3':
					value='organizationManager/list';
					break;
				case '4':
					value='personManager/ancestorsList';
					break;
				case '5':
					value='personManager/famousList';
					break;
				case '6':
					value='personManager/writerList';
					break;
				case '7':
					value='placeManager/list';
					break;
				case '8':
					value='titleManager/list';
					break;
				case '9':
					value='categoryManager/list';
					break;
				case '10':
					value='workManager/list';
					break;
				default:
					value='';
					break;
				}
			return value;
		 },
        unSubmit:'0',  //待提交
        unApproval:'1',  //待审核
        pass:'2', //通过
        unPass:'3' //不通过
};   
/**
 * 父级URI
 */
jQuery.pageUrlGroup = {  
		//根据key得到name
		getName:function(key){ 
			var value;
			switch (key) {
				case '1':
					value='ancestraltempleManager/';
					break;
				case '2':
					value='familyNameManager/';
					break;
				case '3':
					value='organizationManager/';
					break;
				case '4':
					value='personManager/';
					break;
				case '5':
					value='personManager/';
					break;
				case '6':
					value='personManager/';
					break;
				case '7':
					value='placeManager/';
					break;
				case '8':
					value='titleManager/';
					break;
				case '9':
					value='categoryManager/';
					break;
				case '10':
					value='workManager/';
					break;
				default:
					value='';
					break;
				}
			return value;
		 },
        unSubmit:'0',  //待提交
        unApproval:'1',  //待审核
        pass:'2', //通过
        unPass:'3' //不通过
};  
jQuery.roleGroup = {  
        normal:'1',  //普通用户
        expert:'2', //专家用户
        admin:'3' //管理员用户
};   
/**
 * 颜色
 */
jQuery.colorGroup = {  
		//根据key得到name
		getColor:function(key){ 
			var value;
			switch (key) {
			case 'index':
				value='#B13F51';
				break;
				case 'work':
					value='#F1BA01';
					break;
				case 'familyName':
					value='#DBBE0B';
					break;
				case 'person':
					value='#DF7900';
					break;
				case 'place':
					value='#AF6D21';
					break;
				case 'feedBack':
					value='#93D2C3';
					break;
				case 'todolist':
					value='#00A483';
					break;
				case 'systemSetting':
					value='#099697';
					break;
				case 'personInfo':
					value='#3483AE';
					break;	
				default:
					value='white';
					break;
				}
			return value;
		 }
};  
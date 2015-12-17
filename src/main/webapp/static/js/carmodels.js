
function retModelName(response){//二级填充
	var ret = eval('(' + response.responseText + ')');
	//console.log(ret);
	if(ret.res!="0"){
		for(var i=0;i<ret.res;i++){
			jQuery("#model_name_id").append("<option value='" + ret.modnlist[i].id +"'>"+ret.modnlist[i].model_name+"</option>");
		}
	}
}

function retTypeSeries(response){//三级填充
	var ret = eval('(' + response.responseText + ')');
	//console.log(ret);
	if(ret.res!="0"){
		for(var i=0;i<ret.res;i++){
			jQuery("#type_serie_id").append("<option value='" + ret.tyseelist[i].id +"'>"+ret.tyseelist[i].type_series+"</option>");
		}
	}
}

function retTypeName(response){//搜索结果填充
	var ret = eval('(' + response.responseText + ')');
	//console.log(ret);
	//jQuery(".ac_results").html("无记录");
	if(ret.res!="0"){
		jQuery(".ac_results").css("display","block");
		//设置ac_results 位置
		jQuery(".ac_results").css("left",jQuery("#type_name_id").offset().left);//搜索结果与搜索框左边对齐
		var retstr="<ul>";
		for(var i=0;i<ret.res;i++){
			retstr+="<li><a href='javascript:void(0)' onclick='getResults(this)' class='res_ct'>" +ret.tynameslist[i].type_series + "     " +ret.tynameslist[i].type_name +"</a></li>";
			//console.log(ret.tynameslist[i].type_series+"-"+ret.tynameslist[i].type_name);
			//jQuery("#type_serie_id").append("<option value='" + ret.tyseelist[i].id +"'>"+ret.tyseelist[i].type_series+"</option>");
		}
		jQuery(".ac_results").html(retstr+"</ul>");
	}
}


function getResults(aobj){//点击搜索结果链接
	var atxt = aobj.innerText;
	jQuery("#type_name_id").val(atxt);//获取点击结果
	//jQuery(".ac_results").html("");//清除并隐藏搜索结果
	jQuery(".ac_results").css("display","none");
	
	//初始化所购车辆型号
	var mdname = jQuery("#model_name_id").find("option:selected").text();
	var mkname = jQuery("#make_name_id").find("option:selected").text();
	jQuery("#name").val(mkname + mdname + atxt);
}

function mnerror(e){//error
	//console.log(e);
}


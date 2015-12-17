//string:原始字符串
//substr:子字符串
//isIgnoreCase:忽略大小写
function contains(string,substr,isIgnoreCase) {
	if(isIgnoreCase) {
		string=string.toLowerCase();
		substr=substr.toLowerCase();
	}
	var startChar=substr.substring(0,1);
	var strLen=substr.length;
	for(var j=0;j<string.length-strLen+1;j++) {
		if(string.charAt(j)==startChar) { //如果匹配起始字符,开始查找
			if(string.substring(j,j+strLen)==substr) { //如果从j开始的字符与str匹配，那ok
				return true;
			}  
		}
	}
	return false;
}


//上传缩略图
 function checkImageon(item){
 if(item == null){
  		return;
  }
  var fileName = item.value.toLowerCase();
  if((fileName.indexOf(".gif") == -1)
            &&(fileName.indexOf(".jpg") ==  -1)
            &&(fileName.indexOf(".jpeg") == -1)
            &&(fileName.indexOf(".png") == -1)){  
		         alert("请选择gif或jpg,png的图象文件");
		         return;

     }
 
 //显示加载的图片
 document.getElementById("lodingdiv").style.display="";
 document.getElementById("limgs").style.display="none";
 document.getElementById("upform").target = "rFrame";
 document.getElementById("upform").submit();
 return;	      
}

 //上传图片返回
function setUrlImage(img,m){
  document.getElementById("lodingdiv").style.display="none";
  document.getElementById("limgs").style.display="";
  document.getElementById("simg").src = img;
  document.getElementById("imgpath").value = img;
  
  document.getElementById("sforms_0").style.display="none";
  document.getElementById("sforms_1").style.display="";
  
}


//去掉空格
function trim(str) {
	return (str + '').replace(/(\s+)$/g, '').replace(/^\s+/g, '');
}
//去掉所有的html标记
function delHtmlTag(str) {
	return str.replace(/<[^>]+>/g,"");
}
//列表全选
function checkAll(obj) {
	var cbox = document.getElementsByName("cbox_m");
	if (cbox) {
		for (var i=0; i<cbox.length; i++) {
			if (obj.checked) {
				cbox[i].checked = true;
			} else {
				cbox[i].checked = false;
			}
		}
	}
}
//查询功能
function search(url) {
	var value = document.getElementById("sousuo").value;
	value=encodeURI(value);
   	value=encodeURI(value);
   	url = url + "?sousuo=" + value;
   	location.href = url; 
}
//ajax 返回方法(所有都一样，公用一个)
function retObjs(response) {
	var ret = eval('(' + response.responseText + ')');
	if(ret.res==1){
	    alert("操作成功");
  		window.location.reload();
  	} else {
  		alert(ret.message);
  	}
}
//ajax 彻底删除操作 批量
function delObjs(url) {
	var ids = "";
	var cbox = document.getElementsByName("cbox_m");
	if (cbox) {
		for (var i=0; i<cbox.length; i++) {
			if (cbox[i].checked) {
				ids = ids + cbox[i].value + ",";
			} 
		}
	}
	ids = ids.substring(0, ids.length-1);
	var para = "ids=" + ids;
	var myAjax=new Ajax.Request(url,{method:'post',parameters:para,onComplete:retObjs,onError:error});
}
//ajax 更改对象状态（审批 删除 可批量）
function updateState(state, url, id) {
	if(state == 2) {
		//删除操作提示一下
		if(!confirm("是否要删除？")){
			return false;
		}
	}
	if(state == 3){
	  //操作提示
	  if(!confirm("是否要解冻清零积分和能量？")){
	      return false;
	  }
	}

	var ids = "";
	if (id && id != "") {
		ids = id;
	} else {
		var cbox = document.getElementsByName("cbox_m");
		if (cbox) {
			for (var i=0; i<cbox.length; i++) {
				if (cbox[i].checked) {
					ids = ids + cbox[i].value + ",";
				} 
			}
		}
		ids = ids.substring(0, ids.length-1);
	}
	if(trim(ids) == '' || ids.length == 0){
	  alert("请选择一项操作");
	  return false;
	}
	var para = "ids=" + ids + "&state=" + state;
	var myAjax=new Ajax.Request(url, {method:'post',parameters:para,onComplete:retObjs,onError:error});
}
//ajax 更改对象状态（审批 删除 可批量）
function userupdateState(state, url, id,msg,username) {
	if(state == 2) {
		//删除操作提示一下
		if(!confirm("是否要删除？")){
			return false;
		}
	}
	if(state == 3){
	  //操作提示
	  if(!confirm("是否要解冻清零积分和能量？")){
	      return false;
	  }
	}
	if(state==-1&&(msg==""||msg==null||typeof(msg)=="undefined")){
		alert("冻结用户必须填写理由!");
		return false;
	}
	if(state==1&&(msg==""||msg==null||typeof(msg)=="undefined")){
		alert("解冻用户必须填写理由!");
		return false;
	}
	if(state==3&&(msg==""||msg==null||typeof(msg)=="undefined")){
		alert("解冻用户清零积分和能量必须填写理由!");
		return false;
	}
	var ids = "";
	if (id && id != "") {
		ids = id;
	} else {
		var cbox = document.getElementsByName("cbox_m");
		if (cbox) {
			for (var i=0; i<cbox.length; i++) {
				if (cbox[i].checked) {
					ids = ids + cbox[i].value + ",";
				} 
			}
		}
		ids = ids.substring(0, ids.length-1);
	}
	if(trim(ids) == '' || ids.length == 0){
	  alert("请选择一项操作");
	  return false;
	}
	var para = "ids=" + ids + "&state=" + state+"&username="+username+"&msg=" + msg;
	var myAjax=new Ajax.Request(url, {method:'post',parameters:para,onComplete:retObjs,onError:error});
}
//ajax 更改对象types属性 （1推荐 2头条 3热门 4固顶…… 可批量）
function updateTypes(type, url, id) {
	var ids = "";
	if (id && id != "") {
		ids = id;
	} else {
		var cbox = document.getElementsByName("cbox_m");
		if (cbox) {
			for (var i=0; i<cbox.length; i++) {
				if (cbox[i].checked) {
					ids = ids + cbox[i].value + ",";
				} 
			}
		}
		ids = ids.substring(0, ids.length-1);
	} 
	var para = "ids=" + ids + "&type=" + type;
	var myAjax=new Ajax.Request(url ,{method:'post',parameters:para,onComplete:retObjs,onError:error});
}
//ajax 移动到操作
function changeTypes(obj, url) {
	var ids = "";
	var cbox = document.getElementsByName("cbox_m");
	if (cbox) {
		for (var i=0; i<cbox.length; i++) {
			if (cbox[i].checked) {
				ids = ids + cbox[i].value + ",";
			} 
		}
	}
	ids = ids.substring(0, ids.length-1);
	var index = obj.selectedIndex;   
	var s = obj.options[index].text;
	s=encodeURI(s);
   	s=encodeURI(s);
	var para = "ids=" + ids + "&typeid=" + obj.value + "&type=" + s;
	var myAjax=new Ajax.Request(url,{method:'post',parameters:para,onComplete:retObjs,onError:error});
}

//省市联动
var cache = new Object();
var areas = [];
function getChildArea(id){
   	if(cache[id] != null){
		return cache[id];
	}
	var para = "parentId="+id;
	var myAjax=new Ajax.Request("info!findArea.htm",{method:'post',parameters:para,onComplete:retArea,onError:error});
}

function retArea(response){
  	var ret = eval('(' + response.responseText + ')');
  	if (ret.message != null) {
    	alert(ret.message);
  	} else {
    	areas = ret.list;
	    if(ret.id == 0){
	      	fillSelect("province",areas);
	    }else{
	      	fillSelect("city",areas);
	    }
  	}
}	

function clearSelect(id){
	var dom = document.getElementById(id);
	for(var i=dom.options.length-1;i>0;i--){
		var option = dom.options[i];
		dom.removeChild(option);
	}
}
	
function fillSelect(id,areas){
	var dom = document.getElementById(id);
	if(dom!=null){
		var sb = [];
		for(var i=0;i<areas.length;i++){
			var area = areas[i];
			var option=new Option();
			option.value = areas[i].c_id;
			option.text = areas[i].c_name;
			dom.options[dom.options.length] = option; 
		}
	}
}
	
function fillChild(select){
	var index = select.selectedIndex;
	var option = select.options[index];
	var id = option.value;
	var domId = select.id;
	var nextDomId = null;
	if(domId=="province"){
	  	nextDomId = "city";
	  	if(document.getElementById("proname")!=null){
	  		document.getElementById("proname").value = option.text;
	  	}
	}
	if(nextDomId==null){
		document.getElementById("cityname").value = option.text;
		return;	
	}
	var n = document.getElementById(nextDomId);
	if(id=="-1"){
		clearSelect(nextDomId);
		fillChild(n);
	}else{
		clearSelect(nextDomId);
		getChildArea(id);
		if(n.value=="-1"){
			n.selectedIndex = 1;
		}
	}
}
var Ajax={
	xmlhttp:function (){
		try{
			return new ActiveXObject('Msxml2.XMLHTTP');
		}catch(e){
			try{
				return new ActiveXObject('Microsoft.XMLHTTP');
			}catch(e){
				return new XMLHttpRequest();
			}
		}
	}
};
Ajax.Request=function (){
	if(arguments.length<2)return ;
	var para = {asynchronous:true,method:"GET",parameters:""};
	for (var key in arguments[1]){
		para[key] = arguments[1][key];
	}
	var _x=Ajax.xmlhttp();
	var _url=arguments[0];
	if(para["parameters"].length>0) para["parameters"]+='&_=';
	if(para["method"].toUpperCase()=="GET") _url+=(_url.match(/\?/)?'&':'?')+para["parameters"];
	_x.open(para["method"].toUpperCase(),_url,para["asynchronous"]);
	_x.onreadystatechange=function (){
		if(_x.readyState==4){
			if(_x.status==200)
				para["onComplete"]?para["onComplete"](_x):"";
			else{
				para["onError"]?para["onError"](_x):"";
			}
		}
	};
	if(para["method"].toUpperCase()=="POST")_x.setRequestHeader("Content-Type","application/x-www-form-urlencoded");

	for (var ReqHeader in para["setRequestHeader"]){
		_x.setRequestHeader(ReqHeader,para["setRequestHeader"][ReqHeader]);
	}
	_x.send(para["method"].toUpperCase()=="POST"?(para["postBody"]?para["postBody"]:para["parameters"]):null);
	return _x;
};
function error(response) {
    	//alert("run ajax failed!");
};



function changeGroupid(){
   var groupid = document.getElementById("groupid");
   var selectGroups = document.getElementById("selectGroups");
   var newwindowid = document.getElementById("newwindowid");
   var selectNewwindow = document.getElementById("selectNewwindow");
   selectNewwindow.value = newwindowid.value;
   selectGroups.value = groupid.value;
}

//删除单个功能权限
var funName = null;
function deleFunctionComp(functionId, functionName){
   funName = functionName;
   if(confirm("是否确定删除 "+functionName) == true){
      var para = "functionId="+functionId + "&functionName="+functionName;
      var myAjax= new Ajax.Request("priviAction!deleteFuncitonComp.htm",{method:'post',parameters:para,onComplete:deleFunctionPara,onError:error}); 
   }
}

function deleFunctionPara(response){
    var ret = eval('(' + response.responseText + ')');
	if("ok" == ret.deleResult) {
		window.location.href="priviAction!index.htm";
	} else {
	  var deleFuncState = document.getElementById("deleFuncState");
	  showInfo("deleFuncState",funName+" 已授权，不能删除! ",true);
	}
}

//删除单个功能分组
function deleFunctionGroup(groupId,groupName){
    funName = groupName;
   if(confirm("是否确定删除 "+groupName)==true){
      var para ="groupId="+groupId + "&groupName="+groupName;
      var myAjax= new Ajax.Request("priviAction!deleteFuncitonGroup.htm",{method:'post',parameters:para,onComplete:deleFunctionGroupPara,onError:error});
   }
}

function deleFunctionGroupPara(response){
	 var ret = eval('(' + response.responseText + ')');
	if("ok" == ret.deleResult) {
		window.location.href="priviAction!functionGroupsMana.htm";
	} else {
	  var deleFuncState = document.getElementById("deleFuncState");
	  showInfo("deleFuncState",funName+" 已授权，不能删除! ",true);
	}
}
//验证功能名称
function addFunctionCompSubmit(){
   var functionName = document.getElementById("functionName");
   
   if(functionName.value.replace(/[ ]/,"")==""){
	   var deleFuncState = document.getElementById("deleFuncState");
	   showInfo("deleFuncState"," 功能名称不能为空 ",true);
      return ;
   }
   document.myform.submit();
}
//验证分组名称
function funcGroupSubmit(){
   var groupName = document.getElementById("functionGroupName");
   if(groupName.value.replace(/[ ]/,"")==""){
       var deleFuncState = document.getElementById("deleFuncState");
	   showInfo("deleFuncState"," 功能分组不能为空 ",true);
      return ;
   }
   document.myform.submit();
}
function loadAddGroup(groupStr){
   var deleFuncState = document.getElementById("deleFuncState");
	   showInfo("deleFuncState",groupStr,true);
}



//提示层的显示方法和停留时间-----起点
var g_s_registered = {id:0};
var g_isie = null;
window.slogClosed = false;
var _defaultRequest = null;
var SigmaUtil = {
    version:"1.0",

	getRequest:function(){
		if(_defaultRequest==null){
			_defaultRequest = new SigmaRequest();
		}
		return _defaultRequest;
	},
    $ : function(id){
        var dom = document.getElementById(id);
        return dom;
    },
    $$:function(e1,e2){
        if(e1 instanceof String){
            e1 = SigmaUtil.$(e1);
        }
        if(navigator.appVersion.indexOf("MSIE")!=-1){
            return e1.all(e2);
        }else{
            var cs =  e1.childNodes;
            var r = null;
            for(var i=0;i<cs.length;i++){
                var c = cs[i];
                if(e2===c.id ) r = c;
                else r = SigmaUtil.$$(c,e2);
                if(r!=null)	return r;
            }
            return null;
        }
    },

    changeXmlNodeToJSON : function(node,memberAsElement){
         var isLeafNode = function(node){
            return (node.childNodes!=null
               &&(node.childNodes.length<1||(node.childNodes.length==1
               &&node.childNodes[0].nodeType == 3)));
         };
         if(node==null) return null;
         var obj = new Object();
        
         var attributes = node.attributes;
         if(attributes!=null&&!memberAsElement){
            for(var i=0;i<attributes.length;i++){
                obj[attributes[i].name] = unescape(attributes[i].value);
            }
         }
         var children = node.childNodes;
         if(children!=null){
            for(var i=0;i<children.length;i++){
               if(memberAsElement&&isLeafNode(children[i])){
                  if(!children[i].childNodes[0]){
                     obj[children[i].tagName] = "";
                  }else{
                     obj[children[i].tagName] = unescape(children[i].childNodes[0].nodeValue);
                  }                
                  continue;
               }
               var childObj = SigmaUtil.changeXmlNodeToJSON(children[i],memberAsElement);
               var name = children[i].tagName;
               var brother = obj[name];
               if(brother==null){
                  obj[name] = childObj;
               }else if(brother instanceof Array){
                  brother.push(childObj);
               }else{
                  var arr = new Array();
                  arr.push(brother);
                  arr.push(childObj);
                  obj[name] = arr;
               }
            }
        }
        return obj;
    },
   
    isIE : function(){
        if(g_isie===null){
            if(navigator.appVersion.indexOf("MSIE")>=0){
                g_isie =  true;
            }else{
                g_isie = false;
            }
        }
        return g_isie;
    },
    
    register : function(obj){
        var rs = g_s_registered;
        if(obj.id!=null){
            rs[obj.id] = obj;
        }else{
            rs.id++;
            obj.id = rs.id;
            rs[obj.id] = obj;
        }
    },
    retrieve:function(objId){
        var rs = g_s_registered;
        return rs[objId];
    },

    closeLog :function(){
        window.slogClosed = true;
    },
    clearLog :function(){
        window._log_out.value = "";
    },
    log:function(s){
        if(window.slogClosed==true) return;
        if(window._log_out == null){
            var box = document.createElement("div");
            var tip = document.createElement("div");
            var out = null;
            box.style.width ="100%";
            tip.innerHTML = "Hidden Log";
            tip.onclick = function(){
                if(out.style.display == ""){
                    out.style.display="none";
                    tip.innerHTML = "Show Log";
                }else{
                    out.style.display="";
                    tip.innerHTML = "Hidden Log";
                }
            }
            out = document.createElement("textarea");
            out.style.width = "100%";
            out.style.height = "300px";
            box.appendChild(tip);
            box.appendChild(out);
            document.body.appendChild(box);
            window._log_out = out;
        }
        var fN = SigmaUtil.log.caller.getName();
        var t = new Date().toLocaleString();
        s = t + " " + ":" + s;
        window._log_out.value = window._log_out.value+s+"\n";
    },
	addEventHandler:function(dom,event,handler){
		if(SigmaUtil.isIE()){
			dom.attachEvent("on"+event,handler);			
		}else{
			dom.addEventListener(event,handler,false);
		}
	},
	
	removeEventHandler:function(dom,event,handler){
		if(SigmaUtil.isIE()){
			dom.detachEvent("on"+event,handler);			
		}else{
			dom.removeEventListener(event,handler,false);
		}
	}
}
    //调用提示层得方法
   function showInfo(id,info,autoHide){
	var infobox = SigmaUtil.$(id);
	infobox.style.display="";
	infobox.innerHTML = info;
	if(autoHide)
		toHide(id);
	}
	function toHide(id){
		setTimeout('doHide("'+id+'")',1000*3);
	}
	
	function doHide(id){
		SigmaUtil.$(id).style.display = "none";
	}
	
//提示层的显示方法和停留时间-----结束
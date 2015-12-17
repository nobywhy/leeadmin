String.prototype.trim = function () {
    var reg = g_s_regs["(^\s*)|(\s*$)"];
    if(reg==undefined){
        reg = /(^\s*)|(\s*$)/g;
        g_s_regs["(^\s*)|(\s*$)"] = reg;
    }
    return this.replace(reg, "");
}

String.prototype.endWith = function(s){
    var p = this.lastIndexOf(s);
    if(p+s.length==this.length){
        return true;
    }else
        return false;
}

String.prototype.asciiLen = function(){
    var intLength=0;
    for (var i=0;i<this.length;i++){
        if ((this.charCodeAt(i) < 0) || (this.charCodeAt(i) > 255))
            intLength=intLength+2
        else
            intLength=intLength+1    
    }
    return intLength

}

Array.prototype.indexOf=function(o){
    for(var i=0;i<this.length;i++){
        if(this[i]==o)
            return i;
    }
    return-1;
};

Array.prototype.lastIndexOf=function(o){
    for(var i=this.length-1;i>=0;i--){
        if(this[i]==o)
            return i;
    }
    return-1;
};

Array.prototype.contains=function(o){
    return this.indexOf(o)!= -1;
};

Array.prototype.addAll=function(newArray){
    for(var i=0;i<newArray.length;i++){
        this.push(newArray[i]);
    }
}

Array.prototype.copy=function(o){
    return this.concat();
};

Array.prototype.insertAt=function(o,i){
    this.splice(i,0,o);
};

Array.prototype.insertBefore=function(o,o2){
    var i=this.indexOf(o2);
    if(i== -1)
        this.push(o);
    else
        this.splice(i,0,o);
};

Array.prototype.insertAfter = function(o1,o2){
    var i = this.indexOf(o2);
    this.splice(i,0,o1);
}

Array.prototype.removeAt=function(i){
    this.splice(i,1);
};

Array.prototype.remove=function(o){
    var i=this.indexOf(o);
    if(i!= -1)
        this.splice(i,1);
};

Object.prototype.toXML = function(tagName, memberAsElement){
    var key;
    var sb = [];
    var children = [];
    var natives = [];
    sb.push("<"+tagName);
    for(key in this){
        var v = this[key];
        if(v.constructor==Function) continue;
        else if(v.constructor ==Object){
            v = v.toXML(key,memberAsElement);
            var child = {};
            child.name=key;
            child.value = v;
            children.push(child);
        }else{
            if(memberAsElement===true){
               var na = {};
               na.key = key;
               na.v = v;
               natives.push(na);
            }else{
               if(v.constructor == String){
                  v = '"' + v.replace('"','%22')+'"';
               }else{
                  v = '"'+v+'"';
               }
               sb.push(" " + key + "=" + v + " ");
            }  
        }
    }
    //sb.splice(sb.length-1,1);
    sb.push(">");
    for(var i=0;i<natives.length;i++){
      var na = natives[i];
      sb.push("<" + na.key + ">" + na.v + "</" + na.key + ">");
    }
    for(var i=0;i<children.length;i++){
        var child = children[i];
        sb.push(child.value);
    }
    sb.push("</"+tagName+">");
    return sb.join("");
}

Object.prototype.toParamsList = function(parentName, jointChar){
   jointChar = (jointChar!=null) ? jointChar:"_";
   var prefix = (parentName!=null)? parentName + jointChar : "";
   var retStr = "";
   for(var key in this){
        var v = this[key];
        if(v.constructor==Function) continue;
        else if(v.constructor ==Object){
            retStr += v.toParamsList(prefix + key,jointChar);
        }else{
            retStr += prefix + key + "=" + v + "&";  
        }
    }
    return retStr;
}

Array.prototype.toXML = function(tagName){
    var	sb=[];
    sb.push("<root>\n");
    for(i=0;i<this.length;i++){
        var v = this[i];
        if(v.constructor==Function) continue;
        else if(v.constructor ==Object){			
            v = v.toXML(tagName);
            sb.push(v);
            sb.push('\n');
        }else {
            if(v.constructor==String)
                v = '"' + v.replace('"','\\\"')+'"';
            else
                v ='"'+v+'"';
            sb.push("<"+tagName+">");
            sb.push(v);
            sb.push("</"+tagName+">");
        }
    }
    sb.push("</root>\n");
    return sb.join("");
}
Object.prototype.s$ = function(name,v,mode){
    var ns = null;
    if(typeof(name)==="string"){
        ns = name.split(".");
        if(mode==="number"){
            v = new Number(v);
        }else if(mode==="boolean"){
            if(v==="1")
                v = true;
            else if(v==="0")
                v = false;
        }else if(mode==="date"){
            return false;
        }
    }else if(name instanceof Array){
        ns = name;
    }else{
        alert("Illegal parameter - "+name);
        return false;
    }
    
    if(ns.length>1){
        var obj =this[ns[0]];
        if(obj===null || obj ===undefined){
            obj = new Object();
            this[ns[0]] = obj;
            ns.shift();
            obj.s$(ns,v,mode);
        }else{
            ns.shift();
            obj.s$(ns,v,mode);
        }
    }else{
        this[ns[0]] = v;
    }
}

Object.prototype.p$ = function(name,mode){
    var ns = null;
    if(typeof(name)==="string"){
        ns = name.split(".");
    }else if(name instanceof Array){
        ns = name;
    }else{
        alert("Illegal parameter - "+name);
        return;
    }
    if(ns.length>1){
        var obj =this[ns[0]];
        if(obj===null || obj ===undefined){
            return null;
        }else{
            ns.shift();
            return obj.p$(ns,mode);
        }
    }else{
        var v = this[ns[0]];
        if(v==null){
            if(mode!==undefined){
                if(mode==="number")
                    v = 0;
                else if(mode==="boolean")
                    v = false;
                else 
                    v = "";
            }
        }
        return v;
    }
}

Number.prototype.format = function(f){
    var n = this;
    f = f.trim();
    if(f==null)	f = "#.00";
    var isPercent = false;
    if(f.endWith("%")){
        isPercent = true;
        n = n*100;
        f = f.substring(0,f.length-1);
    }
    var v ;
    if(f=="#"){
        v = String(Math.round(n));
    }else{
        var c = 0;
        if(f.endWith("#")){
            c = f.length - 3;
            v  = ""+n;
        }else{
            c = f.length - 2;
            var p = Math.pow(10,c);
            var t = p * n;
            t = Math.round(t);
            v = String(t / p);
        }	
        if(c>0){
            if(v.indexOf(".")===-1){
                v = v+".0";
            }
            var p = v.indexOf(".")+1;
            if(v.length-p<c){
                var l = c-(v.length-p);
                for(var i = 0;i<l;i++){
                    v = v+"0";
                }
            }
        }
    }
    if(isPercent===true){
        v = v+"%";
    }
    return v;
}

Function.prototype.getName = function(){
    var r = ""+this;
    var p = r.indexOf("{");
    r = r.substring(0,p);
    return r;
}
Function.prototype.extendFrom = function(base){
    this._base = base;
    for(var member in base.prototype){
        if(typeof(this.prototype[member]) === 'undefined'){
            this.prototype[member] = baseClass.prototype[member];
        }
    }
}
Function.prototype.initializeBase = function(){
    if(typeof(this._base) === 'undefined') return;    
    var me = arguments[0];
    if(!me) return;
    this.baseClass.apply(me,arguments[1]);
}


var g_s_regs = {};
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

var s_draggingObj = null;
if(SigmaUtil.isIE()){
    window.document.attachEvent("onmousemove",
        function(e){if(s_draggingObj!=null)	s_draggingObj.moveTo(e.clientX,e.clientY);}
    );
    window.document.attachEvent("onmouseup",
        function(e){if(s_draggingObj!=null){s_draggingObj.release();s_draggingObj = null;}}
    );
    
}else{
    window.document.addEventListener("mousemove",
        function(e){if(s_draggingObj!=null) s_draggingObj.moveTo(e.pageX,e.pageY);},
        false
    );
    window.document.addEventListener("mouseup",
        function(e){if(s_draggingObj!=null){s_draggingObj.release();s_draggingObj = null;}},
        false
    );
}

function createPageBar2(pageNumber,pageSize,pageIndex,total,callback,viewedNum,param){
	var sb = [];
	sb.push('<ul class="pagebar">共'+total+'条记录,每页'+pageSize+'条,当前第'+pageIndex+'页');
	if(pageNumber<=1){
		sb.push('</ul>');
		return sb.join('');
	}	
	if (param==null)
		param="";
	if(viewedNum==null)
		viewedNum = 5;
	var firstNumber = pageIndex-Math.floor(viewedNum/2);
	
	if(firstNumber<1)
		firstNumber = 1;
	var lastNumber = firstNumber+viewedNum-1;
	if(lastNumber>pageNumber)
		lastNumber = pageNumber;
	while(firstNumber>1 && (lastNumber-firstNumber+1)<viewedNum){
		firstNumber--;
	}
	var curl = '';
	if(pageIndex>1){
	    curl = callback + '(' + (1) + ',' + pageSize+')';
	    sb.push('<li><a href="'+curl+'">首页</a></li>');
		curl = callback + '(' + (pageIndex-1) + ',' + pageSize+')';
		sb.push('<li><a href="'+curl+'">上一页</a></li>');
	}
	for(var i=firstNumber;i<=lastNumber;i++){
		curl = callback + '(' + i + ',' + pageSize+')';
		if(i==pageIndex){
			sb.push('<li class="ch"> <a href="'+curl+'">'+i+'</a></li>');
		}else{
			sb.push('<li> <a href="'+curl+'">'+i+'</a></li>');
		}
	}
	if(pageIndex<pageNumber){
		curl = callback + '(' + (pageIndex+1) + ',' + pageSize+')';
		sb.push('<li><a href="'+curl+'">下一页</a></li>');
		curl = callback + '(' + (pageNumber) + ',' + pageSize+')';
		sb.push('<li><a href="'+curl+'">末页</a></li>');
	}
    
    sb.push('<li>共' + pageNumber + '页，跳转至</li>'); 
	sb.push('<li><select id="pid"  onchange="location.href=this.value" >');  		
	for(var i=1;i<=pageNumber;i++){	
	if(i==pageIndex){
		curl = callback + '(' + i + ',' + pageSize+')';
		sb.push('<li> <option value="'+curl+'"  selected="selected">'+i+'</option></li>');
	}else{
		curl = callback + '(' + i + ',' + pageSize+')';
		sb.push('<li> <option value="'+curl+'">'+i+'</option></li>');
	}
	}
	sb.push('</select></li>');  

	sb.push('</ul>');
	return sb.join('');
}

function createPageBar(pageNumber,pageSize,pageIndex,url,viewedNum,param){
	if(pageNumber<=1)
		return "";
	if (param==null)
		param="";
	if(viewedNum==null)
		viewedNum = 5;
	var firstNumber = pageIndex-Math.floor(viewedNum/2);
	
	if(firstNumber<1)
		firstNumber = 1;
	var lastNumber = firstNumber+viewedNum-1;
	if(lastNumber>pageNumber)
		lastNumber = pageNumber;
	if((lastNumber-firstNumber+1<viewedNum) && pageNumber>viewedNum){
		firstNumber= lastNumber-viewedNum+1;
	}
	var num=lastNumber-firstNumber+1;
	if (num<viewedNum){
		firstNumber=lastNumber-viewedNum-1;
		if(firstNumber<0)
		{
		firstNumber=1;
		}
	}
	var sb = [];
	var curl = '';
	sb.push('<ul>');
	pageSize = pageSize + "&_isRFPB=true";
	var joinChar = "&";
	if(url.indexOf(".action?")==-1)
		joinChar = "?";
	if(pageIndex>1 && pageIndex <= pageNumber){
	    curl = url+joinChar+'pageIndex='+(1)+'&pageSize='+pageSize+param;
	    sb.push('<li><a href="'+curl+'">首页</a></li>');
		curl = url+joinChar+'pageIndex='+(pageIndex-1)+'&pageSize='+pageSize+param;
		sb.push('<li><a href="'+curl+'">上一页</a></li>');
	}
	for(var i=firstNumber;i<=lastNumber;i++){
		if(i==pageIndex){
			curl = url+joinChar+'pageIndex='+i+'&pageSize='+pageSize+param;
			sb.push('<li class="ch"> <a href="'+curl+'">'+i+'</a></li>');
		}else{
			curl = url+joinChar+'pageIndex='+i+'&pageSize='+pageSize+param;
			sb.push('<li> <a href="'+curl+'">'+i+'</a></li>');
		}
	}
	if(pageIndex<pageNumber){
		curl = url+joinChar+'pageIndex='+(pageIndex+1)+'&pageSize='+pageSize+param;
		sb.push('<li><a href="'+curl+'">下一页</a></li>');
		curl = url+joinChar+'pageIndex='+(pageNumber)+'&pageSize='+pageSize+param;
		sb.push('<li><a href="'+curl+'">末页</a></li>');
	}
	sb.push('<li>共' + pageNumber + '页，跳转至</li>');  	
	sb.push('<li><select id="pid" onchange="location.href=this.value">');  		
	for(var i=1;i<=pageNumber;i++){	
	if(i==pageIndex){
		curl = url+joinChar+'pageIndex='+i+'&pageSize='+pageSize+param;
		sb.push('<li> <option value="'+curl+'"  selected="selected">'+i+'</option></li>');
	}else{
		curl = url+joinChar+'pageIndex='+i+'&pageSize='+pageSize+param;
		sb.push('<li> <option value="'+curl+'">'+i+'</option></li>');
	}
	}
	sb.push('</select></li>');  		

	sb.push('</ul>');
	return sb.join('');
}

function createAjaxPageBar(pageNumber,pageSize,pageIndex,url,viewedNum){
	if(pageNumber<=1)
		return "";
	if(viewedNum==null)
		viewedNum = 5;
	var firstNumber = pageIndex-Math.floor(viewedNum/2);
	
	if(firstNumber<1)
		firstNumber = 1;
	var lastNumber = firstNumber+viewedNum-1;
	if(lastNumber>pageNumber)
		lastNumber = pageNumber;
	if((lastNumber-firstNumber+1<viewedNum) && pageNumber>viewedNum){
		firstNumber= lastNumber-viewedNum+1;
	}
	var num=lastNumber-firstNumber+1;
	if (num<viewedNum){
		firstNumber=lastNumber-viewedNum-1;
		if(firstNumber<0)
			firstNumber=1;
	}
	var sb = [];
	var curl = '';
	sb.push('<ul>');
	if(pageIndex>1){
		curl = 'javascript:getComment(\'&pageIndex='+(pageIndex-1)+'&pageSize='+pageSize+'\')';
		sb.push('<li><a href="'+curl+'">上一页</a></li>');
	}else{
		sb.push('<li><a href="#">上一页</a></li>');
	}
	for(var i=firstNumber;i<=lastNumber;i++){
		if(i==pageIndex){
			curl = 'javascript:getComment(\'&pageIndex='+i+'&pageSize='+pageSize+'\')';
			sb.push('<li class="ch"> <a href="'+curl+'">'+i+'</a></li>');
		}else{
			curl = 'javascript:getComment(\'&pageIndex='+i+'&pageSize='+pageSize+'\')';
			sb.push('<li> <a href="'+curl+'">'+i+'</a></li>');
		}
	}
	if(pageIndex<pageNumber){
		curl = 'javascript:getComment(\'&pageIndex='+(pageIndex+1)+'&pageSize='+pageSize+'\')';
		sb.push('<li><a href="'+curl+'">下一页</a></li>');
	}else{
		sb.push('<li><a href="#">下一页</a></li>');
	}

	sb.push('</ul>');
	return sb.join('');
}

function formatTime(sT,cT){
var str=sT;
	var s1=toNewDate(sT);
	var s2=toNewDate(cT);
	var dif=(s2-s1)/1000;
	if (dif<3)
	{str="1秒钟前";}
	else if (dif >=3 && dif<60 ){
		str=dif+"秒钟前";
	}else if (dif >=60 && dif<3600 ){
		str=Math.floor(dif/60)+"分钟前";
	}else if (dif >=3600 && dif<86400) {
		str=Math.floor(dif/3600)+"小时前";
	}

return str;
}

function toNewDate(sDate){
	var s=sDate.split(' ');
	var s1=s[0].split('-');
	var s2='';
	switch(toInt(s1[1])){
		case 1:
			s2='January';
			break;
		case 2:
			s2='February';
			break;
		case 3:
			s2='March';
			break;
		case 4:
			s2='April';
			break;
		case 5:
			s2='May';
			break;
		case 6:
			s2='June';
			break;
		case 7:
			s2='July';
			break;
		case 8:
			s2='August';
			break;
		case 9:
			s2='September';
			break;
		case 10:
			s2='October';
			break;
		case 11:
			s2='November';
			break;
		case 12:
			s2='December';
			break;
	}
	var s3=s2+' '+s1[2]+', '+s1[0]+' '+s[1];
	return Date.parse(s3);
}

//2008-04-12 01:13:17
function splitTime(t){
var t1=t.split(" ");
var t2=t1[0].split("-");
var t3=t1[1].split(":");
var t4=new Array();
t4[0]=toInt(t2[0]);
t4[1]=toInt(t2[1]);
t4[2]=toInt(t2[2]);
t4[3]=toInt(t3[0]);
t4[4]=toInt(t3[1]);
t4[5]=toInt(t3[2]);
return t4;
}

function toInt(str){
if(str.length==2 && str.substring(0,1)=='0')
str=str.substring(1);
return parseInt(str);
}

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

function toEnable(id){
	setTimeout('doEnable("'+id+'")',1000*3);
}
function doEnable(id){
	su.$(id).disabled = false;
}

function getCookies(){
	var map = {};
	var s = window.document.cookie;
	var ss = s.split(";");
	for(var i=0;i<ss.length;i++){
		var nvp = ss[i].split("=");
		map[nvp[0].trim()] = nvp[1].trim();
	}
	return map;
}

	/*获得页面当前滚动偏移量*/
	function _getScrollOffset(){
		var posX,posY;  
	    if (window.innerHeight) {  
	        posX = window.pageXOffset;  
	        posY = window.pageYOffset;  
	    }else if (document.documentElement && document.documentElement.scrollTop) {  
	        posX = document.documentElement.scrollLeft;  
	        posY = document.documentElement.scrollTop;  
	    }else if (document.body) {  
	        posX = document.body.scrollLeft;  
	        posY = document.body.scrollTop;  
	    }
		var r = [];
		r[0] = posX;
		r[1] = posY;
		return r;
	}
	/*获得当前窗口尺寸和页面尺寸*/
	function _getPageSize(){
		var xScroll, yScroll;
 
		if (window.innerHeight && window.scrollMaxY) { 
			xScroll = document.body.scrollWidth;
			yScroll = window.innerHeight + window.scrollMaxY;
		}else if (document.body.scrollHeight > document.body.offsetHeight){ // all but Explorer Mac
			xScroll = document.body.scrollWidth;
			yScroll = document.body.scrollHeight;
		}else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla and Safari
			xScroll = document.body.offsetWidth;
			yScroll = document.body.offsetHeight;
		}
 
		var windowWidth, windowHeight;
		if (self.innerHeight) { // all except Explorer
			windowWidth = self.innerWidth;
			windowHeight = self.innerHeight;
		} else if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode
			windowWidth = document.documentElement.clientWidth;
			windowHeight = document.documentElement.clientHeight;
		}else if (document.body) { // other Explorers
			windowWidth = document.body.clientWidth;
			windowHeight = document.body.clientHeight;
		} 
 
		// for small pages with total height less then height of the viewport
		if(yScroll < windowHeight){
			pageHeight = windowHeight;
		} else {
			pageHeight = yScroll;
		}
		// for small pages with total width less then width of the viewport
		if(xScroll < windowWidth){ 
			pageWidth = windowWidth;
		} else {
			pageWidth = xScroll;
		}

		arrayPageSize = new Array(pageWidth,pageHeight,windowWidth,windowHeight)
		return arrayPageSize;
	}
	
function setWindowTitle(title){
	var titles = {
		"guestwords.do"				:"留言",
		"home.do"					:"首页",
		"miniblog!listAll.do"		:"最近迷你博客",
		"miniblog!listFriend.do"	:"好友的迷你博客",
		"miniblog!listMy.do"		:"我的迷你博客",
		"blog!listAllBlogs.do"		:"最近日志",
		"blog!listBlogsByCategory.do":"好友的日志",
		"blog!listMySelfBlogs.do"	:"我的日志",
		"subscribe!listAllSubscribeBlogs.do":"我的订阅",
		"blog!addBlog.do"			:"写新日志",
		"blog!editBlog.do"			:"编辑日志",
		"blog!viewBlog.do"			:"阅读日志",
		"photo.do"					:"最近相册",
		"photo!listInGroup.do"		:"好友分组相册",
		"photo!listInFriend.do"		:"好友相册",
		"sysImage.do"				:"系统图库",
		"photo!myAlbum.do"			:"我的相册",
		"photo!preUp.do"			:"上传照片",
		"photo!upload.do"			:"上传照片",
		"photo!preCreate.do"		:"创建相册",
		"photo!listPhoto.do"    :"照片列表",
		"photo!listMyPhoto.do"  :"我的照片列表",
		"photo!view.do"         :"查看照片",
		"friend.do"					:"我的好友",
		"friend!listFriendsByCategory.do":"我的好友",
		"friend!listCity.do":"同城成员",
		"share!hotShares.do":"热门分享",
		"share!listHotSharesByCategory.do":"分类下的热门分享",
		"share.do":"好友分享",
		"share!listMyShares.do":"我的分享",
		"subscribe.do":"订阅排行",
		"subscribe!listCity.do":"热门订阅",
		"subscribe!listSchool.do":"热门订阅",
		"subscribe!listCityHotSub.do":"同城订阅",
		"popularity!listCity.do":"同城人气之星",
		"activity!city.do"		:"同城活动",
		"activity!mylist.do"		:"我参与的活动",
		"topic!news.do"        :"最新主题",
		"topic.do"             :"我发表的主题",
		"topic!replied.do"     :"我参与的主题",
		"group!my.do"          :"我的群组",
		"group!all.do"         :"全站群组",
		"group!city.do"        :"同城群组",
		"group!home.do"        :"群组",
		"topic!view.do"        :"查看主题",
		"topic!add.do"         :"发表主题",
		"topic!edit.do"        :"编辑主题",
		"group!toInvite.do"    :"邀请好友加入群组",
		"group!manage.do"      :"管理群组",
		"invite.do"            :"邀请好友",
		"invite!other.do"      :"邀请好友"
		
		                         
		   
	};
	if(title==null){
		var url = window.location.href;
		if(url.indexOf("?")>0){
			url = url.substring(0,url.indexOf("?"));
		}
		var p = url.lastIndexOf("/");
		url = url.substring(p+1);
		title = titles[url];
		if(title==null){
			title = "YY365";
		}else{
			title = "YY365_"+title
		}
	}
	document.title = title;
}

function getTextByCharNum(txt,num){
	num=num*2-2;
	var Len=txt.replace(/(\r\n)$/gm,'').split(''); 
	var len=0;
	var s='';
	for(var i=0;i<Len.length;i++){
	
		if(Len[i].charCodeAt(0)>256){
			len+=2;	
		}else{
			len++;
		}	
		if (len>=num){
			if (i+1<Len.length){
				if(Len[i+1].charCodeAt(0)>256){
					s+="...";	
					return s;
				}else{
					s+=Len[i+1];
					if((i+1)==Len.length-1){
					return s;
					}else{
					s+="...";			
					return s;
					}
				}	
			}else{
				if(i==Len.length-1){
				return s;
				}else{
				s+="...";			
				return s;
				}
			}
		}else{
		
			s+=Len[i];
			
		}
	}
	return s;
}

	function subStr(txt,maxSize){
		if(txt != null && txt.length>0){
			if(txt.length<=maxSize){
				 document.write(txt);
			}else{
				document.write(txt.substring(0,maxSize-2)+"...");
			}
		}
	}
	    function sImg(img){
	   	var simg=new Image();
	   	simg.src=img.src;
	   	var rate1 = simg.width /100;
	   	var rate2 = simg.height /75;
	   	var rate = rate1;
	   	if(rate2 > rate){
	   		rate = rate2;
	   	}
	   	if(rate>1){
	   		img.height = simg.height/rate;
	   		img.width = simg.width/rate;
	   	}else{
	   		img.height = simg.height;
	   		img.width = simg.width;
	   	}
	}
	
		//去掉空格
	function trim(str) {
		return (str + '').replace(/(\s+)$/g, '').replace(/^\s+/g, '');
	}
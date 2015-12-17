function createPageBar(pageNumber,pageSize,pageIndex,total,callback,viewedNum,param){
	var sb = '<div class="message">共<i class="blue">' + total + '</i>条记录，当前显示第&nbsp;<i class="blue">' + pageIndex + '&nbsp;</i>页</div><ul class="paginList">';
	if(pageNumber<=1){
		sb += '</ul>';
		return sb;
	}	
	
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
	    sb += '<li class="paginItem"><a href="'+curl+'"><span class="pagepre blue"></span></a></li>';
	} else {
		sb += '<li class="paginItem"><a href="javascript:void();"><span class="pagepre"></span></a></li>';
	}
	
	for(var i=firstNumber;i<=lastNumber;i++){
		curl = callback + '(' + i + ',' + pageSize+')';
		if(i==pageIndex){
			sb += '<li class="paginItem current"><a href="javascript:void();">'+ i +'</a></li>';
		}else{
			sb += '<li class="paginItem"><a href="'+curl+'">'+ i + '</a></li>';
		}
	}

	if(pageIndex<pageNumber){
		curl = callback + '(' + (pageIndex+1) + ',' + pageSize+')';
		sb += '<li class="paginItem"><a href="'+curl+'"><span class="pagenxt"></span></a></li>';		
	} else {
		sb += '<li class="paginItem"><a href="javascript:void();"><span class="pagenxt"></span></a></li>';
	}
	
	sb += '</ul>';
	return sb;
}

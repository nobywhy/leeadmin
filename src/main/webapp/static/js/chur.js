function calert(type,title,content,btntext){////类型有5种info，success，warning，primary，danger
   var str = "";
   if(type == 'primary')
   {
      if(title == '' || title.length == 0)title = "权限提示";
      if(content == '' || content.length == 0)content = "对不起您没有此项操作权限！";
      if(btntext == '' || btntext.length == 0) btntext = "确定";
   }else if(type == 'danger'){
      if(title == '' || title.length == 0)title = "出错提示";
      if(content == '' || content.length == 0)content = "抱歉，操作失败！";
      if(btntext == '' || btntext.length == 0) btntext = "确定";
   }else if(type == 'success'){
      if(title == '' || title.length == 0)title = "成功提示";
      if(content == '' || content.length == 0)content = "恭喜，操作成功！";
      if(btntext == '' || btntext.length == 0) btntext = "确定";
   }else if(type == 'warning'){
      if(title == '' || title.length == 0)title = "警告提示";
      if(content == '' || content.length == 0)content = "警告！数据无价，请谨慎操作！";
      if(btntext == '' || btntext.length == 0) btntext = "确定";
   }else{
      if(title == '' || title.length == 0)title = "询问提示";
      if(content == '' || content.length == 0)content = "你确定要删除这条数据吗？";
      if(btntext == '' || btntext.length == 0) btntext = "确定";
   }
   
  str += '<div class="chur-alert" >';
  str += '<div class="alert alert-'+type+'" style="display:block;">';
  str += ' <a class="close closed" href="javascript:closed();">×</a>';
  str += '<h4 class="alert-heading">'+title+'</h4>';
  str += '<div class="context">'+content+'</div>';
  if(type == 'info'){
   str += '<div class="rightbtn"><input type="button" class="btnf closed btnf-'+type+'" id="yes" onclick="closed();" value="确定">&nbsp;<input type="button" class="btnf closed btnf-info" onclick="closed();" id="no" value="取消">&nbsp;</div>';
  }else{
    str += '<div class="rightbtn"><input type="button" class="btnf closed btnf-'+type+'" onclick="closed();" value="'+btntext+'">&nbsp;</div>';
  }
  
  str += '</div></div><iframe class="alert-modal"></iframe>';
   
   return str;
}

function closed(){
    $("#churalert").hide();
}


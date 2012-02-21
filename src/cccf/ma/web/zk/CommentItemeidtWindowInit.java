package cccf.ma.web.zk;
import java.util.List;
import java.util.Map;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import cccf.ma.service.*;
import cccf.ma.model.*;

public class CommentItemeidtWindowInit extends org.zkoss.zkplus.databind.AnnotateDataBinderInit{
 
	 Map params = Executions.getCurrent().getArg();
	 CommentItemInfo commentItem;
	 
	  public void doAfterCompose(Page page,org.zkoss.zk.ui.Component[] comps)throws Exception {
		 commentItem=(CommentItemInfo)params.get("commentItem");		 
	
		
		 if(commentItem==null){
		 	commentItem=new CommentItemInfo();
		 }
		 
		 	  					
		 List<UserInfo> userList=UserInfoServiceUtil.getAll();
		 page.setVariable("userList",userList);		 
	   
	   page.setVariable("commentItem", commentItem);
	   super.doAfterCompose(page,(org.zkoss.zk.ui.Component[]) comps); 
	     
	  }
 }
    
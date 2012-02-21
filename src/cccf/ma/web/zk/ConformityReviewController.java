package cccf.ma.web.zk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.modellite.spring.BeanAdapter;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cccf.ma.function.Functions;
import cccf.ma.model.ObjectApproveInfo;
import cccf.ma.service.ConformityReviewService;

import com.aidi.bpm.zk.BpmZkUtil;

/**
 * @author JOINYO
 * @E-mail JOINYO@YEAH.NET
 * @date 创建于2011-5-19上午12:26:18
 * @version 1.0

 * @describe
 * ALL RIGHTS RESERVED,COPYRIGHT(C) FCH LIMITED 2011
 */
public class ConformityReviewController extends GenericForwardComposer{
	private static final long serialVersionUID = 1L;
	Map params=Executions.getCurrent().getArg();
	private Map applyInfo;
	private List<Map> tasks;//接收传递过来的任务列表
	private ConformityReviewService conformityReviewService;
	private Grid appoveGrid;
	private List<ObjectApproveInfo> fileApprovelist;
	private UserInfo user;
	private Button submitBtn;
	private Window cofirmWin;
	private String applyId;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if((Boolean) params.get("isShowGrid")){
			Long taskInstanceId=(Long) tasks.get(0).get("taskId");
			System.out.println("taskInstanceId"+taskInstanceId);
			BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);
			fileApprovelist=new ArrayList<ObjectApproveInfo>();
		}
	}

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) {

		user=UserInfoServiceUtil.getCurrentLoginUser();
		applyId = (String) params.get("applyId");
		applyInfo = Functions.getConformityInfo(applyId);//取得申请信息
		System.out.println(applyInfo);
		page.setAttribute("applyInfo", applyInfo);
		tasks = (List<Map>) params.get("tasks");// 取得任务列表
		System.out.println(tasks);// 打印
		conformityReviewService = BeanAdapter.getBean("ConformityReviewService",
				ConformityReviewService.class);
		//根据参数设置
		page.setAttribute("isCreateOperate", params.get("isCreateOperate"));
		page.setAttribute("isShowGrid", params.get("isShowGrid"));
		return super.doBeforeCompose(page, parent, compInfo);
	}
	
	public void onApprove(ForwardEvent event)
	{
		Toolbarbutton bar=(Toolbarbutton) event.getOrigin().getTarget();
		String attId=(String) bar.getAttribute("attId");//取得附件文件的ID
		Label shLab=(Label) bar.getFellow(attId+"_shLab");//取得审批结果控件对象
		Label yjLab=(Label) bar.getFellow(attId+"_yjLab");//取得审批意见控件对象

		shLab.setValue("通过");
		yjLab.setValue("同意");
		
		String shvalue=shLab.getValue();//取得审批结果
		String yjvalue=yjLab.getValue();//取得审批意见对象
		//创建一个审批对象的对象
		ObjectApproveInfo approveInfo=new ObjectApproveInfo();
		approveInfo.setApproveResult(shvalue);
		approveInfo.setApproveMemo(yjvalue);
		approveInfo.setApproverID(user.getId());
		approveInfo.setApproverName(user.getName());
		approveInfo.setApproveCode("符合性审查");
		approveInfo.setObjectId(attId);
		approveInfo.setObjectType("Attachment");
		System.out.println("approveInfo="+approveInfo);
		fileApprovelist.add(approveInfo);
	//	objectApproveService.save(approveInfo);
		bar.setDisabled(true);
	}

	public void onSubmit() throws InterruptedException
	{  
		if(Messagebox.show("是否确认提交表单信息？","操作提示",Messagebox.OK|Messagebox.NO,Messagebox.QUESTION)==Messagebox.OK)
		{
			 List approveRows = appoveGrid.getRows().getChildren();
			 Row row = (Row)approveRows.get(0);
			 Listbox resultListbox = (Listbox)row.getFellow("resultListbox");
		 
			 Row row1 = (Row)approveRows.get(1);
			 String approveMemo = ((Textbox)row1.getFellow("approveMemo")).getValue();
			 String approveResult =""; 
			 if (resultListbox.getSelectedItem() != null) {
			      approveResult = resultListbox.getSelectedItem().getLabel(); 
			 }
			 
			conformityReviewService.doSubmit(applyId, approveResult,approveMemo,user.getId(), fileApprovelist);
			Messagebox.show("信息提交成功!","操作提示",Messagebox.OK,Messagebox.INFORMATION);
			
			Events.postEvent(ConfirmityListController.ON_REFRESH,cofirmWin.getParent(),null);
		    cofirmWin.detach();
		}
	    
		
	}
	
	public void onSelectNext(ForwardEvent event)
	{
		Listbox resultListbox=(Listbox) event.getOrigin().getTarget();
		String result=resultListbox.getSelectedItem().getLabel();
	 
		if ("进行会审".equals(result)) {
			submitBtn.setDisabled(true);
		}
		else
			submitBtn.setDisabled(false);
	}
}

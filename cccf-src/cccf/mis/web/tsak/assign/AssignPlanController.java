package cccf.mis.web.tsak.assign;

import java.util.List;
import java.util.Map;

import net.modellite.spring.BeanAdapter;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import cccf.ma.function.Functions;
import cccf.ma.model.AssignPlan;
import cccf.ma.service.AssignPlanService;

public class AssignPlanController
		extends GenericForwardComposer
{
	private Radiogroup			isFCK;
	private Label				fckDaysLabel;
	private Doublebox			fckDays;
	private Row					fcktype;
	private Radiogroup			rg_inspectPlace;
	private Radiogroup			isDoProductTest;
	private Textbox				approveMemo;
	Map							apply				= (Map) Executions.getCurrent().getArg().get("apply");
	
	AssignPlanService svc = BeanAdapter.getBean("AssignPlanService", AssignPlanService.class);
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4553954871446982220L;
	
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		Map selectedItem = (Map) Executions.getCurrent().getArg().get("apply");
		String applyno = (String) selectedItem.get("applyno");// 申请号
		StringBuffer hql = new StringBuffer("select new map(applyno as applyno")
	        .append(",applyno as no")
	        .append(",applyEnterprise.name as enterprise")  // 企业名称
	        .append(",applyEnterprise.state as state")      // 国家
	        .append(",applyEnterprise.location as region")  // 地区 
	        .append(",businessType as business_type")        // 业务类型
	        .append(",productCatalog as product_catalog")    //产品类型
	        .append(",applyType as apply_type")              // 申请类型
	        .append(",applydate as apply_date")              //申请日期
			.append(",contractAgree as contractAgree")
			.append(",contractYear as contractYear")
			.append(",contractMonth as contractMonth")
			.append(",contractApprove as contractApprove")
			.append(")")
			.append(" from ApplicationPublicInfo o")
			.append(" where applyno='").append(applyno).append("'"); 
		Map applyInfo = (Map) svc.querySingleResult(hql.toString());
	    
		List<Map> listProductModel  = Functions.getProductInfoListByApplyno(applyno);
		page.setAttribute("applyInfo", applyInfo);  //  申请信息 
		page.setAttribute("products", listProductModel); //产品信息
		
		hql = new StringBuffer("select new map( ") 
	        .append("applyEnterprise.name as name")  // 申请企业
	        .append(",applyEnterprise.faxNum as faxNum")      // 专真
	        .append(",applyEnterprise.contactPerson as contactPerson")  // 联系人
	        .append(",applyEnterprise.telephoneNum as telephoneNum")        // 联系电话
	        .append(",applyEnterprise.contactAddress as contactAddress")    //通讯地址
	        .append(",applyEnterprise.postalcode as postalcode")              //邮政编码
	        .append(",applyEnterprise.nameEn as nameEn")              //企业英文名
			.append(",applyEnterprise.contactAddressEn as contactAddressEn")      //企业英文地址 
			.append(")")
			.append(" from ApplicationPublicInfo o")
			.append(" where applyno='").append(applyno).append("'"); 
		
		Map applyEnt = (Map) svc.querySingleResult(hql.toString());
		page.setAttribute("applyEnt", applyEnt);  //  申请企业信息
		
		return super.doBeforeCompose(page, parent, compInfo);
	}
	public void onSubmit()
			throws InterruptedException
	{ 
		String usid = UserInfoServiceUtil.getCurrentLoginUser().getId();
		String approveResult = isFCK.getSelectedItem().getValue();
		String approveMemoValue = approveMemo.getText();
		// 派组计划
		AssignPlan plan = new AssignPlan();
		plan.setApplyno((String) apply.get("applyno"));
		if ("需要工厂检查".equals(approveResult))
		{
			plan.setDoFactoryInspect(true);
			plan.setInspectDays(fckDays.doubleValue());
			plan.setInspectPlace(rg_inspectPlace.getSelectedItem().getLabel());
		} else
		{
			plan.setDoFactoryInspect(false);
		}
		if ("是".equals(isDoProductTest.getSelectedItem().getLabel()))
		{
			plan.setDoProductTest(true);
		} else
		{
			plan.setDoProductTest(false);
		}
		svc.doAssignPlan(plan, usid, approveResult, approveMemoValue);
		// 操作成功
		self.setAttribute("submit", "OK");
		self.setVisible(false);
	}
	public void onIsFckChange(ForwardEvent event)
	{
		Radio radio = isFCK.getItemAtIndex(0);
		// if(radio.isChecked();
		fckDaysLabel.setVisible(radio.isChecked());
		fckDays.setVisible(radio.isChecked());
		fcktype.setVisible(radio.isChecked());
	}
}

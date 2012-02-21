package cccf.mis.web.tsak.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.modellite.spring.BeanAdapter;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Radio;
import cccf.ma.function.Functions;
import cccf.ma.model.ContractFeeItem;
import cccf.ma.model.TechnicalEvaluationReport;
import cccf.ma.model.TechnicalEvaluationReportOpinion;
import cccf.ma.service.TechnicalEvaluationReportCreateService;

public class ReportController
		extends GenericForwardComposer
{
	private Listbox							limitLbx;
	private Listbox							noLbx;
	private Radiogroup						optypeRgp;
	private Textbox							opinionTbx;
	private Textbox							tb_memo;
	private Listbox							lb_products;
	// private Textbox tb_certificateValid;
	private String							operatetype			= null;
	private List<String>					modelIds			= null;
	private Map								selectItem;
	TechnicalEvaluationReportCreateService	svc					= BeanAdapter.getBean("TechnicalEvaluationReportCreateService", TechnicalEvaluationReportCreateService.class);
	/**
	 * 
	 */
	private static final long				serialVersionUID	= 6803755799861930720L;
	public void onOpenContract(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
		String contractNo = (String) event.getOrigin().getTarget().getAttribute("contract");
		// TODO ...
		StringBuffer buf = new StringBuffer();
		buf.append("select new map(c.contractNo as contractNo");// 合同号,c.auditYear,c.auditMonth
		buf.append(",c.agreedAuditTeams as greedAuditTeams");// 合同信息,是否同意派发审核组
		buf.append(",c.auditYear as auditYear");// 审核时间年
		buf.append(",c.auditMonth as auditMonth");// 审核时间月
		buf.append(",c.preverify as preverify");// 是否预审核
		buf.append(",c.preverifyYear as preverifyYear");// 预审时间年
		buf.append(",c.preverifyMonth as preverifyMonth");// 预审时时间月
		buf.append(",c.status as status");// 合同状态
		buf.append(",c.createDate as createDate");// 创建时间
		buf.append(",c.creater as creater");// 创建人
		buf.append(",c.feeTotal as feeTotal");// 创建人
		buf.append(")");
		buf.append(" from Contract c where c.contractNo='" + contractNo + "'");
		Map map = (Map) svc.querySingleResult(buf.toString());
		System.out.println(map);
		Executions.getCurrent().setAttribute("contract", map);
		Executions.getCurrent().setAttribute("bills", getBills(contractNo));
		
		Window win = (Window) Executions.createComponents("/views/public/Contract.zul", null, null);
		win.setTitle(" ");
		win.setBorder("normal");
		win.setClosable(true);
		win.setWidth("85%");
		win.setHeight("85%");
		win.doModal();
	}
	private Map getBills(String contractNo)
	{
		HashMap bills = new HashMap();
		StringBuffer hql = new StringBuffer("from ContractFeeItem where contract.contractNo='").append(contractNo).append("'").append(" order by itemName asc");
		List<ContractFeeItem> list = svc.getResultList(hql.toString());
		for (int i = 0; i < list.size(); i++)
		{
			ContractFeeItem item = list.get(i);
			bills.put("unit_r" + (1 + i), item.getUnit());
			bills.put("unitPrice_r" + (1 + i), item.getUnitPrice());
			bills.put("memo_r" + (1 + i), item.getMemo() == null || item.getMemo().isEmpty() ? "-" : item.getMemo());
			bills.put("quantity_r" + (1 + i), item.getQuantity());
			bills.put("quantity_1_r" + (1 + i), item.getQuantity_1());
			bills.put("price_r" + (1 + i), item.getPrice());
		}
		return bills;
	}
	public void onOpenApply(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
		String applyno = (String) event.getOrigin().getTarget().getAttribute("applyno");
		// TODO getMap...
		Map applyInfo = EvalQueryUtils.getApplyInfo(svc, applyno);
		List appAttachments = EvalQueryUtils.getAppAttachments(svc, applyno);
		List appApprove = EvalQueryUtils.getAttachmentApprove(svc, applyno);
		EvalQueryUtils.buildObjectLinkApprove(appAttachments, appApprove);
		applyInfo.put("attachments", appAttachments);
		List productList = EvalQueryUtils.getProductlList(svc, applyno);
		List productModelList = EvalQueryUtils.getProductModelList(svc, applyno);
		List productModelAttachmentList = EvalQueryUtils.getProductModelAttachmentList(svc, applyno);
		EvalQueryUtils.buildProductLinkProductModel(productList, productModelList);
		EvalQueryUtils.buildProductModelLinkAttachment(productModelList, productModelAttachmentList);
		applyInfo.put("productList", productList);
		Executions.getCurrent().setAttribute("applyInfo", applyInfo);
		Window win = (Window) Executions.createComponents("/views/public/Apply_Detail_ap.zul", null, null);
		win.setTitle(" ");
		win.setBorder("normal");
		win.setClosable(true);
		win.setWidth("85%");
		win.setHeight("85%");
		win.doModal();
	}
	public void onOpenSurvey(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
		String srid = (String) event.getOrigin().getTarget().getAttribute("survey");
		// TODO getMap...
		Map param = EvalQueryUtils.getSurveyReport(svc, srid);
		System.out.println("param=" + param);
		Executions.getCurrent().setAttribute("surveyReport", param);
		Window win = (Window) Executions.createComponents("/views/public/SurveyReport_read.zul", null, null);
		win.setTitle(" ");
		win.setBorder("normal");
		win.setClosable(true);
		win.setWidth("85%");
		win.setHeight("85%");
		win.doModal();
	}
	@SuppressWarnings({ "rawtypes" })
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		modelIds = new ArrayList<String>();
		// 数据
		Map applyInfo = null;
		List<Map> listProductModel;// 申请信息
		// Map apply = (Map) Executions.getCurrent().getArg().get("apply");
		selectItem = (Map) Executions.getCurrent().getArg().get("apply");;
		String applyno = (String) selectItem.get("applyno");// 申请号
		StringBuffer hql = new StringBuffer("select new map(applyno as applyno").append(",applyno as no").append(",applyEnterprise.name as enterprise")
				// 企业名称
				.append(",applyEnterprise.state as state")
				// 国家
				.append(",applyEnterprise.location as region")
				// 地区
				.append(",businessType as businessType")
				// 业务类型
				.append(",productCatalog as productCatalog")
				// 产品类型
				.append(",applyType as applyType")
				// 申请类型
				.append(",applydate as applydate")
				// 申请日期
				.append(",contractAgree as contractAgree").append(",contractYear as contractYear").append(",contractMonth as contractMonth").append(",contractApprove as contractApprove").append(")").append(" from ApplicationPublicInfo o")
				.append(" where applyno='").append(applyno).append("'");
		applyInfo = (Map) svc.querySingleResult(hql.toString());
		listProductModel = Functions.getProductInfoListByApplyno(applyno);
		//
		Executions.getCurrent().setAttribute("applyInfo", applyInfo);
		/** 申请企业信息 **/
		StringBuffer hqlEnt = new StringBuffer("select new map( ").append("o.applyEnterprise.name as name") // 申请企业
				.append(",o.applyEnterprise.faxNum as faxNum") // 专真
				.append(",o.applyEnterprise.contactPerson as contactPerson") // 联系人
				.append(",o.applyEnterprise.telephoneNum as telephoneNum") // 联系电话
				.append(",o.applyEnterprise.contactAddress as contactAddress") // 通讯地址
				.append(",o.applyEnterprise.postalcode as postalcode") // 邮政编码
				.append(",o.applyEnterprise.nameEn as nameEn") // 企业英文名
				.append(",o.applyEnterprise.contactAddressEn as contactAddressEn") // 企业英文地址
				.append(",c.contractNo as contractNo")// 合同
				.append(")").append(" from ApplicationPublicInfo o,Contract c").append(" where c.applyno=o.applyno and o.applyno='").append(applyno).append("'");
		Map applyEnt = (Map) svc.querySingleResult(hqlEnt.toString());
		Executions.getCurrent().setAttribute("applyEnt", applyEnt); // 申请企业信息
		Executions.getCurrent().setAttribute("products", listProductModel);
		return super.doBeforeCompose(page, parent, compInfo);
	}
	public void onSubmit(ForwardEvent event)
			throws InterruptedException
	{
		if ((modelIds == null || modelIds.size() == 0) && "发证".equals(operatetype))
		{
			Messagebox.show("请选择评定产品");
			return;
		}
		if (Messagebox.show("是否确认提交表单信息？", "操作提示", Messagebox.OK | Messagebox.NO, Messagebox.QUESTION) == Messagebox.OK)
		{
			// 技术评定报告
			TechnicalEvaluationReport report = new TechnicalEvaluationReport();
			report.setCertificateValid(limitLbx.getSelectedItem().getLabel());
			report.setEntTollCode(noLbx.getSelectedItem().getLabel());
			report.setOperateType(operatetype);
			String applyno = (String) selectItem.get("applyno");// 申请号
			report.setApplyno(applyno);
			TechnicalEvaluationReportOpinion opinion = new TechnicalEvaluationReportOpinion();
			opinion.setPhase("生成报告");
			opinion.setOpinionContent(opinionTbx.getValue());
			opinion.setMemo(tb_memo.getText());
			String approveResult = "提交";
			String approveMemoValue = tb_memo.getText();
			svc.doReportCreate(report, opinion, modelIds, UserInfoServiceUtil.getCurrentLoginUser().getId(), approveResult, approveMemoValue);
			self.setAttribute("submit", "OK");
			self.setVisible(false);
		}
	}
	public void onCheckOperateType(ForwardEvent event)
	{
		Radio operatetypeRdo = (Radio) event.getOrigin().getTarget();
		operatetype = operatetypeRdo.getLabel();
		if ("发证".equals(operatetype))
		{
			modelIds.clear();
			Set<?> items = lb_products.getSelectedItems();
			int i = 0;
			for (Iterator it = items.iterator(); it.hasNext();)
			{
				Listitem item = (Listitem) it.next();
				String id = (String) item.getValue();
				// System.out.println(map);
				// String id = (String)map.get("id");
				modelIds.add(id);
				if (item.getAttribute("isMainModel") != null && (Boolean) item.getAttribute("isMainModel"))
				{
					i++;
				}
			}
			opinionTbx.setValue("符合规则要求，建议发证" + i + "张;");
		} else
			opinionTbx.setValue("");
	}
}

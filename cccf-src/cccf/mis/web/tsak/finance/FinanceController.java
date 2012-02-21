package cccf.mis.web.tsak.finance;

import java.util.HashMap;
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
import org.zkoss.zul.Textbox;
import cccf.ma.function.Functions;
import cccf.ma.model.ContractFeeItem;
import cccf.ma.service.ContractService;
import cccf.ma.service.FinanceService;

public class FinanceController
		extends GenericForwardComposer
{
	private Textbox			approveMemo;
	Map						selectedItem	= (Map) Executions.getCurrent().getArg().get("contract");
	/**
	 * 
	 */
	private ContractService	svc				= BeanAdapter.getBean("ContractService", ContractService.class);
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		List<Map> listProductModel;// 申请信息
		selectedItem = (Map) Executions.getCurrent().getArg().get("contract");
		String applyno = (String) selectedItem.get("applyno");// 申请号
		StringBuffer hql = new StringBuffer("select new map(applyno as applyno").append(",applyno as no").append(",applyEnterprise.name as enterprise")
				// 企业名称
				.append(",applyEnterprise.state as state")
				// 国家
				.append(",applyEnterprise.location as region")
				// 地区
				.append(",businessType as business_type")
				// 业务类型
				.append(",productCatalog as product_catalog")
				// 产品类型
				.append(",applyType as apply_type")
				// 申请类型
				.append(",applydate as apply_date")
				// 申请日期
				.append(",contractAgree as contractAgree").append(",contractYear as contractYear").append(",contractMonth as contractMonth").append(",contractApprove as contractApprove").append(")").append(" from ApplicationPublicInfo o")
				.append(" where applyno='").append(applyno).append("'");
		Map applyInfo = (Map) svc.querySingleResult(hql.toString());
		listProductModel = Functions.getProductInfoListByApplyno(applyno);
		page.setAttribute("applyInfo", applyInfo);
		page.setAttribute("products", listProductModel);
		String contractNo = (String) selectedItem.get("contractno");
		Map bills = getBills(contractNo);
		bills.put("feeTotal", selectedItem.get("feeTotal"));
		bills.put("contractNo", selectedItem.get("contractNo"));
		page.setAttribute("bills", bills);
		// TODO Auto-generated method stub
		return super.doBeforeCompose(page, parent, compInfo);
	}
	@SuppressWarnings("unchecked")
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
	private static final long	serialVersionUID	= 2602989360335742459L;
	public void onSubmit(ForwardEvent event)
	{
		FinanceService svc = BeanAdapter.getBean("FinanceService", FinanceService.class);
		String contractNo = (String) selectedItem.get("contractno");
		// 修改 1、从控件中取流程下一步值
		String approveMemoValue = approveMemo.getValue();
		String approveResult = "提交";
		svc.doChargeConfirm(contractNo, UserInfoServiceUtil.getCurrentLoginUser().getId(), approveResult, approveMemoValue);
		// 反回正确提交信息
		self.setAttribute("submit", "OK");
		self.setVisible(false);
	}
}

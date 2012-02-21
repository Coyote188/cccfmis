package cccf.mis.web.tsak.contract.approval;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.modellite.rim.itext.PdfFileUtil;
import net.modellite.spring.BeanAdapter;
import openjframework.service.UserInfoServiceUtil;
import openjframework.util.ZkFileUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import cccf.ma.function.Functions;
import cccf.ma.model.Certification;
import cccf.ma.model.ContractFeeItem;
import cccf.ma.service.ContractService;
import cccf.mis.web.pub.OnlinFileController;
import com.aidi.bpm.zk.BpmZkUtil;
import com.aidi.core.service.BaseDAOServcieUtil;

public class AuditController
		extends GenericForwardComposer
{
	private Map					selectedItem;
	// 控件
	private Grid				appoveGrid;
	private Include				bills;
	private ContractService		svc					= BeanAdapter.getBean("ContractService", ContractService.class);
	private static final long	serialVersionUID	= -8070842842645116085L;
	Map   applyInfo;
	@SuppressWarnings({ "rawtypes" })
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		// 数据
	 
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
		applyInfo = (Map) svc.querySingleResult(hql.toString());
		listProductModel = Functions.getProductInfoListByApplyno(applyno);
		page.setAttribute("applyInfo", applyInfo);
		page.setAttribute("products", listProductModel);
		String contractNo = (String) selectedItem.get("contractno");
		Map bills = getBills(contractNo);
		bills.put("feeTotal", selectedItem.get("feeTotal"));
		bills.put("contractNo", selectedItem.get("contractNo"));
		page.setAttribute("bills", bills);
		return super.doBeforeCompose(page, parent, compInfo);
	}
	public void onOpenContract(ForwardEvent event)
			throws IOException, SuspendNotAllowedException, InterruptedException
	{
		String path = ZkFileUtil.getRealUrl("/filemodels/contract_template.pdf");
		String filePath = Certification.mkDir(Certification.repoPath);
		
		Map<String, String> fieldsArgs = new HashMap<String, String>();
		Map<?, ?> map = (Map<?, ?>) arg.get("contract");
		String certNo = (String) map.get("contractno");
		//
		fieldsArgs.put("contractNo", certNo);
		fieldsArgs.put("productType",(String) applyInfo.get("product_catalog"));
		fieldsArgs.put("eterprise_name",(String) applyInfo.get("enterprise"));
		
		String file_path = filePath + "/" + certNo + ".pdf";
		PdfFileUtil.simpleMakPdfFile(path,file_path, fieldsArgs);
		//打开
		
		Map<String, String> arg = new HashMap<String, String>();
		arg.put("filepath", Certification.repoPath+"/"+certNo + ".pdf");
		Window win = OnlinFileController.createSee(arg);
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
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
		// 流程下一步选择下拉框
		String applyno = (String) selectedItem.get("applyno");// 申请号
		StringBuffer hql = new StringBuffer("select pl.taskInstanceId from ProcessLog pl,ApplicationInfo a where pl.boType='ApplicationInfo' and pl.end is null ").append(" and pl.boId=a.id").append(" and a.sioid='").append(applyno).append("'");
		List<?> l = BaseDAOServcieUtil.findByQueryString(hql.toString());
		if (l != null && l.size() > 0)
		{
			Long taskInstanceId = (Long) l.get(0);
			BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);
		}
	}
	public void onSubmit(ForwardEvent event)
			throws InterruptedException
	{
		String contractNo = (String) selectedItem.get("contractno");
		int status = 2;
		// 修改 1、从控件中取流程下一步值
		List<?> approveRows = appoveGrid.getRows().getChildren();
		Row row = (Row) approveRows.get(0);
		Listbox resultListbox = (Listbox) row.getFellow("resultListbox");
		Row row1 = (Row) approveRows.get(1);
		String approveMemo = ((Textbox) row1.getFellow("approveMemo")).getValue();
		String approveResult = "";
		if (resultListbox.getSelectedItem() != null)
		{
			approveResult = resultListbox.getSelectedItem().getLabel();
		}
		if ("通过".equals(approveResult))
		{
			status = 4;
		} else
		{
			status = 5;
		}
		svc.doCheck(contractNo, status, UserInfoServiceUtil.getCurrentLoginUser().getId(), approveResult, approveMemo);
		self.setAttribute("submit", "OK");
		self.setVisible(false);
	}
}

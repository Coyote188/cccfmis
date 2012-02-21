package cccf.mis.web.tsak.contract;

import java.util.ArrayList;
import java.util.Date;
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
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import cccf.ma.function.Functions;
import cccf.ma.model.Contract;
import cccf.ma.model.ContractFeeItem;
import cccf.ma.service.ContractService;

import com.aidi.core.service.BaseDAOServcieUtil;

public class ContractController
		extends GenericForwardComposer
{
	ContractService svc = BeanAdapter.getBean("ContractService", ContractService.class);
	// 控件
	private Include bills;
	private Textbox				approveMemo;
	private static final long	serialVersionUID	= -8070842842645116085L;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		// 数据
		Map applyInfo = null;
		List<Map> listProductModel;// 申请信息
		Map apply = (Map) Executions.getCurrent().getArg().get("apply");
		String applyno = (String) apply.get("applyno");// 申请号
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
		applyInfo = (Map) svc.querySingleResult(hql.toString());
		
		listProductModel = Functions.getProductInfoListByApplyno(applyno); 
		
		page.setAttribute("applyInfo", applyInfo);
		page.setAttribute("products", listProductModel);
		 
		// TODO 收费项目 
		Map bills =getBills(applyno); 
		page.setAttribute("bills", bills); 
		
		return super.doBeforeCompose(page, parent, compInfo);
	}
	
	private Map getBills(String applyno){
		HashMap bills = new HashMap();
		StringBuffer hql = new StringBuffer("from ContractFeeItem where contract.applyno='")
		                 .append(applyno).append("'")
		                 .append(" order by itemName asc") ;
		List<ContractFeeItem> list = svc.getResultList(hql.toString());
		for(int i=0 ;i<list.size();i++){
			ContractFeeItem item = list.get(i);
			bills.put("unit_r"+(1+i), item.getUnit());
			bills.put("unitPrice_r"+(1+i), item.getUnitPrice());
			bills.put("memo_r"+(1+i), item.getMemo()==null||item.getMemo().isEmpty()?"-":item.getMemo() );
			bills.put("quantity_r"+(1+i), item.getQuantity());
			bills.put("quantity_1_r"+(1+i), item.getQuantity_1());
			bills.put("price_r"+(1+i), item.getPrice());  
		}
		//hql = new StringBuffer("select new map(contractNo) from Contract where applyno='").append(applyno).append("'") ;
		//Map map=(Map)svc.querySingleResult(hql.toString());
		//bills.put("feeTotal",map.get("feeTotal") );
	    //bills.put("contractNo",map.get("contractNo"));
		return bills;
	}
	
	 
	public void onSubmit(ForwardEvent event)
			throws InterruptedException
	{
		
		Contract contract = new Contract();
		Map<?, ?> applyInfo = (Map<?, ?>) page.getAttribute("applyInfo");
		// 甲方填写
		contract.setApplyno((String) applyInfo.get("applyno"));
		contract.setAgreedAuditTeams((String) applyInfo.get("contractAgree"));
		contract.setAuditYear((String) applyInfo.get("contractYear"));
		contract.setAuditMonth((String) applyInfo.get("contractMonth"));
		contract.setPreverify((String) applyInfo.get("contractApprove"));
		// 乙方填写
		contract.setPreverifyMonth(applyInfo.get("preverifyMonth")==null?"": applyInfo.get("preverifyMonth").toString());
		contract.setPreverifyYear(applyInfo.get("preverifyYear")==null?"": applyInfo.get("preverifyYear").toString());
		//String feeTotal = (String) applyInfo.get("feeTotal");
		// 
		//if (feeTotal != null && !feeTotal.isEmpty())
			//contract.setFeeTotal(Double.valueOf(feeTotal));// 收费合计
		contract.setCreater(UserInfoServiceUtil.getCurrentLoginUser().getId());
		contract.setCreateDate(new Date());
		// TODO 合同收付费项
		Grid billGrid=(Grid)(bills.getFirstChild().getFellow("billGrid"));
		Decimalbox box=(Decimalbox) billGrid.getFellow("r10_c1");
		double totalPrice=box.getValue()==null?0:box.getValue().doubleValue();
		contract.setFeeTotal(totalPrice);
		List<ContractFeeItem> feeItems=getContractBill(billGrid);
		 
		String usid = UserInfoServiceUtil.getCurrentLoginUser().getId();
		String approveResult = "合同审核";
		String approveMemoValue = approveMemo.getText();
		svc.doDrawUpContract(contract, feeItems, usid, approveResult, approveMemoValue);
		Messagebox.show("信息提交成功!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
		self.setAttribute("submit", "OK");
		self.setVisible(false);
	}
	

	public List<ContractFeeItem> getContractBill(Grid billGrid){
		List<ContractFeeItem> theList=new ArrayList<ContractFeeItem>();
		ContractFeeItem  contractFeeItem;
		for (int i = 0; i < 9; i++) {
			contractFeeItem=new ContractFeeItem();
			if (i==1||i==2 || i==4 || i==6 || i==8 || i==5) {
			 
				if (i==5) {
					contractFeeItem.setItemName(((Label)billGrid.getCell(i, 1).getFirstChild()).getValue());
					contractFeeItem.setUnit(((Label)billGrid.getCell(i, 2).getFirstChild()).getValue());
					Decimalbox box=(Decimalbox)billGrid.getCell(i, 3).getFellow("r"+(i+1)+"_c1");
					Decimalbox box1=(Decimalbox)billGrid.getCell(i, 4).getFellow("r"+(i+1)+"_c2");
					Decimalbox box2=(Decimalbox)billGrid.getCell(i, 5).getFellow("r"+(i+1)+"_c3");
					Decimalbox box3=(Decimalbox)billGrid.getCell(i, 5).getFellow("r"+(i+1)+"_c4");
					Textbox box4=(Textbox)billGrid.getCell(i, 6).getFellow("r"+(i+1)+"_c5");
					contractFeeItem.setUnitPrice(checkData(box));
					contractFeeItem.setQuantity(checkData(box1));
					contractFeeItem.setQuantity_1(checkData(box2));
					contractFeeItem.setPrice(checkData(box3));
					contractFeeItem.setMemo(box4.getValue());
				}else if (i==1 || i==4) {
					contractFeeItem.setItemName(((Label)billGrid.getCell(i, 0).getFirstChild()).getValue());
					contractFeeItem.setUnit(((Label)billGrid.getCell(i, 1).getFirstChild()).getValue());
					Decimalbox box=(Decimalbox)billGrid.getCell(i, 2).getFellow("r"+(i+1)+"_c1");
					Decimalbox box1=(Decimalbox)billGrid.getCell(i, 3).getFellow("r"+(i+1)+"_c2");
					Decimalbox box2=(Decimalbox)billGrid.getCell(i, 4).getFellow("r"+(i+1)+"_c3");
					Decimalbox box3=(Decimalbox)billGrid.getCell(i, 4).getFellow("r"+(i+1)+"_c4");
					Textbox box4=(Textbox)billGrid.getCell(i, 5).getFellow("r"+(i+1)+"_c5");
					contractFeeItem.setUnitPrice(checkData(box));
					contractFeeItem.setQuantity(checkData(box1));
					contractFeeItem.setQuantity_1(checkData(box2));
					contractFeeItem.setPrice(checkData(box3));
					contractFeeItem.setMemo(box4.getValue());
				}else if (i==8){
					contractFeeItem.setItemName(((Label)billGrid.getCell(i, 0).getFirstChild()).getValue());
					Textbox box=(Textbox)billGrid.getCell(i, 1).getFellow("r"+(i+1)+"_c1");
					Decimalbox box1=(Decimalbox)billGrid.getCell(i, 2).getFellow("r"+(i+1)+"_c2");
					Decimalbox box2=(Decimalbox)billGrid.getCell(i, 3).getFellow("r"+(i+1)+"_c3");
					Decimalbox box3=(Decimalbox)billGrid.getCell(i, 4).getFellow("r"+(i+1)+"_c4");
					Textbox box4=(Textbox)billGrid.getCell(i, 5).getFellow("r"+(i+1)+"_c5");
					contractFeeItem.setUnit(box.getValue());
					contractFeeItem.setUnitPrice(checkData(box1));
					contractFeeItem.setQuantity(checkData(box2));
					contractFeeItem.setPrice(checkData(box3));
					contractFeeItem.setMemo(box4.getValue());
				}else{
					contractFeeItem.setItemName(((Label)billGrid.getCell(i, 0).getFirstChild()).getValue());
					contractFeeItem.setUnit(((Label)billGrid.getCell(i, 1).getFirstChild()).getValue());
					Decimalbox box=(Decimalbox)billGrid.getCell(i, 2).getFellow("r"+(i+1)+"_c1");
					Decimalbox box1=(Decimalbox)billGrid.getCell(i, 3).getFellow("r"+(i+1)+"_c2");
					Decimalbox box2=(Decimalbox)billGrid.getCell(i, 4).getFellow("r"+(i+1)+"_c3");
					Textbox box3=(Textbox)billGrid.getCell(i, 5).getFellow("r"+(i+1)+"_c4");
					contractFeeItem.setUnitPrice(checkData(box));
					contractFeeItem.setQuantity(checkData(box1));
					contractFeeItem.setPrice(checkData(box2));
					contractFeeItem.setMemo(box3.getValue());
				}
			}else {
				contractFeeItem.setItemName(((Label)billGrid.getCell(i, 1).getFirstChild()).getValue());
				contractFeeItem.setUnit(((Label)billGrid.getCell(i, 2).getFirstChild()).getValue());
				Decimalbox box=(Decimalbox)billGrid.getCell(i, 3).getFellow("r"+(i+1)+"_c1");
				Decimalbox box1=(Decimalbox)billGrid.getCell(i, 4).getFellow("r"+(i+1)+"_c2");
				Decimalbox box2=(Decimalbox)billGrid.getCell(i, 5).getFellow("r"+(i+1)+"_c3");
				Textbox box3=(Textbox)billGrid.getCell(i, 6).getFellow("r"+(i+1)+"_c4");
				contractFeeItem.setUnitPrice(checkData(box));
				contractFeeItem.setQuantity(checkData(box1));
				contractFeeItem.setPrice(checkData(box2));
				contractFeeItem.setMemo(box3.getValue());
			}
			theList.add(contractFeeItem);
		}
	return theList;
	}
	private double checkData(Decimalbox box) {

		if (box.getValue()==null) {
			return 0.0;
		}else {
			return box.getValue().doubleValue();
		}
	} 
}

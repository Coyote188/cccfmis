package cccf.ma.web.zk.surveyreport;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.modellite.spring.BeanAdapter;
import openjframework.model.OrganizationInfo;
import openjframework.service.OrganizationInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Listitem;

import cccf.ma.common.ObjectUtils;
import cccf.ma.model.DataDictionary;
import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.EnterpriseOwnActivatedProductListInfo;
import cccf.ma.model.ManufactureInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.ProductModel;
import cccf.ma.model.ProductModelAttachment;
import cccf.ma.model.ProductionEnterpriseInfo;
import cccf.ma.model.SurveyReport;
import cccf.ma.service.EnterpriseOwnActivatedProductListInfoServiceUtil;
import cccf.ma.service.EnterpriseProductModelServiceUtil;
import cccf.ma.service.ManufactureInfoServiceUtil;
import cccf.ma.service.ProductionEnterpriseInfoServiceUtil;
import cccf.ma.service.SurveyReportService;

import com.aidi.core.service.BaseDAOServcieUtil;
 

/**
 * 检验报告 —— 控制类
 */
public class SurveyReportFormController  extends GenericForwardComposer { 
	private static final long serialVersionUID = -3039777696982952204L;
	//企业查找
	private Listbox lb_searchEnterprise; 
	private Textbox tb_searchEnterprise;
	private List<EnterpriseInfo> enterpriseList; 
	private Bandbox bb_enterpriseList;  
	//制造商
	private Listbox lb_searchProductionEnterpriseInfo; 
	private Bandbox bb_ProductionEnterpriseInfo; 
	private List<ProductionEnterpriseInfo> productionEnterpriseInfoList; 
	//生产厂
	private Listbox lb_searchManufactureInfo; 
	private Bandbox bb_ManufactureInfo; 
	private List<ManufactureInfo> manufactureInfoList; 
	
	//检验机构
	private List<OrganizationInfo>organizationlist; 
	//检验类型
	private List<DataDictionary> surveyTypeList; 
	//检验报告编号
	private Textbox surveyReportSN; 
	//检验开始日期
	private Datebox surveyFromDate;
	//检验结束日期
	private Datebox surveyToDate;   
	//检验机构
	private Combobox organizationCbx;
	//检验类型 
	private Combobox surveyType;
	//检验依据
	private Textbox surverAccording; 
	//检验结论
	private Radiogroup surverVerdict;
	//检验结论——内容
	private Textbox surverVerdictContent ;
	//签发日期
	private Datebox issuingDate;
	//委托单位
	private Textbox agentOrg; 
	//抽样单位
	private Textbox samplingOrg; 
	
	private Combobox cb_product;
	
	private Listbox lb_productModelList;
	
	//检验样品列表
	private List<ProductModel> productModelList = new ArrayList<ProductModel>();

	//private AnnotateDataBinder binder;
	private Component comp;
	 
	private SurveyReportService surveyReportService;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp); 
		this.comp = comp; 
		surveyReportService = BeanAdapter.getBean("SurveyReportService", SurveyReportService.class);
		
		enterpriseList = EnterpriseProductModelServiceUtil.findByQuery("select o from EnterpriseInfo o ");
		organizationlist=OrganizationInfoServiceUtil.getAll();
		surveyTypeList = EnterpriseProductModelServiceUtil.findByQuery("select o from DataDictionary o where o.typeCode='SurveyType'"); 
		 
		Map surveyReport  =  Executions.getCurrent().getArg();  
		if(surveyReport.get("id")!=null){
			
		} 
		
	}
  
	//查询企业信息
	public void onSearchEnterprise(){
		String  name = tb_searchEnterprise.getText();
		enterpriseList = EnterpriseProductModelServiceUtil.findByQuery("select o from EnterpriseInfo o where o.name like '"+name+"%'"); 
	 
 	    lb_searchEnterprise.setModel(new ListModelList(enterpriseList));
	}  
	
	public void onSelectEnterprise(ForwardEvent event)
	{
		Listbox lb=(Listbox) event.getOrigin().getTarget();
		
		Set<Listitem> items = lb.getSelectedItems();
		Iterator<Listitem> itemi = items.iterator();
		Listitem item = itemi.next();
		EnterpriseInfo enterprise = (EnterpriseInfo) item.getValue();
		bb_enterpriseList.setValue(enterprise.getName());
		bb_enterpriseList.setAttribute("enterprise", enterprise);
		bb_enterpriseList.close();
		Events.sendEvent("onChange", bb_enterpriseList, null);
		
		
		cb_product.setSelectedItem(null);
		List<EnterpriseOwnActivatedProductListInfo> products = EnterpriseOwnActivatedProductListInfoServiceUtil.findByEnterprise(enterprise);
		ListModelList model = new ListModelList(products);
		cb_product.setModel(model);
		
		lb_searchProductionEnterpriseInfo.setSelectedItem(null); 
		String hql = "select o from ProductionEnterpriseInfo o where status=1 and o.enterprise.id='"	+ enterprise.getId() + "'";
		
		productionEnterpriseInfoList = ProductionEnterpriseInfoServiceUtil.findByQuery(hql);
		
		System.out.println("productionEnterpriseInfoList="+productionEnterpriseInfoList);
		
		ListModelList listModelList = new ListModelList(productionEnterpriseInfoList);
		lb_searchProductionEnterpriseInfo.setModel(listModelList);  
		
		
		lb_searchManufactureInfo.setSelectedItem(null); 
		hql = "from ManufactureInfo where status=1 and enterprise.id='" + enterprise.getId() + "'";
		 
		manufactureInfoList = ManufactureInfoServiceUtil.findByQuery(hql);
		ListModelList manumodel = new ListModelList(manufactureInfoList);
		lb_searchManufactureInfo.setModel(manumodel);
		
	}
	
	public void onSelectProductionEnterpriseInfo(ForwardEvent event)
	{
		Listbox lb=(Listbox) event.getOrigin().getTarget();
		
		Set<Listitem> items = lb.getSelectedItems();
		Iterator<Listitem> itemi = items.iterator();
		Listitem item = itemi.next();
		ProductionEnterpriseInfo productionEnterpriseInfo = (ProductionEnterpriseInfo) item.getValue();
		bb_ProductionEnterpriseInfo.setAttribute("productionEnterpriseInfo", productionEnterpriseInfo);
		 bb_ProductionEnterpriseInfo.setValue(productionEnterpriseInfo.getName());
		 bb_ProductionEnterpriseInfo.close();
		 Events.sendEvent("onChange", bb_ProductionEnterpriseInfo, null);
	 
	} 
	
	public void onSelectManufactureInfo(ForwardEvent event)
	{
		Listbox lb=(Listbox) event.getOrigin().getTarget();
		
		Set<Listitem> items = lb.getSelectedItems();
		Iterator<Listitem> itemi = items.iterator();
		Listitem item = itemi.next();
		ManufactureInfo obj = (ManufactureInfo) item.getValue();
		
		bb_ManufactureInfo.setAttribute("manufactureInfo", obj);
		bb_ManufactureInfo.setValue(obj.getName());
		bb_ManufactureInfo.close();
	    Events.sendEvent("onChange", bb_ManufactureInfo, null);
	 
	}  
	
	//打开产品型号添加窗口
	public void onOpen_productModel_add() throws SuspendNotAllowedException, InterruptedException{ 
		
		Window winProductSampleForm  = (Window) Executions.createComponents("/SysForm/SurveyReport/productModel_add.zul",
				null, null);
	    winProductSampleForm.addEventListener("onSaveProductSample", new EventListener() { 
					@Override
					public void onEvent(Event event) throws Exception {
						ProductModel bo = (ProductModel)event.getData(); 
						productModelList.add(bo); 
						//binder.loadAll();  
						((Window)event.getTarget()).detach();
						 
						lb_productModelList.setModel(new ListModelList(productModelList));
					} 
			 }) ; 
		winProductSampleForm.setClosable(true);
		winProductSampleForm.setMaximizable(true);
		winProductSampleForm.doModal();   
	}
	
	//保存检验报告
	public void onSave(){
		SurveyReport bo = new SurveyReport();
		bo.setProductModelList(productModelList);
		//企业信息 
		EnterpriseInfo enterprise = (EnterpriseInfo) bb_enterpriseList.getAttribute("enterprise");
	    bo.setEnterpriseInfo(enterprise);
		//产品分类 
		ProductCatalogueInfo pci = (ProductCatalogueInfo) cb_product.getSelectedItem().getAttribute("product");
	 	bo.setProductCatalogueInfo(pci); 
		//检验报告编号 
		bo.setSurveyReportSN(surveyReportSN.getText()); 
		//检验开始日期
		bo.setSurveyFromDate(surveyFromDate.getText());
		  
		//检验结束日期
		bo.setSurveyToDate(surveyToDate.getText()); 
		//检验机构名称
		bo.setSurverOrgName(organizationCbx.getSelectedItem().getLabel()); 
		//检验类型 
		bo.setSurverType(surveyType.getSelectedItem().getLabel()) ; 
		//检验依据
		bo.setSurverAccording(surverAccording.getText());
		//检验结论——合格、不合格 
		bo.setSurverVerdict( new Integer(surverVerdict.getSelectedItem().getValue() ));
		//检验结论——内容
		bo.setSurverVerdictContent(surverVerdictContent.getText()) ;
		//签发日期
		bo.setIssuingDate(issuingDate.getText());
		//制造商名称 
		ProductionEnterpriseInfo productionEnterpriseInfo = (ProductionEnterpriseInfo) bb_ProductionEnterpriseInfo.getAttribute("productionEnterpriseInfo");
	    bo.setProductionEnterpriseInfo(productionEnterpriseInfo);
		//生产厂名称
	     
		ManufactureInfo manufactureInfo = (ManufactureInfo) bb_ManufactureInfo.getAttribute("manufactureInfo");
	    bo.setManufactureInfo(manufactureInfo);
	    //委托单位
	    bo.setAgentOrg(agentOrg.getText());
	    //抽样单位
		bo.setSamplingOrg(samplingOrg.getText());
		
	    bo.setOrganizer(UserInfoServiceUtil.getCurrentLoginUser().getId());
	    bo.setOrganizeTime(ObjectUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
	    
		surveyReportService.save(bo);  
	    
	    Events.sendEvent(Events.ON_CLOSE, comp, null);
	} 

	public List<EnterpriseInfo> getEnterpriseList() {
		return enterpriseList;
	}

	public void setEnterpriseList(List<EnterpriseInfo> enterpriseList) {
		this.enterpriseList = enterpriseList;
	}

	public List<ProductionEnterpriseInfo> getProductionEnterpriseInfoList() {
		return productionEnterpriseInfoList;
	}

	public void setProductionEnterpriseInfoList(
			List<ProductionEnterpriseInfo> productionEnterpriseInfoList) {
		this.productionEnterpriseInfoList = productionEnterpriseInfoList;
	}

	public List<ManufactureInfo> getManufactureInfoList() {
		return manufactureInfoList;
	}

	public void setManufactureInfoList(List<ManufactureInfo> manufactureInfoList) {
		this.manufactureInfoList = manufactureInfoList;
	}

	public List<OrganizationInfo> getOrganizationlist() {
		return organizationlist;
	}

	public void setOrganizationlist(List<OrganizationInfo> organizationlist) {
		this.organizationlist = organizationlist;
	}

	public List<DataDictionary> getSurveyTypeList() {
		return surveyTypeList;
	}

	public void setSurveyTypeList(List<DataDictionary> surveyTypeList) {
		this.surveyTypeList = surveyTypeList;
	}

	public List<ProductModel> getProductModelList() {
		return productModelList;
	}

	public void setProductModelList(List<ProductModel> productModelList) {
		this.productModelList = productModelList;
	}
 
	 
}

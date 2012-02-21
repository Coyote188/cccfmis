package cccf.mis.web.survey;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.modellite.spring.BeanAdapter;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Listitem;

import cccf.ma.common.ObjectUtils;
import cccf.ma.model.Attachment;
import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.ManufactureInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.ProductModel;
import cccf.ma.model.ProductModelAttachment;
import cccf.ma.model.ProductionEnterpriseInfo;
import cccf.ma.model.SurveyReport;
import cccf.ma.service.SurveyReportService;

public class ReportController
		extends GenericForwardComposer
{ 
	private static final long	serialVersionUID	= 8260028457932691461L;
	
	private SurveyReportService svc =BeanAdapter.getBean("SurveyReportService", SurveyReportService.class);
	private Window					cofirmWin;
	
	//企业查找
	private Listbox lb_searchEnterprise; 
	private Bandbox bb_enterpriseList;   
	
	//制造商
	private Listbox lb_searchProductionEnterpriseInfo; 
	private Bandbox bb_ProductionEnterpriseInfo;  
	
	//产品
	private Listbox lb_searchProduct; 
	private Bandbox bb_Product;  
	
	//生产厂
	private Listbox lb_searchManufactureInfo; 
	private Bandbox bb_ManufactureInfo; 
	
	private Fileupload fl_SurveyReport;
	private Toolbarbutton tb_del_SurveyReport;
	private Toolbarbutton tb_read_SurveyReport;
	private Attachment attachment_SurveyReport;
	
	//检验报告编号
	private Textbox surveyReportSN; 
	//检验开始日期
	private Datebox surveyFromDate;
	//检验结束日期
	private Datebox surveyToDate;   
 
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
	//检验中心
	private Label  lb_organizationInfo;
	
	private Listbox lb_productModelList;
	private ArrayList<ProductModel> productModelList = new ArrayList<ProductModel>();
	
	  // 文件存放路径
	private String path_Attachment = "/SurveyReport/ProductModelAttachment";
	private String path_SurveyReport = "/SurveyReport";
	 
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);   
		
	}
	
	public void onInit(ForwardEvent event){
		List enterpriseList = svc.getResultList("select o from DataDictionary o where o.typeCode='SurveyType'"); 
		ListModelList listModelList = new ListModelList(enterpriseList);
		surveyType.setModel(listModelList) ; 
		
		listModelList = new ListModelList(productModelList);
		lb_productModelList.setModel(listModelList) ; 
		
		lb_organizationInfo.setValue(UserInfoServiceUtil.getCurrentLoginUser().getOrganization().getOrganizationName());
	}

	
	/**
	 * 查询企业信息
	 * @param event
	 */
	public void onQueryEnterprise(ForwardEvent event){
		Bandbox bb = (Bandbox) event.getOrigin().getTarget();
		String name = bb.getValue(); 
		StringBuffer hql = new StringBuffer("select new map (")
		                 .append(" name as name")
		                 .append(",state as state")
		                 .append(",location as location")
		                 .append(",contactPerson as contactPerson")
		                 .append(",id as id")
		                 .append(") from EnterpriseInfo ")
		                 .append(" where name like '").append(name).append("%'");
		List enterpriseList = svc.getResultList(hql.toString()); 
		lb_searchEnterprise.setModel(new ListModelList(enterpriseList)); 
	}
	
	public void onSelectEnterprise(ForwardEvent event)
	{
		Listbox lb=(Listbox) event.getOrigin().getTarget();
		
		Set<Listitem> items = lb.getSelectedItems();
		Iterator<Listitem> itemi = items.iterator();
		Listitem item = itemi.next();
		Map selectEnterprise = (Map) item.getValue();
		bb_enterpriseList.setValue((String)selectEnterprise.get("name"));
		bb_enterpriseList.setAttribute("enterprise", selectEnterprise);
		bb_enterpriseList.close();
		Events.sendEvent("onChange", bb_enterpriseList, null); 
	}
	
	public void onQueryProductionEnterpriseInfo(ForwardEvent event){
		Bandbox bb = (Bandbox) event.getOrigin().getTarget();
		ListModelList listModelList = new ListModelList();
		Map selectEnterprise = (Map) bb_enterpriseList.getAttribute("enterprise");
		if(selectEnterprise!=null){
			String name = bb.getValue(); 
			StringBuffer hql = new StringBuffer("select new map (")
			                 .append(" name as name")
			                 .append(",facDistrict as facCountry")
			                 .append(",facDistrict as facDistrict")
			                 .append(",registeredAddress as registeredAddress")
			                 .append(",facLinkMan as facLinkMan")
			                 .append(",id as id")
			                 .append(") from ProductionEnterpriseInfo ")
			                 .append(" where status=1")
			                 .append(" and enterprise.id = '").append(selectEnterprise.get("id")).append("'")
			                 .append(" and name like '").append(name).append("%'");
			List list = svc.getResultList(hql.toString()); 
			if(list!=null){
				listModelList.addAll(list);
			}
		}
		lb_searchProductionEnterpriseInfo.setModel(listModelList);
	}
	
	public void onSelectProductionEnterpriseInfo(ForwardEvent event)
	{
		Listbox lb=(Listbox) event.getOrigin().getTarget();
		
		Set<Listitem> items = lb.getSelectedItems();
		Iterator<Listitem> itemi = items.iterator();
		Listitem item = itemi.next();
		Map selectItem = (Map) item.getValue();
		bb_ProductionEnterpriseInfo.setAttribute("productionEnterpriseInfo", selectItem);
		bb_ProductionEnterpriseInfo.setValue((String)selectItem.get("name"));
	    bb_ProductionEnterpriseInfo.close();
		Events.sendEvent("onChange", bb_ProductionEnterpriseInfo, null); 
	} 
	
	public void onQueryProduct(ForwardEvent event){
		Bandbox bb = (Bandbox) event.getOrigin().getTarget();
		ListModelList listModelList = new ListModelList();
		Map selectEnterprise = (Map) bb_enterpriseList.getAttribute("enterprise");
		if(selectEnterprise!=null){
			String name = bb.getValue(); 
			StringBuffer hql = new StringBuffer("select new map (")
			                 .append(" product.id as id") 
			                 .append(",product.productName as productName") 
			                 .append(") from EnterpriseOwnActivatedProductListInfo ")
			                 .append(" where activateStatus=1") 
			                 .append(" and enterprise.id = '").append(selectEnterprise.get("id")).append("'")
			                 .append(" and product.productName like '").append(name).append("%'");
		 
			 List list = svc.getResultList(hql.toString()); 
			 if(list!=null){
				listModelList.addAll(list);
			} 
			
		}
		lb_searchProduct.setModel(listModelList);
	}
	
	public void onSelectProduct(ForwardEvent event)
	{
		Listbox lb=(Listbox) event.getOrigin().getTarget();
		
		Set<Listitem> items = lb.getSelectedItems();
		Iterator<Listitem> itemi = items.iterator();
		Listitem item = itemi.next();
		Map selectItem = (Map) item.getValue();
		bb_Product.setAttribute("product", selectItem);
		bb_Product.setValue((String)selectItem.get("productName"));
		bb_Product.close();
		Events.sendEvent("onChange", bb_Product, null); 
	} 
	
	public void onQueryManufactureInfo(ForwardEvent event){
		Bandbox bb = (Bandbox) event.getOrigin().getTarget();
		ListModelList listModelList = new ListModelList();
		Map selectEnterprise = (Map) bb_enterpriseList.getAttribute("enterprise");
		if(selectEnterprise!=null){
			String name = bb.getValue();  
			 
			StringBuffer hql = new StringBuffer("select new map (")
			                 .append(" id as id")      
			                 .append(",name as name")
			                 .append(",manuDistrict as manuDistrict")
			                 .append(",registeredAddress as registeredAddress") 
			                 .append(",manuLinkMan as manuLinkMan") 
			                 .append(") from ManufactureInfo ")
			                 .append(" where status=1") 
			                 .append(" and enterprise.id = '").append(selectEnterprise.get("id")).append("'")
			                 .append(" and name like '").append(name).append("%'");
			List list = svc.getResultList(hql.toString()); 
			if(list!=null){
				listModelList.addAll(list);
			}
			
		}
		lb_searchManufactureInfo.setModel(listModelList);
	}
	
	public void onSelectManufactureInfo(ForwardEvent event)
	{
		Listbox lb=(Listbox) event.getOrigin().getTarget();
		
		Set<Listitem> items = lb.getSelectedItems();
		Iterator<Listitem> itemi = items.iterator();
		Listitem item = itemi.next();
		Map selectItem = (Map) item.getValue();
		bb_ManufactureInfo.setAttribute("ManufactureInfo", selectItem);
		bb_ManufactureInfo.setValue((String)selectItem.get("name"));
		bb_ManufactureInfo.close();
		Events.sendEvent("onChange", bb_ManufactureInfo, null); 
	} 
	
	//保存检验报告
	public void onSave() throws InterruptedException{
		SurveyReport bo = new SurveyReport();
		//bo.setProductModelList(productModelList);
		//企业信息 
		Map map = (Map)bb_enterpriseList.getAttribute("enterprise");
		EnterpriseInfo enterprise = new EnterpriseInfo();
		enterprise.setId((String)map.get("id"));
	    bo.setEnterpriseInfo(enterprise);
		//产品分类 
	    map = (Map)bb_Product.getAttribute("product"); 
		ProductCatalogueInfo pci = new ProductCatalogueInfo();
		pci.setId((String)map.get("id"));
	 	bo.setProductCatalogueInfo(pci); 
		//检验报告编号 
		bo.setSurveyReportSN(surveyReportSN.getText()); 
		//检验开始日期
		bo.setSurveyFromDate(surveyFromDate.getText());
		  
		//检验结束日期
		bo.setSurveyToDate(surveyToDate.getText());  ; 
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
		map = (Map) bb_ProductionEnterpriseInfo.getAttribute("productionEnterpriseInfo");
		ProductionEnterpriseInfo productionEnterpriseInfo = new ProductionEnterpriseInfo();
		productionEnterpriseInfo.setId((String)map.get("id"));
	    bo.setProductionEnterpriseInfo(productionEnterpriseInfo);
		//生产厂名称 
	    map = (Map)bb_ManufactureInfo.getAttribute("ManufactureInfo");
		ManufactureInfo manufactureInfo = new ManufactureInfo();
		manufactureInfo.setId((String)map.get("id"));
	    bo.setManufactureInfo(manufactureInfo);
	    //委托单位
	    bo.setAgentOrg(agentOrg.getText());
	    //抽样单位
		bo.setSamplingOrg(samplingOrg.getText());
		
	    bo.setOrganizer(UserInfoServiceUtil.getCurrentLoginUser().getId());
	    bo.setOrganizeTime(ObjectUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
	    
	    bo.setAttachment(attachment_SurveyReport);
	    
	    bo.setProductModelList(productModelList);
	    
	    bo.setOrganizationInfo(UserInfoServiceUtil.getCurrentLoginUser().getOrganization());
	    bo.setSurverOrgName(bo.getOrganizationInfo().getOrganizationName());
	    
	    
		svc.save(bo);  
		
		Messagebox.show("信息提交成功!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
		// Events.postEvent(ConfirmityListController.ON_REFRESH,cofirmWin.getParent(),null);
		cofirmWin.setAttribute("submit", "OK");
		cofirmWin.setVisible(false);
	} 
	
	public void onAddProductModel(ForwardEvent event){
		ProductModel productModel = new ProductModel(); 
		  
		productModel.setProductModelAttachment(new ArrayList<ProductModelAttachment>());
	 
		productModelList.add(0, productModel);
		ListModelList listModelList = new ListModelList(productModelList);
		lb_productModelList.setModel(listModelList) ; 
	}
	 
	 
	// 上传附件
	public void onAttachmentUpload(ForwardEvent event)
	{
		UploadEvent uev = (UploadEvent) event.getOrigin();
		Media media = uev.getMedia(); 
		Listitem item = (Listitem)(uev.getTarget().getParent().getParent().getParent().getParent().getParent());
		Combobox combobox =(Combobox) uev.getTarget().getParent().getPreviousSibling().getFirstChild();
		ProductModel productModel =(ProductModel)item.getValue();
		List<ProductModelAttachment> attachments = productModel.getProductModelAttachment();
		
		ProductModelAttachment att = new ProductModelAttachment();
		att.setName( combobox.getValue());
		att.setAttachment(new Attachment());
		att.getAttachment().setPath(path_Attachment);
		att.getAttachment().setName(media.getName());
		att.getAttachment().setAttfile(media);  
		attachments.add(att);
		 
		ListModelList listModelList = (ListModelList)lb_productModelList.getModel(); 
		lb_productModelList.setModel(listModelList) ; 
	}
	// 删除附件
	public void onDeleteAttachfile(ForwardEvent event)
	{
		Toolbarbutton btn = (Toolbarbutton) event.getOrigin().getTarget();
		Listitem item = (Listitem)(btn.getParent().getParent().getParent().getParent().getParent());
		ProductModelAttachment attachment = (ProductModelAttachment) btn.getAttribute("attachment");
		ProductModel productModel =(ProductModel)item.getValue();
		
		productModel.getProductModelAttachment().remove(attachment); 
		
		ListModelList listModelList = (ListModelList)lb_productModelList.getModel(); 
		lb_productModelList.setModel(listModelList) ; 
	}
	
	public void onSurveyReportUpload(ForwardEvent event)
	{
		UploadEvent uev = (UploadEvent) event.getOrigin();
		Media media = uev.getMedia(); 
		attachment_SurveyReport = new Attachment();
		attachment_SurveyReport.setName(media.getName());
		attachment_SurveyReport.setPath(path_SurveyReport);
		attachment_SurveyReport.setAttfile(media); 
		fl_SurveyReport.setVisible(false);  
		tb_del_SurveyReport.setVisible(true);
		tb_read_SurveyReport.setVisible(true);
		tb_read_SurveyReport.setLabel(media.getName());
	}
	
	public void onDeleteSurveyReport(ForwardEvent event)
	{
		attachment_SurveyReport=null;
		fl_SurveyReport.setVisible(true);  
		tb_del_SurveyReport.setVisible(false);
		tb_read_SurveyReport.setVisible(false); 
	}
	
	public void onDownloadSurveyReport(ForwardEvent event){
		
	}
}

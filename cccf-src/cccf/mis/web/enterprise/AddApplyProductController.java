package cccf.mis.web.enterprise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cccf.ma.model.ManufactureInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.ProductionEnterpriseInfo;

import com.aidi.core.service.BaseDAOServcieUtil;

/**
 * @author 周金雄
 * @E-mail JOINYO@YEAH.NET
 * @date 创建于2011-5-17上午12:40:52
 * @version 1.0
 * 
 * @describe ALL RIGHTS RESERVED,COPYRIGHT(C) FCH LIMITED 2011
 */
public class AddApplyProductController extends GenericForwardComposer {
	private static final long serialVersionUID = 1L;
	Map params = Executions.getCurrent().getArg();
	private ProductionEnterpriseInfo peInfo;
	private ManufactureInfo mfInfo;
	private ProductCatalogueInfo productCatalogInfo, selectSecondryCatalogInfo,
			selectFlyersCatalogInfo;
	private List<ProductCatalogueInfo> secondaryCatalogue, flyersCatalogue;
	// 选择的二级目录控件/三级目录/产品名称控件/检验报告
	private Combobox secondaryCbx, prnameCbx, surveyReportCbx;
	// 检验报告列表/产品型号列表
	private List<Map> surveyReportList, productModelList;
	private Map map;

	// 设置生产厂和制造商
	private Label proLab, manLab;
	private Window addProWin, pAAWin;
	private Listbox pmLbx;
	// 选中的产品型号
	private Checkbox checkItem;
	// 选中的产品型号id
	private List<String> selectModelItems;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		selectModelItems = new ArrayList<String>();
		productCatalogInfo = (ProductCatalogueInfo) params
				.get("productCatalogInfo");
		secondaryCatalogue = new ArrayList<ProductCatalogueInfo>(
				productCatalogInfo.getProductChildren());
		super.doAfterCompose(comp);
	}

	public void onSave(ForwardEvent event) throws InterruptedException {
		
		if (pmLbx.getSelectedCount() == 0){ 
			Messagebox.show("请选择至少一个产品型号!", "操作提示", Messagebox.OK,
					Messagebox.INFORMATION);
		} else {
			List List = new ArrayList();
			for( Object o :pmLbx.getSelectedItems()){
				 Listitem item = (Listitem)o;
				 List.add(item.getValue());
			}
			Events.postEvent(ApplyController.ON_REFRESH, pAAWin, List);
			addProWin.detach();
		}
	}
 

	// 选择二级目录
	public void onSelectSecondaryCatalogue(SelectEvent event) {
		secondaryCbx = (Combobox) event.getTarget();
		selectSecondryCatalogInfo = (ProductCatalogueInfo) secondaryCbx
				.getSelectedItem().getValue();
		flyersCatalogue = selectSecondryCatalogInfo.getAllChildren();
		prnameCbx.setModel(new ListModelList(flyersCatalogue));
	}// 选择产品

	public void onSelectFlyersCatalogue(SelectEvent event) {
		prnameCbx = (Combobox) event.getTarget();
		selectFlyersCatalogInfo = (ProductCatalogueInfo) prnameCbx
				.getSelectedItem().getValue();
		StringBuffer hql = new StringBuffer("SELECT new map(id AS id")
		                    .append(",surveyReportSN AS surveyReportSN")
		                    .append(",productionEnterpriseInfo.name AS productionName")
		                    .append(",productionEnterpriseInfo.id AS productionId")
		                    .append(",manufactureInfo.name AS manufactureName")
		                    .append(",manufactureInfo.id AS manufactureId") 
		                    .append(")")
		                    .append(" FROM SurveyReport ")
		                    .append(" where status=1")
		                    .append(" and productCatalogueInfo.id='").append(selectFlyersCatalogInfo.getId()).append("'");
	 
		surveyReportList = BaseDAOServcieUtil.findByQueryString(hql.toString());
		System.out.println(surveyReportList);
		surveyReportCbx.setModel(new ListModelList(surveyReportList));
	}

	public void onSelectSurveyReport(SelectEvent event) {
		surveyReportCbx = (Combobox) event.getTarget();
		map = (Map) surveyReportCbx.getSelectedItem().getValue();
		// peInfo=ProductionEnterpriseInfoServiceUtil.getById(map.get("productionId").toString());
		proLab.setValue(map.get("productionName").toString());
		proLab.setAttribute("p_id", map.get("productionId").toString());
		manLab.setValue(map.get("manufactureName").toString());
		manLab.setAttribute("m_id", map.get("manufactureId").toString());
		// 要得到产品型号
		StringBuffer hql = new StringBuffer("SELECT new map( id AS id")
				.append(",surveyReport.surveyReportSN AS surveyReportSN")
				.append(",productionEnterpriseInfo.name AS produtionName")
				.append(",manufactureInfo.name AS manufactureName")
				.append(",productCatalogueInfo.productName AS productName")
				.append(",productionEnterpriseInfo.id AS productionId")
				.append(",specification AS specification")
				.append(",characterization AS characterization")
				.append(",isMainModel AS isMainModel").append(")")
				.append(" FROM ProductModel ")
				.append(" WHERE surveyReport.id='").append(map.get("id"))
				.append("'");
		productModelList = BaseDAOServcieUtil.findByQueryString(hql.toString());
		Map<String, Map> tmpMap = new HashMap<String, Map>();
		for (Map item : productModelList) {
			tmpMap.put((String) item.get("id"), item);
			item.put("productModelAttachment", new ArrayList());
		}

		hql = new StringBuffer("select new map(name as name ")
				.append(", productModel.id as productModel_id")
				.append(", attachment.id as attachment_id")
				.append(", attachment.path as attachment_path")
				.append(", attachment.name as attachment_name")
				.append(") from ProductModelAttachment ")
				.append(" where productModel.surveyReport.id='")
				.append(map.get("id")).append("'");
		 
		List<Map> attachments = BaseDAOServcieUtil.findByQueryString(hql
				.toString());
		for (Map item : attachments) {
			Map sr = tmpMap.get(item.get("productModel_id"));
			ArrayList productModelAttachment = (ArrayList) sr.get("productModelAttachment");
			productModelAttachment.add(item);
		}
		pmLbx.setModel(new ListModelList(productModelList));
	}

	public List<ProductCatalogueInfo> getSecondaryCatalogue() {
		return secondaryCatalogue;
	}

	public void setSecondaryCatalogue(
			List<ProductCatalogueInfo> secondaryCatalogue) {
		this.secondaryCatalogue = secondaryCatalogue;
	}

	public ProductCatalogueInfo getProductCatalogInfo() {
		return productCatalogInfo;
	}

	public void setProductCatalogInfo(ProductCatalogueInfo productCatalogInfo) {
		this.productCatalogInfo = productCatalogInfo;
	}
}

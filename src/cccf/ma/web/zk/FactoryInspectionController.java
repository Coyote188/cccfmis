package cccf.ma.web.zk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.FactoryInspection;
import cccf.ma.model.ManufactureInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.ProductionEnterpriseInfo;
import cccf.ma.model.ProductionModelInfo;
import cccf.ma.model.ProductionModelInspection;
import cccf.ma.service.FactoryInspectionServiceUtil;
import cccfmis.bpm.zk.ApplictionGCJCWindow;

public class FactoryInspectionController extends GenericForwardComposer{

	private static final long serialVersionUID = 2326647593416977170L;

	public static final String ON_FACTORYINSPECTION = "onInspection";
	
	private List<ProductionModelInfo> productionModel;
	private EnterpriseInfo enterprise;
	private ManufactureInfo manufacture;
	private ProductionEnterpriseInfo penterprise;
	private List<Media> medias;
	private List<Map<String,Object>> mediaMap,simpleImageMap;
	private FactoryInspection factoryInspection;
	
	@SuppressWarnings("unchecked")
	private Map params = Executions.getCurrent().getArg();
	private Window zlscApproveWindow;
	private ApplicationInfo application;
	private Grid grdFiles,grdProductionInspection;
	private Textbox tbxFileName,tbxFileType;
	private Tab tbProduct;
	private Fileupload fpdSampleImage;
	private Set<ProductionModelInspection> pmiList;
	private Radiogroup rdpFactoryConditions,rdpEquipmentConditions,rdpCertificateUsage,rdpFlagUsage,rdpFacConditionChange;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		factoryInspection = new FactoryInspection(); //这里有问题，factoryinspection 并未与任务相关联，且执行有误
		FactoryInspectionServiceUtil.create(factoryInspection);
		application = (ApplicationInfo) params.get("application");
		setEnterprise(application.getEnterprise());
		setManufacture(application.getManufacture());
		setPenterprise(application.getProductionEnterprise());
		productionModel = application.getModels();//ProductionModelInfoServiceUtil.getAll();
		mediaMap = new ArrayList<Map<String,Object>>();
		medias = new ArrayList<Media>();
		simpleImageMap = new ArrayList<Map<String,Object>>();
		ProductCatalogueInfo product = application.getProduction();
		tbProduct.setLabel("产品："+product.getProductName());
		pmiList = new HashSet<ProductionModelInspection>();
	}
	
	public void onInspection(Event ent){
		validata();
		getGridData();
		params.put("appfromInspection", application);
		params.put("mediaMap", mediaMap);
		params.put("factoryInspection", factoryInspection);
		
		FactoryInspectionServiceUtil.create(factoryInspection);
		
		Events.sendEvent(ApplictionGCJCWindow.ON_GETINSPECTIONDATA, zlscApproveWindow, params);
	}
	
	private void getGridData(){
//		factoryInspection.setInspectionResult(rdpInspectionResult.getSelectedItem().getLabel());
		factoryInspection.setFactoryConditions(Integer.valueOf(rdpFactoryConditions.getSelectedItem().getValue()));
		factoryInspection.setEquipmentConditions(Integer.valueOf(rdpEquipmentConditions.getSelectedItem().getValue()));
		factoryInspection.setCertificateUsage(Integer.valueOf(rdpCertificateUsage.getSelectedItem().getValue()));
		factoryInspection.setFlagUsage(Integer.valueOf(rdpFlagUsage.getSelectedItem().getValue()));
		factoryInspection.setFacConditionChange(Boolean.valueOf(rdpFacConditionChange.getSelectedItem().getValue()));
		List<Row> rows = grdProductionInspection.getRows().getChildren();
		for(Row row : rows){
			ProductionModelInspection productionInspection = new ProductionModelInspection();
			List<Component> comps = row.getChildren();
			for(Component comp : comps){
				System.out.println(comp);
			}
			Label lblModel = (Label) comps.get(1);
			for(ProductionModelInfo model : productionModel){
				if(model.getModel().equals(lblModel.getValue().toString()))
				{
					productionInspection.setModel(model);
				}
			}
			List<Component> compInH = ((Vbox)comps.get(3)).getChildren();
			for(Component comp : compInH)
			{
				if(comp instanceof Checkbox)
				{
					if(((Checkbox)comp).getLabel().equals("外观结构是否一致"))
					{
						productionInspection.setWhetherAppearanceStructureConswhethertent(((Checkbox)comp).isChecked());
					}
					else if(((Checkbox)comp).getLabel().equals("关键件是否一致"))
					{
						productionInspection.setWhetherKeyComponentsConswhethertent(((Checkbox)comp).isChecked());
					}
					else if(((Checkbox)comp).getLabel().equals("生产工艺是否一致"))
					{
						productionInspection.setWhetherProductionProcessConswhethertent(((Checkbox)comp).isChecked());
					}
				}
			}
			
			productionInspection.setInspectionType(Integer.valueOf(((Combobox)comps.get(2)).getSelectedItem().getValue().toString()));
			//一致性检查结论未处理
			productionInspection.setInspectionResult(Integer.valueOf(((Combobox)comps.get(4)).getSelectedItem().getValue().toString()));
			String fileNames = "";
			for(Map<String,Object> simpleMap : simpleImageMap){
				Media media = (Media)simpleMap.get(lblModel.getValue().toString());
				if(null != media)
					fileNames += (media.getName())+"|";
			}
			productionInspection.setSimpleImgs(fileNames);
//			productionInspection.setModel(model);
			pmiList.add(productionInspection);
			factoryInspection.setProductionModelIspection(pmiList);
			factoryInspection.setApplication(application);
		}
	}
	
	public void onClick$btnFillFactoryChecklist() throws SuspendNotAllowedException, InterruptedException{
		Map params = new HashMap();
		params.put("inspection", factoryInspection);
		Window objWin = (Window) Executions.createComponents("/SysForm/FactoryCheck/factory-checklist-edit.zul", null, params);
		objWin.doModal();
	}
	
	public void onOpenAttachment(ForwardEvent ent)
	{
		System.out.println(ent.getData() + "--" + ent.getName());
		System.out.println(((Toolbarbutton)ent.getOrigin().getTarget()).getAttribute(ent.getData().toString()));
	}
	
	void validata(){
		List<Row> rows = grdProductionInspection.getRows().getChildren();
		for(Row row : rows)
		{
			List<Component> comps = row.getChildren();
			for(Component comp : comps)
			{
				if(comp instanceof Textbox)
					if(null == ((Textbox)comp).getValue())
						throw new WrongValueException(comp,"请选择!");
				if(comp instanceof Combobox)
					if(null == ((Combobox)comp).getSelectedItem())
						throw new WrongValueException(comp,"请选择!");
				if(comp instanceof Bandbox)
					if(null == ((Bandbox)comp).getText())
						throw new WrongValueException(comp,"请选择!");
			}
		}
		
//		if(null == rdpInspectionResult.getSelectedItem()){
//			throw new WrongValueException(rdpInspectionResult,"请选择!");
//		}
		
		if(null == rdpFactoryConditions .getSelectedItem()){
			throw new WrongValueException(rdpFactoryConditions,"请选择!");	
		}else if(null == rdpEquipmentConditions .getSelectedItem()){
			throw new WrongValueException(rdpEquipmentConditions,"请选择!");
		}else if(null == rdpCertificateUsage .getSelectedItem()){
			throw new WrongValueException(rdpCertificateUsage,"请选择!");
		}else if(null == rdpFlagUsage .getSelectedItem()){
			throw new WrongValueException(rdpFlagUsage,"请选择!");
		}else if(null == rdpFacConditionChange.getSelectedItem()){
			throw new WrongValueException(rdpFacConditionChange,"请选择!");
		}
	}
	
	public void onEnableSimpleUploader(ForwardEvent ent){
		SelectEvent event = (SelectEvent) ent.getOrigin();
		Fileupload fpd = (Fileupload)((Hbox)((Row)ent.getOrigin().getTarget().getParent()).getChildren().get(6)).getLastChild();
		if(0==Integer.valueOf((((Combobox)event.getTarget()).getSelectedItem().getValue().toString()))){
			fpd.setVisible(true);
		}else{
			fpd.setVisible(false);
		}
	}
	
	public void onSimpleImgUpload(ForwardEvent ent){
		
		UploadEvent event = (UploadEvent) ent.getOrigin();
		List<Media> medias = new ArrayList<Media>();
		Map map = new HashMap();
//		System.out.println(((Row)ent.getOrigin().getTarget().getParent().getParent()).getValue());//productmodel
		map.put(((Row)ent.getOrigin().getTarget().getParent().getParent()).getValue(), event.getMedia());
		simpleImageMap.add(map);
		
		
		Bandbox bd = (Bandbox) ent.getOrigin().getTarget().getParent().getChildren().get(0);
		Listbox lbxSimpleImg = (Listbox) ((Bandpopup) bd.getChildren().get(0)).getChildren().get(0);
		
		
//		for(Map<String,Object> simpleMap : simpleImageMap){
//			Set<Map.Entry<String,Object>> set = simpleMap.entrySet();
//			for(Iterator<Map.Entry<String, Object>> entry = set.iterator();entry.hasNext();){
//				if(entry.next().getKey().equals(((Row)ent.getOrigin().getTarget().getParent().getParent()).getValue()))
//					medias.add((Media) entry.next().getValue());
//			}
//		}
		for(Map<String,Object> simpleMap : simpleImageMap){
			Media media = (Media) simpleMap.get(((Row)ent.getOrigin().getTarget().getParent().getParent()).getValue());
			medias.add(media);
		}
		
		ListModelList model = new ListModelList(medias);
		lbxSimpleImg.setModel(model);
		
		System.out.println(event.getMedia().getName());
	}
	
	public void onUpload$fpdBrowser(UploadEvent ent){
		Media file = ent.getMedia();
		Map<String,Object> media = new HashMap<String,Object>();
		if( tbxFileName.getValue() == "" || tbxFileType.getValue() == "")
			throw new WrongValueException(tbxFileType,"文件名或类型为空，请注意填写!");
		medias.add(file);
		media.put("id", null);
		media.put("filename", tbxFileName.getText());
		media.put("Aid", application.getId());
		media.put("file", file);
		media.put("filetype", tbxFileType.getText());
		mediaMap.add(media);
		
		ListModelList model = new ListModelList(medias);
		grdFiles.setModel(model);
		{
			tbxFileName.setText("");
			tbxFileType.setText("");
		}
	}

	public void onRemoveFile(ForwardEvent ent){
		Toolbarbutton btnFile = (Toolbarbutton) ent.getOrigin().getTarget();
		Media media = (Media)btnFile.getAttribute("media");
		medias.remove(media);
		ListModelList model = new ListModelList(medias);
		grdFiles.setModel(model);
		
		for(Map<String,Object> map : mediaMap){
			if(media.getName().equals(map.get("filename")))
				mediaMap.remove(map);
		}
	}
	
	public void setProductionModel(List<ProductionModelInfo> productionModel) {
		this.productionModel = productionModel;
	}

	public List<ProductionModelInfo> getProductionModel() {
		return productionModel;
	}

	public EnterpriseInfo getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(EnterpriseInfo enterprise) {
		this.enterprise = enterprise;
	}

	public ManufactureInfo getManufacture() {
		return manufacture;
	}

	public void setManufacture(ManufactureInfo manufacture) {
		this.manufacture = manufacture;
	}

	public ProductionEnterpriseInfo getPenterprise() {
		return penterprise;
	}

	public void setPenterprise(ProductionEnterpriseInfo penterprise) {
		this.penterprise = penterprise;
	}

	public List<Media> getMedias() {
		return medias;
	}

	public void setMedias(List<Media> medias) {
		this.medias = medias;
	}

	public FactoryInspection getFactoryInspection() {
		return factoryInspection;
	}

	public void setFactoryInspection(FactoryInspection factoryInspection) {
		this.factoryInspection = factoryInspection;
	}
}

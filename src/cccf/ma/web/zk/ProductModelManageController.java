package cccf.ma.web.zk;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.sql.DataSource;

import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;
import openjframework.util.ZkFileUtil;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Toolbarbutton;

import com.itextpdf.text.ListItem;

import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.EnterpriseOwnActivatedProductListInfo;
import cccf.ma.model.EnterpriseProductModel;
import cccf.ma.model.ManufactureInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.ProductionEnterpriseInfo;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.EnterpriseOwnActivatedProductListInfoServiceUtil;
import cccf.ma.service.EnterpriseProductModelServiceUtil;
import cccf.ma.service.ManufactureInfoServiceUtil;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;
import cccf.ma.service.ProductionEnterpriseInfoServiceUtil;
import cccf.ma.web.zk.override.GB2Alpha;

public class ProductModelManageController extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private List<ProductCatalogueInfo> products, srcProducts;
	private List<EnterpriseInfo> enterpriseList, srcEnterprise;
	private EnterpriseProductModel epModel;
	private String productSearch = "", enterpriseSearch = "", modelListSearch = "";
	private Bandbox BenterPriseList;
	private Combobox Eproduct, filetype, productionenterprise, manufacture, ereport;
	private EnterpriseInfo enterInfo = null;
	private EnterpriseProductModel editmodel;
	private String station = "isNew";
	private String productimage, designfile, otherfile, checkfile;

	public EnterpriseProductModel getEditmodel() {
		return editmodel;
	}

	public void setEditmodel(EnterpriseProductModel editmodel) {
		this.editmodel = editmodel;
	}

	public String getModelListSearch() {
		return modelListSearch;
	}

	public void setModelListSearch(String modelListSearch) {
		this.modelListSearch = modelListSearch;
	}

	private Textbox newNo, tempname, Nmodel, Nname, emodel, ename, echeckno, eorgname, echeckby, echecktype, searchValue, sproductimage, sdesignfile, sotherfile, secheckfile;
	private Datebox esetdate;
	private Listbox lbxProducts, lbxEnterprise, enterModelList, filelist, enterPriseList, noList;
	private Button upload, edit, delete, searchEnterprise, addNo;
	private Groupbox detail;
	private Component comp;
	DataSource source;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		this.comp = comp;
		setEnterpriseList(EnterpriseInfoServiceUtil.getAll());
		setSrcProducts(ProductCatalogueInfoServiceUtil.getLeafData());
		setProducts(getSrcProducts());

		epModel = new EnterpriseProductModel();
		editmodel = new EnterpriseProductModel();
		comp.getPage().setAttribute("editmodel", editmodel);
	}

	public void onClick$btnSubmit() throws InterruptedException {
		if (null == BenterPriseList.getValue() || BenterPriseList.getValue() == "")
			throw new WrongValueException(BenterPriseList, "请选择企业");
		else if (null == Eproduct.getSelectedItem())
			throw new WrongValueException(Eproduct, "请选择产品");
		else if (null == Nmodel.getText() || Nmodel.getText() == "")
			throw new WrongValueException(Nmodel, "请输入型号名称");
		epModel.setEnterprise(enterInfo);
		ProductCatalogueInfo product = (ProductCatalogueInfo) Eproduct.getSelectedItem().getValue();
		epModel.setProduct(product);
		epModel.setModel(Nmodel.getText());
		epModel.setName(Nname.getText());
		if (!EnterpriseProductModelServiceUtil.isModelExist(epModel.getModel(), enterInfo)) {
			if (Messagebox.YES == Messagebox.show("确认添加型号?", "添加型号", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION)) {
				EnterpriseProductModelServiceUtil.create(epModel);
				Nmodel.setValue(null);
				Nname.setValue(null);
				if (enterInfo != null) {
					List<EnterpriseProductModel> products = EnterpriseProductModelServiceUtil.findByEnterprise(enterInfo);
					ListModelList model = new ListModelList(products);
					enterModelList.setModel(model);
				}
			}
		} else {
			throw new WrongValueException(Nmodel, "该型号已经存在，请重新输入!");
		}
	}

	private List<ProductCatalogueInfo> getProductAlpha(String arg) {

		products = new ArrayList<ProductCatalogueInfo>();
		for (ProductCatalogueInfo product : getSrcProducts()) {
			if (GB2Alpha.getFirstLetter(product.getProductName()).contains(GB2Alpha.getFirstLetter(arg))) {
				products.add(product);
			}
		}
		return products;
	}

	private List<EnterpriseInfo> getEnterpriseAlpha(String arg) {
		enterpriseList = new ArrayList<EnterpriseInfo>();
		for (EnterpriseInfo enter : getSrcEnterprise()) {
			if (GB2Alpha.getFirstLetter(enter.getName()).contains(GB2Alpha.getFirstLetter(arg))) {
				enterpriseList.add(enter);
			}
		}
		return enterpriseList;
	}

	public void onChanging$tbxProductSearch(InputEvent event) {
		List<ProductCatalogueInfo> products = getProductAlpha(event.getValue());
		ListModelList model = new ListModelList(products);
		lbxProducts.setModel(model);
	}

	public void onSelect$lbxEnterprise(SelectEvent event) throws InterruptedException {
		@SuppressWarnings("unchecked")
		Set<Listitem> items = event.getSelectedItems();
		Iterator<Listitem> itemi = items.iterator();
		EnterpriseInfo enter = (EnterpriseInfo) itemi.next().getValue();
		List<EnterpriseProductModel> products = EnterpriseProductModelServiceUtil.findByEnterprise(enter);
		ListModelList model = new ListModelList(products);
		enterModelList.setModel(model);
	}

	// 选择产品
	public void onSelect$Eproduct(SelectEvent event) {
		echeckno.setValue(null);
		noList.setSelectedItem(null);
		Combobox combo = (Combobox) event.getTarget();
		setEditmodel(EnterpriseProductModelServiceUtil.getByPrimaryKey(combo.getSelectedItem().getValue().toString()));
		@SuppressWarnings("unchecked")
		List<EnterpriseProductModel> list = EnterpriseProductModelServiceUtil.findByQuery("from EnterpriseProductModel e where e.enterprise='" + enterInfo.getId()
				+ "' and e.product='" + combo.getSelectedItem().getValue() + "' group by checkno,e.enterprise,e.product");
		ListModelList model = new ListModelList(list);
		noList.setModel(model);
	}

	// 选择检验编号
	public void onSelect$noList(SelectEvent event) {
		EnterpriseProductModel model = (EnterpriseProductModel) noList.getSelectedItem().getValue();
		reloadModel(model);
		@SuppressWarnings("unchecked")
		List<EnterpriseProductModel> products = EnterpriseProductModelServiceUtil.findByQuery(" from EnterpriseProductModel e where e.enterprise='" + model.getEnterprise().getId()
				+ "' and e.product='" + model.getProduct().getId() + "' and e.checkno='" + model.getCheckno() + "'");
		ListModelList listmodel = new ListModelList(products);
		enterModelList.setModel(listmodel);
		productionenterprise.setValue(model.getProductname());
		manufacture.setValue(model.getMenuname());
		this.station = "isEdit";
	}

	public void reloadModel(EnterpriseProductModel model) {
		eorgname.setValue(model.getOrgname());
		echeckby.setValue(model.getCheckby());
		ereport.setValue(model.getReport());
		esetdate.setValue(model.getSetdate());
		echecktype.setValue(model.getChecktype());
		eorgname.setValue(model.getOrgname());
		secheckfile.setValue(model.getCheckfile());
	}

	// 添加检验编号
	public void onClick$addNo(Event envet) throws InterruptedException {
		if (enterInfo == null) {
			throw new WrongValueException(BenterPriseList, "请选择企业");
		} else if (Eproduct.getSelectedItem() == null) {
			throw new WrongValueException(Eproduct, "请选择产品");
		} else if (newNo.getValue() == null || newNo.getValue().trim().equals("")) {
			throw new WrongValueException(newNo, "请输入检验编号");
		} else {
			this.station = "isNew";
			reloadModel(new EnterpriseProductModel());
			enterModelList.setModel(new ListModelList());
			echeckno.setValue(newNo.getValue());
		}
	}

	public void onChanging$tbxEnterpriseSearch(InputEvent event) {
		List<EnterpriseInfo> enterprise = getEnterpriseAlpha(event.getValue());
		ListModelList model = new ListModelList(enterprise);
		lbxEnterprise.setModel(model);
	}

	public EnterpriseProductModel getEpModel() {
		return epModel;
	}

	public void setEpModel(EnterpriseProductModel epModel) {
		this.epModel = epModel;
	}

	public void setEnterpriseList(List<EnterpriseInfo> enterpriseList) {
		this.enterpriseList = enterpriseList;
	}

	public List<EnterpriseInfo> getEnterpriseList() {
		return enterpriseList;
	}

	public void setProducts(List<ProductCatalogueInfo> products) {
		this.products = products;
	}

	public List<ProductCatalogueInfo> getProducts() {
		return products;
	}

	public String getProductSearch() {
		return productSearch;
	}

	public void setProductSearch(String productSearch) {
		this.productSearch = productSearch;
	}

	public String getEnterpriseSearch() {
		return enterpriseSearch;
	}

	public void setEnterpriseSearch(String enterpriseSearch) {
		this.enterpriseSearch = enterpriseSearch;
	}

	public void setSrcProducts(List<ProductCatalogueInfo> srcProducts) {
		this.srcProducts = srcProducts;
	}

	public List<ProductCatalogueInfo> getSrcProducts() {
		return srcProducts;
	}

	public void setSrcEnterprise(List<EnterpriseInfo> srcEnterprise) {
		this.srcEnterprise = srcEnterprise;
	}

	public List<EnterpriseInfo> getSrcEnterprise() {
		return srcEnterprise;
	}

	public void onSelect$enterPriseList(SelectEvent event) throws InterruptedException {
		@SuppressWarnings("unchecked")
		Set<Listitem> items = event.getSelectedItems();
		Iterator<Listitem> itemi = items.iterator();
		Listitem item = itemi.next();
		enterInfo = (EnterpriseInfo) item.getValue();
		BenterPriseList.setValue(((EnterpriseInfo) item.getValue()).getName());
		BenterPriseList.close();
		Events.sendEvent("onChange", BenterPriseList, null);
	}

	public void onChange$BenterPriseList(ForwardEvent event) {
		Eproduct.setSelectedItem(null);
		List<EnterpriseOwnActivatedProductListInfo> products = EnterpriseOwnActivatedProductListInfoServiceUtil.findByEnterprise(enterInfo);
		ListModelList model = new ListModelList(products);
		Eproduct.setModel(model);

		productionenterprise.setSelectedItem(null);
		@SuppressWarnings("unchecked")
		List<ProductionEnterpriseInfo> productionfac = ProductionEnterpriseInfoServiceUtil.findByQuery("from ProductionEnterpriseInfo where status=1 and enterprise='"
				+ enterInfo.getId() + "'");
		ListModelList productionmodel = new ListModelList(productionfac);
		productionenterprise.setModel(productionmodel);

		manufacture.setSelectedItem(null);
		@SuppressWarnings("unchecked")
		List<ManufactureInfo> manufac = ManufactureInfoServiceUtil.findByQuery("from ManufactureInfo where status=1 and enterprise='" + enterInfo.getId() + "'");
		ListModelList manumodel = new ListModelList(manufac);
		manufacture.setModel(manumodel);
	}

	// public void onSelect$enterModelList(SelectEvent event) throws Exception {
	// Listbox listbox = (Listbox) event.getTarget();
	// editmodel = (EnterpriseProductModel)
	// listbox.getSelectedItem().getValue();
	// detail.getCaption().setLabel(editmodel.getProduct().getProductName() +
	// " " + editmodel.getModel() + " 资料信息");
	// detail.setVisible(true);
	// emodel.setValue(editmodel.getModel());
	// ename.setValue(editmodel.getName());
	// echeckno.setValue(editmodel.getCheckno());
	// eorgname.setValue(editmodel.getOrgname());
	// echeckby.setValue(editmodel.getCheckby());
	// ereport.setValue(editmodel.getReport());
	// if (editmodel.getSetdate() != null) {
	// esetdate.setValue(editmodel.getSetdate());
	// } else
	// esetdate.setValue(null);
	// echecktype.setValue(editmodel.getChecktype());
	// // AnnotateDataBinder binder = new AnnotateDataBinder(detail);
	// // binder.removeBinding(detail, "editmodel");
	// // binder.bindBean("editmodel", editmodel);
	// // binder.loadComponent(detail);
	// loadFileList();
	// }

	public void onSave() throws InterruptedException {
		EnterpriseProductModel model = new EnterpriseProductModel();
		model.setEnterprise(enterInfo);
		model.setProduct(ProductCatalogueInfoServiceUtil.getById(Eproduct.getSelectedItem().getValue().toString()));
		model.setModel(emodel.getValue());
		model.setName(ename.getValue());
		model.setProductname(productionenterprise.getValue());
		model.setMenuname(manufacture.getValue());
		model.setCheckno(echeckno.getValue());
		model.setOrgname(eorgname.getValue());
		model.setCheckby(echeckby.getValue());
		model.setReport(ereport.getValue());
		if (esetdate.getValue() != null) {
			model.setSetdate(esetdate.getValue());
		}
		model.setChecktype(echecktype.getValue());
		model.setCheckfile(null);
		model.setTempname(tempname.getValue());
		model.setProductimage(productimage);
		model.setDesignfile(designfile);
		model.setOtherfile(otherfile);
		model.setCheckfile(checkfile);
		EnterpriseProductModelServiceUtil.saveOrUpdate(model);
		Messagebox.show("保存成功!");
		sproductimage.setValue("");
		sdesignfile.setValue("");
		sotherfile.setValue("");
		@SuppressWarnings("unchecked")
		List<EnterpriseProductModel> products = EnterpriseProductModelServiceUtil.findByQuery(" from EnterpriseProductModel e where e.enterprise='" + enterInfo.getId()
				+ "' and e.product='" + Eproduct.getSelectedItem().getValue() + "' and e.checkno='" + echeckno.getValue() + "'");
		ListModelList listmodel = new ListModelList(products);
		enterModelList.setModel(listmodel);
	}

	public void onFileUP(ForwardEvent event) throws SQLException {
		UploadEvent ent = (UploadEvent) event.getOrigin();
		File product = new File("/Product");
		if (!product.exists()) {
			product.mkdir();
		}
		System.out.println(product.getPath());
		File Factory = new File("/Product/" + BenterPriseList.getValue());
		if (!Factory.exists()) {
			Factory.mkdir();
		}
		System.out.println(Factory.getPath());
		File Product = new File("/Product/" + BenterPriseList.getValue() + "/" + Eproduct.getValue());
		System.out.println(Product.getPath());
		Media media = ent.getMedia();
		if (media == null)
			return;
		String fileName;
		try {
			if (ZkFileUtil.uploadFile(media, Product.getPath())) {
				File file = new File(comp.getPage().getDesktop().getWebApp().getRealPath(Product.getPath()) + "\\" + media.getName());
				fileName = media.getName();
				fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."), fileName.length());
				// 文件的真实url
				String filrUrl = Product.getPath().replace("\\", "/") + "/" + fileName;
				file.renameTo(new File(comp.getPage().getDesktop().getWebApp().getRealPath(Product.getPath()) + "\\" + fileName));
				if (event.getData().toString().equals("productimage")) {
					File sfile = new File(comp.getDesktop().getWebApp().getRealPath(sproductimage.getValue()));
					if (sfile.exists())
						sfile.delete();
					productimage = filrUrl;
					sproductimage.setValue(filrUrl);
				} else if (event.getData().toString().equals("designfile")) {
					File sfile = new File(comp.getDesktop().getWebApp().getRealPath(sdesignfile.getValue()));
					if (sfile.exists())
						sfile.delete();
					designfile = filrUrl;
					sdesignfile.setValue(filrUrl);
				} else if (event.getData().toString().equals("other")) {
					File sfile = new File(comp.getDesktop().getWebApp().getRealPath(sotherfile.getValue()));
					if (sfile.exists())
						sfile.delete();
					otherfile = filrUrl;
					sotherfile.setValue(filrUrl);
				} else if (event.getData().toString().equals("checkfile")) {
					File sfile = new File(comp.getDesktop().getWebApp().getRealPath(secheckfile.getValue()));
					if (sfile.exists())
						sfile.delete();
					checkfile = filrUrl;
					secheckfile.setValue(filrUrl);
				}
			}

		} catch (Exception e) {
			System.out.println("无法上传" + e.getMessage());
		}
	}

	public void onOpenFile(ForwardEvent event) throws InterruptedException {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) filelist.getSelectedItem().getValue();
		Window seefile = (Window) Executions.createComponents("/cccfpage/seeFile.zul", null, null);
		Iframe iframe = (Iframe) seefile.getFellow("ifrme");
		iframe.setSrc(map.get("filename").toString());
		seefile.doModal();
	}

	public void onDelete(ForwardEvent event) throws InterruptedException, SQLException {
		if (Messagebox.show("确定要删除些文件吗?", "删除文件", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES) {
			Listitem listitem = enterModelList.getSelectedItem();
			EnterpriseProductModel model = (EnterpriseProductModel) enterModelList.getSelectedItem().getValue();

			EnterpriseProductModelServiceUtil.delete(model);
			enterModelList.getItems().remove(listitem);
			if (model.getProductimage() != null) {
				File file = new File(comp.getDesktop().getWebApp().getRealPath(model.getProductimage()));
				if (file.exists())
					file.delete();
			}
			if (model.getDesignfile() != null) {
				File file = new File(comp.getDesktop().getWebApp().getRealPath(model.getDesignfile()));
				if (file.exists())
					file.delete();
			}
			if (model.getOtherfile() != null) {
				File file = new File(comp.getDesktop().getWebApp().getRealPath(model.getOtherfile()));
				if (file.exists())
					file.delete();
			}
		}
	}

	private void loadFileList() throws SQLException {
		JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_productModelFile", "where modelid='" + getEditmodel().getId() + "'");
		List map = adapter.load();
		ListModelList model1 = new ListModelList(map);
		filelist.setModel(model1);
	}

	public void onClick$searchEnterprise(Event event) {
		if (searchValue.getText() != null && searchValue.getText().length() >= 2) {
			List<EnterpriseInfo> list = EnterpriseInfoServiceUtil.findByQuery("from EnterpriseInfo e where name like '%" + searchValue.getText().trim() + "%'");
			ListModelList model = new ListModelList(list);
			enterPriseList.setModel(model);
		}
	}
}

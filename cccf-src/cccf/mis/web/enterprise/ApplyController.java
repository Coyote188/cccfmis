package cccf.mis.web.enterprise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.modellite.spring.BeanAdapter;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cccf.ma.model.ApplicationPublicInfo;
import cccf.ma.model.ApplicationPublicInfoAttachment;
import cccf.ma.model.Attachment;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.ProductModel;
import cccf.ma.service.ApplicationPublicService;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;
import cccf.mis.web.index.enterprise.ApplyQueryUtils;

public class ApplyController
		extends GenericForwardComposer
{
	private static final long	serialVersionUID	= -3829436517850901440L;
	// 事件
	public static final String	 ON_REFRESH	 = "onRefreshModel";
	
	private List<ProductCatalogueInfo>	 productionList;//产品大类
	private ProductCatalogueInfo selectedProductCatalog;
	private Map	 productModelMap = new HashMap();
	private Listbox	 productmodelLbx; 
	
	private Combobox  production ;
	 
	private Grid				selectgrid, filesgrid;
	private UserInfo			userInfo;
	 
	private List<String>		staticfils			= new ArrayList<String>();
	{
		staticfils.add("营业执照副本复印件");
		staticfils.add("组织机构代码证复印件");
		staticfils.add("产品检验设备清单");
		staticfils.add("生产配置平面图");
		staticfils.add("《质量手册》和《程序文件》");
		staticfils.add("原产品认证证书复印件");
	}
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		attachfilePath = "/attachments/" + "application" + "/" + UUID.randomUUID().toString();
		userInfo = UserInfoServiceUtil.getCurrentLoginUser();
		 
		super.doAfterCompose(comp);
	}
	// 文件
	public void onInited(ForwardEvent event)
	{
		List<Map<String, Object>> files = new ArrayList<Map<String, Object>>();
		 
		for (String name : staticfils)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("filename", "");
			map.put("isFile", false);
			files.add(map);
		}
		filesgrid.setModel(new ListModelList(files));
		
		productionList = ProductCatalogueInfoServiceUtil.getDataListByLevel(1);
		production.setModel(new ListModelList(productionList));
		
	}
	 
 
	// 公共信息服务
	private ApplicationPublicService	applicationPublicService	= BeanAdapter.getBean("ApplicationPublicService", ApplicationPublicService.class);
	// 产品表
	private List<Map>					productModelList			= new ArrayList<Map>();
	// 公共信息
	private ApplicationPublicInfo		applicationPublic			= new ApplicationPublicInfo();
	private Combobox					applyType, businessType;
	private Radiogroup					contractAgree, contractApprove;
	private Intbox						contractYear, contractMonth;
	private String						attachfilePath;
	//
	public void onUploadOther(ForwardEvent event)
	{
		// TODO 多文件上传
		UploadEvent evt = (UploadEvent) event.getOrigin();
		Fileupload fileupload = (Fileupload) evt.getTarget();
		Media media = evt.getMedia();
		String name = media.getName();
		Textbox textbox = (Textbox) fileupload.getPreviousSibling();
		String ext = "";
		if (name.indexOf(".") > 0)
		{
			ext = name.substring(name.indexOf("."));
			name = name.substring(0, name.indexOf("."));
			System.out.print(name + "--" + ext);
		}
		if (textbox.getValue() != null && !textbox.getValue().equals("-重命名-"))
		{
			name = textbox.getValue();
		}
		List<Map<String, Object>> list = (List<Map<String, Object>>) filesgrid.getModel();
		Map<String, Object> val;
		if (staticfils.contains(name))
		{
			val = list.get(staticfils.indexOf(name));
			val.put("attr", null);
		} else
		{
			val = new HashMap<String, Object>();
			list.add(val);
		}
		val.put("name", name);
		val.put("filename", name + ext);
		val.put("isFile", true);
		Attachment att = new Attachment(attachfilePath, name + ext, media);
		val.put("attr", att);
		textbox.setValue("-重命名-");
	}
	public void onUpload(ForwardEvent event)
	{
		UploadEvent evt = (UploadEvent) event.getOrigin();
		Fileupload fileupload = (Fileupload) evt.getTarget();
		Media media = evt.getMedia();
		// String name = media.getName();
		Row row = (Row) fileupload.getParent().getParent();
		Map<String, Object> val = (Map<String, Object>) row.getValue();
		List<Map<String, Object>> list = (List<Map<String, Object>>) filesgrid.getModel();
		val = list.get(staticfils.indexOf(val.get("name")));
		Attachment attr = new Attachment(attachfilePath, media.getName(), media);
		// List<Map<String, Object>> list = (List<Map<String, Object>>) filesgrid.getModel();
		// Map<String, Object> attr = list.get(staticfils.indexOf(val.get("name")));
		val.put("filename", media.getName());
		val.put("isFile", true);
		val.put("attr", attr);
		filesgrid.setModel((ListModel) list);
	}
	public void onRemoveUpload(ForwardEvent event)
	{
		Toolbarbutton but = (Toolbarbutton) event.getOrigin().getTarget();
		((Label) but.getPreviousSibling()).setValue("");
		Row row = (Row) but.getParent().getParent();
		List<Map<String, Object>> list = (List<Map<String, Object>>) filesgrid.getModel();
		Map<String, Object> val = (Map<String, Object>) row.getValue();
		if (staticfils.contains(val.get("name")))
		{
			val = list.get(staticfils.indexOf(val.get("name")));
			val.put("filename", "");
			val.put("attr", null);
			val.put("isFile", false);
		} else
		{
			((List) filesgrid.getModel()).remove(val);
		}
		filesgrid.setModel((ListModel) list);
	}
	public void onProductSelected(ForwardEvent event)
	{
		Listbox box = (Listbox) event.getOrigin().getTarget();
		@SuppressWarnings("unchecked")
		List<Listitem> items = box.getItems();
		for (Listitem item : items)
		{
			productModelList.remove(item.getValue());
		}
		Set<Listitem> selc = box.getSelectedItems();
		for (Listitem item : selc)
		{
			productModelList.add((Map) item.getValue());
		}
	}
	public void onSubmit(ForwardEvent event)
			throws InterruptedException
	{
		applicationPublic.setApplyType(applyType.getValue());
		applicationPublic.setBusinessType(businessType.getValue());
		applicationPublic.setProductCatalog(selectedProductCatalog.getProductName()); 
		applicationPublic.setContractAgree(contractAgree.getSelectedItem().getValue());
		applicationPublic.setContractAgree(contractApprove.getSelectedItem().getValue());
		applicationPublic.setApplyEnterprise(EnterpriseInfoServiceUtil.getCurrentEnterprise());
		if (contractYear.getValue() != null)
			applicationPublic.setContractYear(contractYear.getValue() + "");
		if (contractMonth.getValue() != null)
			applicationPublic.setContractMonth(contractMonth.getValue() + "");
		//
		// selectgrid.getCell(row, col);
		//
		List<ApplicationPublicInfoAttachment> atts = new ArrayList<ApplicationPublicInfoAttachment>();
		//
		//
		List<Map<String, Object>> list = (List<Map<String, Object>>) filesgrid.getModel();
		int i = 0;
		for (Map<String, Object> obj : list)
		{
			if (null == obj.get("attr"))
				continue;
			Attachment att = (Attachment) obj.get("attr");
			ApplicationPublicInfoAttachment aa = new ApplicationPublicInfoAttachment();
			aa.setAttachment(att);
			aa.setName((String) obj.get("name"));
			aa.setSn(i++);
			aa.setApplicationPublicInfo(applicationPublic);
			atts.add(aa);
		}
		//
		// atts
		//
		// 处理文件关系
		System.out.println(productModelList);
		System.out.println(atts);
		applicationPublicService.doSaveSubmit(applicationPublic, productModelList, atts, userInfo.getId());
		// applicationPublicService.
		Messagebox.show("认证申请成功提交，请等待审批!", "系统提示", Messagebox.OK, Messagebox.INFORMATION);
		self.detach();
		Events.sendEvent("onClose", self, null);
	}
	public void openFile(ForwardEvent event)
	{
		String id = (String)event.getOrigin().getData();
		System.out.println("id");
	}
	
	// 选择产品大类
	public void onSelectProductCatalog(ForwardEvent event)
	{
		Combobox cbx = (Combobox) event.getOrigin().getTarget();
		selectedProductCatalog = (ProductCatalogueInfo) cbx.getSelectedItem().getValue();
	}
	// 添加產品
	@SuppressWarnings("unchecked")
	public void onAddProducts()
			throws InterruptedException
	{
		if (selectedProductCatalog == null)
		{
			Messagebox.show("请选择一种产品大类!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		HashMap params = new HashMap();
		params.put("productCatalogInfo", selectedProductCatalog);
		Window objWindow = (Window) Executions.createComponents("/views/Index_enterprise/item/add_product.zul", null, params);
		objWindow.addEventListener("onAddProductModel", new EventListener() { 
				@Override
				public void onEvent(Event event) throws Exception {
					// 刷新申请产品列表
					List<Map> list = (List<Map>) event.getData();
					for (Map item : list)
					{
						if (productModelMap.get(item.get("id")) == null)
						{
							productModelList.add(item);
							productModelMap.put(item.get("id"), item);
						}
					}
					production.setDisabled(true);
					productmodelLbx.setModel(new ListModelList(productModelList));
				} 
		 }) ; 
		try
		{
			objWindow.doModal();
		} catch (SuspendNotAllowedException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	public void onDeleteProductModel(ForwardEvent event)
	{
		Toolbarbutton delTlb = (Toolbarbutton) event.getOrigin().getTarget();
		Map map = (Map) delTlb.getAttribute("productmodel");
		productModelList.remove(map);
		productModelMap.remove(map.get("id"));
		if (productModelList.size() == 0)
			production.setDisabled(false);
		productmodelLbx.setModel(new ListModelList(productModelList));
	}
 
}

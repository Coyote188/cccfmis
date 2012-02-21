package cccf.ma.web.zk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.modellite.spring.BeanAdapter;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import cccf.ma.common.JbpmUtil;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.ApplicationPublicInfo;
import cccf.ma.model.ApplicationPublicInfoAttachment;
import cccf.ma.model.Attachment;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.service.ApplicationInfoServiceUtil;
import cccf.ma.service.ApplicationPublicInfoServiceUtil;
import cccf.ma.service.ApplicationPublicService;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;
import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.service.ProcessFormServiceUtil;
import com.aidi.core.service.BaseDAOServcieUtil;

public class ProductAccreditationApplyController
		extends GenericForwardComposer
{
	// 事件
	public static final String						ON_REFRESH			= "onRefreshModel";
	private ApplicationPublicInfo					applicationPublic;
	private List<ProductCatalogueInfo>				productionList;
	private ProductCatalogueInfo					selectedProductCatalog;
	private Map										params				= new HashMap();
	private List<ApplicationPublicInfoAttachment>	atts				= new ArrayList<ApplicationPublicInfoAttachment>();
	private Radiogroup								constractChoice_AgreeRgp, constractChoice_ApproveRgp;
	private Window									pAAWin;
	private Button									saveBtn, submitBtn;
	private Media									media;
	private String									attachfilePath;
	private Toolbarbutton							del_businessLisence, del_code, del_inspection, del_plan, del_programFiles, del_certification;
	private Combobox								apptype, bustype, production;
	private Listbox									productmodelLbx;
	private List<Map>								productModelList	= new ArrayList<Map>();
	private Map										productModelMap		= new HashMap();
	private ApplicationPublicService				applicationPublicService;
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		attachfilePath = "/attachments/" + "application" + "/" + UUID.randomUUID().toString();
		super.doAfterCompose(comp);
		applicationPublic.setApplyType(apptype.getItemAtIndex(0).getLabel());
		applicationPublic.setBusinessType(bustype.getItemAtIndex(0).getLabel());
		applicationPublicService = BeanAdapter.getBean("ApplicationPublicService", ApplicationPublicService.class);
	}
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		applicationPublic = new ApplicationPublicInfo();
		productionList = ProductCatalogueInfoServiceUtil.getDataListByLevel(1);
		java.util.Calendar cal = java.util.Calendar.getInstance();
		applicationPublic.setContractYear(String.valueOf(cal.get(Calendar.YEAR)));
		applicationPublic.setContractMonth(String.valueOf(cal.get(Calendar.MONTH)));
		return super.doBeforeCompose(page, parent, compInfo);
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
	// 刷新申请产品列表
	public void onRefreshModel(Event event)
	{
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
	private static final long	serialVersionUID	= 1L;
	// 保存数据
	public void onSave()
	{
		applicationPublic.setContractAgree(constractChoice_AgreeRgp.getSelectedItem().getLabel());
		applicationPublic.setContractApprove(constractChoice_ApproveRgp.getSelectedItem().getLabel());
		applicationPublic.setApplyEnterprise(EnterpriseInfoServiceUtil.getCurrentEnterprise());
		applicationPublicService.doSave(applicationPublic, productModelList, atts);
	}
	public void onSubmit()
			throws InterruptedException
	{
		applicationPublic.setContractAgree(constractChoice_AgreeRgp.getSelectedItem().getLabel());
		applicationPublic.setContractApprove(constractChoice_ApproveRgp.getSelectedItem().getLabel());
		applicationPublic.setApplyEnterprise(EnterpriseInfoServiceUtil.getCurrentEnterprise());
		applicationPublicService.doSaveSubmit(applicationPublic, productModelList, atts, UserInfoServiceUtil.getCurrentLoginUser().getId());
		Messagebox.show("认证申请成功提交，请等待审批!", "系统提示", Messagebox.OK, Messagebox.INFORMATION);
		self.detach();
		Events.sendEvent("onClose", self, null);
	}
	// 上传附件
	public void onAttachmentUpload(ForwardEvent event)
	{
		UploadEvent uev = (UploadEvent) event.getOrigin();
		media = uev.getMedia();
		ApplicationPublicInfoAttachment aa = new ApplicationPublicInfoAttachment();
		Attachment att = new Attachment(attachfilePath, media.getName(), media);
		aa.setAttachment(att);
		if ("businessLisence".equals(event.getData()))
		{
			aa.setName("企业营业执照副本复印件");
			aa.setSn(1);
			att.setName("企业营业执照副本复印件");
			whetherAttachmentOnly("企业营业执照副本复印件");
			del_businessLisence.setLabel(media.getName());
			del_businessLisence.setVisible(true);
		}
		if ("code".equals(event.getData()))
		{
			aa.setName("企业组织机构代码证复印件");
			aa.setSn(2);
			att.setName("企业组织机构代码证复印件");
			whetherAttachmentOnly("企业组织机构代码证复印件");
			del_code.setLabel(media.getName());
			del_code.setVisible(true);
		}
		if ("inspection".equals(event.getData()))
		{
			aa.setName("产品检验设备清单");
			aa.setSn(3);
			att.setName("产品检验设备清单");
			whetherAttachmentOnly("产品检验设备清单");
			del_inspection.setLabel(media.getName());
			del_inspection.setVisible(true);
		}
		if ("plan".equals(event.getData()))
		{
			aa.setName("企业生产配置平面图");
			aa.setSn(4);
			att.setName("企业生产配置平面图");
			whetherAttachmentOnly("企业生产配置平面图");
			del_plan.setLabel(media.getName());
			del_plan.setVisible(true);
		}
		if ("programFiles".equals(event.getData()))
		{
			aa.setName("《质量手册》和《程序文件》");
			aa.setSn(5);
			att.setName("《质量手册》和《程序文件》");
			whetherAttachmentOnly("《质量手册》和《程序文件》");
			del_programFiles.setLabel(media.getName());
			del_programFiles.setVisible(true);
		}
		if ("certification".equals(event.getData()))
		{
			aa.setName("原产品认证证书复印件");
			aa.setSn(6);
			att.setName("原产品认证证书复印件");
			whetherAttachmentOnly("原产品认证证书复印件");
			del_certification.setLabel(media.getName());
			del_certification.setVisible(true);
		}
		atts.add(aa);
	}
	// 删除附件
	public void onDeleteAttachfile(ForwardEvent event)
	{
		if ("businessLisence".equals(event.getData()))
		{
			whetherAttachmentOnly("企业营业执照副本复印件");
			del_businessLisence.setVisible(false);
		}
		if ("code".equals(event.getData()))
		{
			whetherAttachmentOnly("企业组织机构代码证复印件");
			del_code.setVisible(false);
		}
		if ("inspection".equals(event.getData()))
		{
			whetherAttachmentOnly("产品检验设备清单");
			del_inspection.setVisible(false);
		}
		if ("plan".equals(event.getData()))
		{
			whetherAttachmentOnly("企业生产配置平面图");
			del_plan.setVisible(false);
		}
		if ("programFiles".equals(event.getData()))
		{
			whetherAttachmentOnly("《质量手册》和《程序文件》");
			del_programFiles.setVisible(false);
		}
		if ("certification".equals(event.getData()))
		{
			whetherAttachmentOnly("原产品认证证书复印件");
			del_certification.setVisible(false);
		}
		System.out.println(atts.size());
	}
	// 从现有列表中移除 重新上传的文件
	private void whetherAttachmentOnly(String fileName)
	{
		if (!atts.isEmpty())
		{
			Iterator it = atts.iterator();
			while (it.hasNext())
			{
				ApplicationPublicInfoAttachment a = (ApplicationPublicInfoAttachment) it.next();
				if (a.getAttachment().getName().equals(fileName))
					it.remove();
			}
		}
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
		params.clear();
		params.put("productCatalogInfo", selectedProductCatalog);
		Window objWindow = (Window) Executions.createComponents("/SysForm/application/add_product.zul", null, params);
		objWindow.setParent(pAAWin);
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
	public List<ProductCatalogueInfo> getProductionList()
	{
		return productionList;
	}
	public void setProductionList(List<ProductCatalogueInfo> productionList)
	{
		this.productionList = productionList;
	}
	public ApplicationPublicInfo getApplicationPublic()
	{
		return applicationPublic;
	}
	public void setApplicationPublic(ApplicationPublicInfo applicationPublic)
	{
		this.applicationPublic = applicationPublic;
	}
}

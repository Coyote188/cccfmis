package cccf.ma.web.zk;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.EnterpriseOwnActivatedProductListInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.EnterpriseOwnActivatedProductListInfoServiceUtil;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;

public class ChooseProductActiveController extends GenericForwardComposer{

	private static final long serialVersionUID = 1L;
	private List<EnterpriseOwnActivatedProductListInfo> activated_erp_list;//已激活的产品列表
	private List<ProductCatalogueInfo> all_p_list;//所有产品列表
	private Window chooseproWin;
	private Tree chooseprocTre;
	private Div actiListDiv;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		
		super.doAfterCompose(comp);
		//为产品目录增加选择征听
		chooseprocTre.addEventListener(Events.ON_SELECT, new EventListener(){

			@SuppressWarnings("unchecked")
			@Override
			public void onEvent(Event event) throws Exception {
				
					List<Checkbox> ckbList=actiListDiv.getChildren();
					ckbList.clear();
					Checkbox ckb;
					Set setTre=chooseprocTre.getSelectedItems();
					List<Treeitem> items=new ArrayList(setTre);
					for(Treeitem item:items)
					{
						if(item.isCheckable())
						{
							ckb= new Checkbox();
							ckb.setLabel(item.getLabel());
							ckb.setValue(((ProductCatalogueInfo)item.getValue()).getId());
							ckb.setChecked(true);
							actiListDiv.appendChild(ckb);
						}
					}
				
				
			}
			
		});
	}

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) {
		all_p_list=ProductCatalogueInfoServiceUtil.getDataListByLevel(1);
		activated_erp_list=EnterpriseOwnActivatedProductListInfoServiceUtil.findCurrentEnterprise();
		page.setAttribute("pgroup",all_p_list);
		return super.doBeforeCompose(page, parent, compInfo);
	}
	public Set filterSetHandle(Set oriset)
	{
		for(Iterator<ProductCatalogueInfo> it=oriset.iterator();it.hasNext();)
		{
			ProductCatalogueInfo p=it.next();
			boolean isEx=false;
			for(EnterpriseOwnActivatedProductListInfo e:activated_erp_list)
			{
				if(p.getId().equals(e.getProduct().getId()))
				{
					isEx=true;
					break;
				}
			}
			if(isEx)
			{
				it.remove();
			}
		}
		return oriset;
	}
	public void onTreeitemOpen(ForwardEvent event)
	{
		Treeitem selitem=(Treeitem) event.getOrigin().getTarget();
		ProductCatalogueInfo pcInfo= (ProductCatalogueInfo) selitem.getValue();
		selitem.getTreechildren().detach();
		selitem.appendChild(new Treechildren());
		Set cpclist=pcInfo.getProductChildren();
		Treeitem item=null;
		for(Iterator<ProductCatalogueInfo> iterator=cpclist.iterator();iterator.hasNext();)
		{//创建二级目录
			ProductCatalogueInfo cpcInfo=iterator.next();
			System.out.println(cpcInfo.getProductName());
			item=new Treeitem();
			item.setCheckable(false);
			item.setOpen(false);
			item.setValue(cpcInfo);
			item.setLabel(cpcInfo.getProductName());
			item.appendChild(new Treechildren());
			item.addEventListener(Events.ON_OPEN, new EventListener(){

				@Override
				public void onEvent(Event arg0) throws Exception {
					Treeitem target=(Treeitem) arg0.getTarget();
					ProductCatalogueInfo pcInfo=(ProductCatalogueInfo) target.getValue();
					System.out.println("--"+pcInfo.getProductName());
					target.getTreechildren().detach();
					target.appendChild(new Treechildren());
					Treeitem item=null;
					Set fset=filterSetHandle(pcInfo.getProductChildren());
					for(Iterator<ProductCatalogueInfo> iterator=fset.iterator();iterator.hasNext();)
					{//创建三级目录
						ProductCatalogueInfo cpcInfo=iterator.next();
						item=new Treeitem();
						item.setOpen(false);
						item.setValue(cpcInfo);
						item.setLabel(cpcInfo.getProductName());
						item.appendChild(new Treechildren());
						item.addEventListener(Events.ON_OPEN, new EventListener(){

							@Override
							public void onEvent(Event arg0) throws Exception {
								Treeitem target=(Treeitem) arg0.getTarget();
								ProductCatalogueInfo pcInfo=(ProductCatalogueInfo) target.getValue();
								System.out.println("----"+pcInfo.getProductName());
								target.getTreechildren().detach();
								target.appendChild(new Treechildren());
								Treeitem item=null;
								Set fset=filterSetHandle(pcInfo.getProductChildren());
								for(Iterator<ProductCatalogueInfo> iterator=fset.iterator();iterator.hasNext();)
								{//创建四级目录
									ProductCatalogueInfo cpcInfo=iterator.next();
									item=new Treeitem();
									item.setValue(cpcInfo);
									item.setLabel(cpcInfo.getProductName());
									target.getTreechildren().appendChild(item);
								}
								
							}
							
						});
						target.getTreechildren().appendChild(item);
					}
					
				}
				
			});
			selitem.getTreechildren().appendChild(item);
		}
	}
	public void onClick$submitBtn(ForwardEvent forwardEvent) throws InterruptedException
	{
		List<Checkbox> ckbList=actiListDiv.getChildren();
		ProductCatalogueInfo _product;
		EnterpriseOwnActivatedProductListInfo eoap;
		EnterpriseInfo enpr;
		enpr=EnterpriseInfoServiceUtil.findEnterpriseByUser(
					        UserInfoServiceUtil.getCurrentLoginUser());
		boolean isNull=true;
		if(ckbList.size()>0)
		for(Checkbox cbx:ckbList)
		{
			if(cbx.isChecked())
			{
				_product= ProductCatalogueInfoServiceUtil.getById(cbx.getValue());
				System.out.println(_product.getProductName());
				 eoap=new EnterpriseOwnActivatedProductListInfo();
				 eoap.setProduct(_product);
				 eoap.setEnterprise(enpr);
				 eoap.setActivateStatus(0);
				 eoap.setApplydate(new Date());
				 EnterpriseOwnActivatedProductListInfoServiceUtil.create(eoap);
				 isNull=false;
			}
			
		}
		else
			throw new WrongValueException("提交失败,待激活列表为空!");
		
		if(isNull==true)
			throw new WrongValueException("提交失败,待激活列表没有选中任何产品!");
		else
		{
			Messagebox.show("提交成功!", "提示", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	public void onCheck$selectCbx(Event event)
	{
		Checkbox tcbx=(Checkbox)event.getTarget().getFellow("selectCbx");
		List<Checkbox> ckbList=actiListDiv.getChildren();
		if(ckbList.size()>0){
			if(tcbx.isChecked()){
				for(Checkbox cbx:ckbList)
				{
					cbx.setChecked(true);
				}
			}
			else
			{
				for(Checkbox cbx:ckbList)
				{
					cbx.setChecked(false);
				}
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void onCheck$InverseCbx(Event event)
	{
		List<Checkbox> ckbList=actiListDiv.getChildren();
		if(ckbList.size()>0)
		for(Checkbox cbx:ckbList)
		{
			cbx.setChecked(cbx.isChecked()?false:true);
		}
	}
	public void onClick$cancelBtn(Event event) throws InterruptedException
	{
		if (Messagebox.show("您确定要放弃本次激活申请吗?", "提示", Messagebox.OK | Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.OK) {
			chooseproWin.getParent().detach();
		}
		
	}
}

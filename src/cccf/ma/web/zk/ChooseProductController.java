package cccf.ma.web.zk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import cccf.ma.model.EnterpriseInfo;
import cccf.ma.model.EnterpriseOwnActivatedProductListInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.service.EnterpriseInfoServiceUtil;
import cccf.ma.service.EnterpriseOwnActivatedProductListInfoServiceUtil;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;
import cccf.ma.service.ProductCatalogueTreeService;

@SuppressWarnings("serial")
public class ChooseProductController extends GenericForwardComposer{
	Window chooseproWin;
	private Tree chooseprocTre;
	private Div actiListDiv;
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp); 
		
		chooseprocTre.setModel(new ProductCatalogueTreeChoose());
		chooseprocTre.setTreeitemRenderer(new ChooseTreeitemRenderer());
		
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
							ckb.setValue((String)item.getValue());
							ckb.setChecked(true);
							actiListDiv.appendChild(ckb);
						}
					}
				
				
			}
			
		});
	}
	@SuppressWarnings("unchecked")
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
			chooseproWin.getParent().detach();
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
	/**
	 * 内部类的使用,如果放在上面的方法中应放到最后一句前
	 * @author Administrator
	 *
	 */
	private class ChooseTreeitemRenderer implements TreeitemRenderer
	{

		@Override
		public void render(final Treeitem item, Object data) throws Exception {
			

			final ProductCatalogueInfo product = (ProductCatalogueInfo)data;
			//
			item.setValue(product.getId());
			
			Treecell tc=new Treecell(product.getProductName());
			
			Treerow tr = null;
			if(item.getTreerow()==null){
				tr = new Treerow();
				tr.setParent(item);
			}else{
				tr = item.getTreerow(); 
				tr.getChildren().clear();
			}
			tc.setParent(tr);
			if(!ProductCatalogueTreeService.checkIsLeafFilterable(product)||product.getProductLevel()==1||product.getProductLevel()==2)
			{
				item.setCheckable(false);
				
			}
			if(ProductCatalogueTreeService.checkIsLeafFilterable(product)&&product.getProductChildren().size()>0)
				item.setCheckable(false);
			item.setOpen(true);
		}
		
	}
	
}

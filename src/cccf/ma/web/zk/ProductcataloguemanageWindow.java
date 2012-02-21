package cccf.ma.web.zk;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;
import cccf.ma.model.*;
import cccf.ma.service.*;

public class ProductcataloguemanageWindow extends Window{

   Long taskInstanceId;
   String processId = "0";
   String userId;
   HashMap params = new HashMap();
    public ProductCatalogueInfo productCatalogueInfo;
    public ProductCatalogueInfo getProductCatalogueInfo(){
           return productCatalogueInfo;
    }
    public void setProductCatalogueInfo(ProductCatalogueInfo entity){
           this.productCatalogueInfo=entity;
    }
    
    public void onCreate(){
    List<ProductCatalogueInfo> list=ProductCatalogueInfoServiceUtil.getAll();
    if (list != null &&
				!list.isEmpty())
		{
			setProductCatalogueInfo(list.iterator().next());
		}
	         
		
	}
	
	public List<ProductCatalogueInfo> getObjList()
	{
		Listbox listbox = getListbox();
		List<ProductCatalogueInfo> list = (List<ProductCatalogueInfo>)listbox.getModel();
		return list;
	}
	
	public Listbox getListbox()
	{
		return (Listbox)this.getFellow("productCatalogueInfoListbox");
	}
	
	public void onAdd()
	{	
		params.put("productCatalogue", null);	
		params.put("cmd", "add");		
		openEditWindow(params);
	}
	
	public void openEditWindow(Map params)	{
		params.put("parentListbox", getListbox());
		Window objWindow = (Window) Executions.createComponents("productcatalogue-edit.zul",null,params);
		
		try {
			
			objWindow.doModal();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void onDelete() throws InterruptedException
	{
		if (Messagebox.show("是否要删除记录?", "删除?", Messagebox.OK | Messagebox.NO,
				Messagebox.QUESTION) == Messagebox.OK) {			
			Listbox listbox = getListbox();
			Set selectItems=listbox.getSelectedItems();
			Iterator iterator = selectItems.iterator();
			//String value = "";
			while(iterator.hasNext()) {
				Listitem listitem=(Listitem)iterator.next();
				String id=listitem.getValue().toString();
				ProductCatalogueInfo delcom=ProductCatalogueInfoServiceUtil.getByPrimaryKey(id);
				ProductCatalogueInfoServiceUtil.delete(delcom);
			}

			if(selectItems.size()>0){
				refreshUsersListbox();				
			}
		}
	}
	
	public void onEdit()
	{
		params.put("productCatalogue", getProductCatalogueInfo());		
		params.put("cmd", "edit");	
		openEditWindow(params);
	}
	
	public void refreshUsersListbox()
	{
		List<ProductCatalogueInfo> list = getObjList();
		list.clear();
		list.addAll(ProductCatalogueInfoServiceUtil.getAll());		
	}
	
	public void onSearch()
	{
		String qstr="";
		Hbox hbox=(Hbox)this.getFellow("querybox");
		 		
 		Textbox productNo=(Textbox)hbox.getFellow("productNo");
 		if(productNo.getText()!=""&&productNo.getText()!=null)
 		{
	 		
			qstr=qstr+" and productNo like ";
			
			qstr=qstr+"'%"+productNo.getText()+"%'";
			
		}
		 		
 		Textbox productName=(Textbox)hbox.getFellow("productName");
 		if(productName.getText()!=""&&productName.getText()!=null)
 		{
	 		
			qstr=qstr+" and productName like ";
			
			qstr=qstr+"'%"+productName.getText()+"%'";
			
		}
		 		
 		Combobox activateStatus=(Combobox)hbox.getFellow("activateStatus");
 		if(activateStatus.getText()!=""&&activateStatus.getText()!=null)
 		{
	 		
			qstr=qstr+" and activateStatus=";
			
			qstr=qstr+activateStatus.getText();
			
		}
		 		
 		Textbox certificatesNo=(Textbox)hbox.getFellow("certificatesNo");
 		if(certificatesNo.getText()!=""&&certificatesNo.getText()!=null)
 		{
	 		
			qstr=qstr+" and certificatesNo like ";
			
			qstr=qstr+"'%"+certificatesNo.getText()+"%'";
			
		}
		
		
		if(qstr!="")
		{
			qstr=qstr.replaceFirst("and","");
			qstr="from ProductCatalogueInfo where "+qstr;
		}else
		{
			qstr="from ProductCatalogueInfo";		
		}
		List<ProductCatalogueInfo> searchResult=ProductCatalogueInfoServiceUtil.findByQuery(qstr);
		List<ProductCatalogueInfo> list = getObjList();
		list.clear();
		list.addAll(searchResult);	
	}
	
	
		
		
	

}
	
	
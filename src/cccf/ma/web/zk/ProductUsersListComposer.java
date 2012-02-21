package cccf.ma.web.zk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.Product_User_ListInfo;
import cccf.ma.service.Product_User_ListInfoServiceUtil;

public class ProductUsersListComposer  extends GenericForwardComposer{

	public String getProName() {
		return proName;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Grid reviewGrd,checkGrd,assessGrd;
	List<UserInfo> reviewGrdList=new ArrayList();
	List<UserInfo> checkGrdList=new ArrayList();
	List<UserInfo> assessGrdList=new ArrayList();
	private ListModelList gridModel;
	
	Map params=Executions.getCurrent().getArg();
	ProductCatalogueInfo product;
	String proName;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		product=(ProductCatalogueInfo)params.get("productCatalogue");
		proName=product.getProductName();
		reviewGrdList=Product_User_ListInfoServiceUtil.getUserByProductofJurisdiction(product,0);
		checkGrdList=Product_User_ListInfoServiceUtil.getUserByProductofJurisdiction(product,1);
		assessGrdList=Product_User_ListInfoServiceUtil.getUserByProductofJurisdiction(product,2);
		
		List<List<UserInfo>> model=new ArrayList<List<UserInfo>>();
		List<UserInfo> userlist=UserInfoServiceUtil.getByUserType(0);
		
		int rowclu=userlist.size()/4;
		int row_clu=userlist.size()%4;
		for(int i=0;i<rowclu;i++)
		{
			List<UserInfo> rowlist=new ArrayList<UserInfo>();
			rowlist=userlist.subList((i+3*i), ((i+3)+3*i)+1);//subList(from:0,to:4),截取list为0,1,2,3
			model.add(rowlist);
		}
		if(row_clu>0)
		{
			List<UserInfo> rowlist=new ArrayList<UserInfo>();
			rowlist=userlist.subList(4*rowclu,userlist.size());
			model.add(rowlist);
		}
		
		gridModel = new ListModelList(model,true);
		reviewGrd.setModel(gridModel);
		checkGrd.setModel(gridModel);
		assessGrd.setModel(gridModel);
		reviewGrd.setAttribute("selflist", reviewGrdList);
		checkGrd.setAttribute("selflist", checkGrdList);
		assessGrd.setAttribute("selflist", assessGrdList);
		
		reviewGrd.setRowRenderer(new RowRenderer() {			
			@Override
			public void render(Row row, Object data) throws Exception {
				ImplementRowRenderer(row,data);
			}
		});
		checkGrd.setRowRenderer(new RowRenderer() {			
			@Override
			public void render(Row row, Object data) throws Exception {
				ImplementRowRenderer(row,data);
			}
		});
		assessGrd.setRowRenderer(new RowRenderer() {			
			@Override
			public void render(Row row, Object data) throws Exception {
				ImplementRowRenderer(row,data);
			}
		});
		
	}
	
	public void onClick$saveBtn(Event event) throws InterruptedException
	{
		Product_User_ListInfo pul;
		Product_User_ListInfoServiceUtil.deleteByProduct(product);
		for(UserInfo user:reviewGrdList)
		{
			pul=new Product_User_ListInfo();
			pul.setProduct(product);
			pul.setUser(user);
			pul.setType(0);
			Product_User_ListInfoServiceUtil.saveOrUpdate(pul);
		}
		for(UserInfo user:checkGrdList)
		{
			pul=new Product_User_ListInfo();
			pul.setProduct(product);
			pul.setUser(user);
			pul.setType(1);
			Product_User_ListInfoServiceUtil.saveOrUpdate(pul);
		}
		for(UserInfo user:assessGrdList)
		{
			pul=new Product_User_ListInfo();
			pul.setProduct(product);
			pul.setUser(user);
			pul.setType(2);
			Product_User_ListInfoServiceUtil.saveOrUpdate(pul);
		}
		Messagebox.show("保存成功", "提示", Messagebox.OK,
				Messagebox.INFORMATION);
	}
	
	
	/**
	 * 实现数据绑定
	 * @param row
	 * @param data
	 */
	public void ImplementRowRenderer(final Row row,Object data)
	{
		if (data == null) return;
		List<UserInfo> userRows = (List<UserInfo>) data;
		for(final UserInfo user:userRows)
		{
			if(user!=null)
			{
				Grid bindGrd=row.getGrid();
				final List<UserInfo> list=(List<UserInfo>) bindGrd.getAttribute("selflist");
				final Checkbox ckb=new Checkbox();
				ckb.setLabel(user.getName());
				if(userinUserlist(user,list))
					ckb.setChecked(true);
				//ckb.setAttribute("user", user);
				ckb.addEventListener(Events.ON_CHECK,new EventListener(){

					@Override
					public void onEvent(Event event) throws Exception {
						if(ckb.isChecked())
							list.add(user);
						else
							list.remove(user);
						
					}
				});
				row.appendChild(ckb);
			}
		}
		
	}
	@SuppressWarnings("unused")
	private boolean userinUserlist(UserInfo user,List<UserInfo>list)
	{
		if(list!=null)
		for(UserInfo u:list)
		{
			if(u.getId().equals(user.getId()))
				return true;
		}
		return false;
	}

}

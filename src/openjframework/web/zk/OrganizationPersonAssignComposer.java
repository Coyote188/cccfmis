package openjframework.web.zk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import openjframework.model.OrganizationInfo;
import openjframework.model.ProfileInfo;
import openjframework.service.ProfileInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Window;

public class OrganizationPersonAssignComposer extends GenericForwardComposer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2885414236126985181L;
	private Window assignUserWin;
	private Grid enableGrd,ownGrd;
	private Div own_Div,own_Grd_Div,d_Btn_Div,enable_Div,enable_Grd_Div,a_Btn_Div;
	private ListModelList gridModel;
	private List<List<ProfileInfo>> model;
	private Label currentDepart;
	Map params=Executions.getCurrent().getArg();
	OrganizationInfo org;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		org=(OrganizationInfo)params.get("organization");
		currentDepart.setValue(org.getOrganizationName());
		redrawRow();
		
		
	}
	public void redrawRow()
	{
		
		List<ProfileInfo> userlist=ProfileInfoServiceUtil.getByNotAssign();
		List<ProfileInfo> userlist_self=ProfileInfoServiceUtil.getByOrganization(org);
		if(userlist!=null&&userlist.size()>0)
		{
			enable_Div.setVisible(false);
			enable_Grd_Div.setVisible(true);
			a_Btn_Div.setVisible(true);
			model=new ArrayList<List<ProfileInfo>>();
			model.clear();
			int rowclu=userlist.size()/6;
			int row_clu=userlist.size()%6;
			for(int i=0;i<rowclu;i++)
			{
				List<ProfileInfo> rowlist=new ArrayList<ProfileInfo>();
				rowlist=userlist.subList((i+5*i), ((i+5)+5*i)+1);//subList(from:0,to:4),截取list为0,1,2,3
				model.add(rowlist);
			}
			if(row_clu>0)
			{
				List<ProfileInfo> rowlist=new ArrayList<ProfileInfo>();
				rowlist=userlist.subList(6*rowclu,userlist.size());
				model.add(rowlist);
			}
			
			gridModel = new ListModelList(model,true);
			enableGrd.setModel(gridModel);
			enableGrd.setRowRenderer(new RowRenderer() {			
				@Override
				public void render(Row row, Object data) throws Exception {
					ImplementRowRenderer(row,data);
				}
			});
		}
		else
		{
			enable_Div.setVisible(true);
			enable_Grd_Div.setVisible(false);
			a_Btn_Div.setVisible(false);
		}
		if(userlist_self!=null&&userlist_self.size()>0)
		{
			own_Div.setVisible(false);
			own_Grd_Div.setVisible(true);
			d_Btn_Div.setVisible(true);
			model=new ArrayList<List<ProfileInfo>>();
			model.clear();
			int rowclu=userlist_self.size()/6;
			int row_clu=userlist_self.size()%6;
			for(int i=0;i<rowclu;i++)
			{
				List<ProfileInfo> rowlist=new ArrayList<ProfileInfo>();
				rowlist=userlist_self.subList((i+5*i), ((i+5)+5*i)+1);//subList(from:0,to:4),截取list为0,1,2,3
				model.add(rowlist);
			}
			if(row_clu>0)
			{
				List<ProfileInfo> rowlist=new ArrayList<ProfileInfo>();
				rowlist=userlist_self.subList(6*rowclu,userlist_self.size());
				model.add(rowlist);
			}
			
			gridModel = new ListModelList(model,true);
			ownGrd.setModel(gridModel);
			ownGrd.setRowRenderer(new RowRenderer() {			
				@Override
				public void render(Row row, Object data) throws Exception {
					ImplementRowRenderer(row,data);
				}
			});
		}
		else
		{
			own_Div.setVisible(true);
			own_Grd_Div.setVisible(false);
			d_Btn_Div.setVisible(false);
		}
	}
	public void onClick$backBtn()
	{
		Tabpanel tp=(Tabpanel) assignUserWin.getParent();
		Tabs tbs=tp.getTabbox().getTabs();
		List<Tab> tab = tbs.getChildren();
		int i = -1;
		for (Tab t : tab) {
			if (t.getLabel().equals("人员分配管理")) {
				i = t.getIndex();
				break;
			}
		}
		if(i==-1)
		{
			assignUserWin.detach();
			tp.getLinkedTab().setLabel("人员分配管理");
			Window objWindow = (Window) Executions.createComponents("/SysForm/SystemAdministrator/organization-manage.zul",null,null);
			objWindow.setParent(tp);
			objWindow.doEmbedded();
		}
		else
			tp.getTabbox().setSelectedIndex(i);
	}
	public void onClick$deleteBtn()
	{
	  List<Checkbox>list=getGrdBySelectedChildren(ownGrd);
	  for(Checkbox ckb:list)
	  {
		  ProfileInfo user=(ProfileInfo) ckb.getAttribute("user");
		  user.setOrganization(null);
		  ProfileInfoServiceUtil.update(user);
		  
	  }
	  if(list!=null&&list.size()>0)
		  redrawRow();
	}
	public void onClick$addBtn()
	{
	  List<Checkbox>list=getGrdBySelectedChildren(enableGrd);
	  for(Checkbox ckb:list)
	  {
		  ProfileInfo user=(ProfileInfo) ckb.getAttribute("user");
		  user.setOrganization(org);
		  ProfileInfoServiceUtil.update(user);
	  }
	  if(list!=null&&list.size()>0)
		  redrawRow();
	}
	public void onClick$registTlb()
	{
		Tabpanel tp=(Tabpanel) assignUserWin.getParent();
		Tabs tbs=tp.getTabbox().getTabs();
		List<Tab> tab = tbs.getChildren();
		int i = -1;
		for (Tab t : tab) {
			if (t.getLabel().equals("用户注册")) {
				i = t.getIndex();
				break;
			}
		}
		if(i==-1)
		{
			assignUserWin.detach();
			tp.getLinkedTab().setLabel("用户注册");
			Window objWindow = (Window) Executions.createComponents("/SysForm/SystemAdministrator/registerUser.zul",null,null);
			objWindow.setParent(tp);
			objWindow.doEmbedded();
		}
		else
			tp.getTabbox().setSelectedIndex(i);
		
	}
	public void ImplementRowRenderer(Row row,Object data)
	{
		if (data == null) return;
		List<ProfileInfo> userRows = (List<ProfileInfo>) data;
		row.setDroppable("true");
		Checkbox ckb;
		for(final ProfileInfo user:userRows)
		{
			if(user!=null)
			{
				ckb=new Checkbox();
				ckb.setLabel(user.getName());
				ckb.setAttribute("user", user);
				//拖拽事件
//				ckb.setDraggable("true");
//				ckb.addEventListener(Events.ON_DROP,new EventListener(){
//
//					@Override
//					public void onEvent(Event event) throws Exception {
//						DropEvent dorpev=(DropEvent)event;
//						Component dragged=dorpev.getDragged();
//						Component self=dorpev.getTarget();
//						if(dragged instanceof Checkbox)
//						{
//							Row sw=(Row)self;
//							List<Checkbox>list=getEnableGrdBySelectedChildren();
//							for(Checkbox ckb:list)
//							{
//								ProfileInfo user=(ProfileInfo) ckb.getAttribute("user");
//								if(sw.getGrid().getId().equals("ownGrd"))
//								{
//									user.setOrganization(org);
//								}
//								else
//								{
//									user.setOrganization(null);
//								}
//							}
//							redrawRow();
//						}
//						
//					}
//				});
				row.appendChild(ckb);
			}
		}
	}
	public List<Checkbox> getGrdBySelectedChildren(Grid grd)
	{
		List<Checkbox>selectedlist=new ArrayList();
		List<Row>rowlist=grd.getRows().getChildren();
		for(Row row:rowlist)
		{
			List<Checkbox>list=row.getChildren();
			for(Checkbox ckb:list)
			{
				if(ckb.isChecked())selectedlist.add(ckb);
			}
		}
		return selectedlist;
	}
	
}

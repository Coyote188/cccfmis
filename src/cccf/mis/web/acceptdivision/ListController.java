package cccf.mis.web.acceptdivision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.modellite.spring.BeanAdapter;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.itextpdf.text.ListItem;

import cccf.ma.service.ApplicationPublicService;

public class ListController
		extends GenericForwardComposer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6095241973368025491L;
	private Vlayout				includelayout;
	private Map<Object, Object>	args				= new HashMap<Object, Object>();
	private Listbox				tastklist;
	private Combobox cb_stauts;
	private ApplicationPublicService applicationPublicService;  
	 
	
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp); 
		if (arg != null)
			args.putAll(arg);
		tastklist = (Listbox) Executions.createComponents("/views/TaskList/ApplyList.zul", includelayout, args);
		
		applicationPublicService = BeanAdapter.getBean("ApplicationPublicService", ApplicationPublicService.class);
	}
	public void onInited(ForwardEvent event)
			throws InterruptedException
	{
		List<Map<String, Object>> list = getTasks("受理分工", 0 , 10, null);
		
		ListModelList model = new ListModelList();
		
		if (list == null)
		{
			tastklist.setModel(model);
			Messagebox.show("没有查到到任何数据.", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		model.addAll(list);
		tastklist.setModel(model);
	}
	public void onFind()
			throws InterruptedException
	{
		List<Map<String, Object>> list = getTasks("受理分工", 0, 10, null);
		ListModelList model = new ListModelList();
		if (list == null)
		{
			tastklist.setModel(model);
			Messagebox.show("没有查到到任何数据.", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}model.addAll(list);
		tastklist.setModel(model);
	}
	
	private StringBuffer getWhere(){
		 StringBuffer hql =new StringBuffer();
		 if("待办".equals(cb_stauts.getValue())){
	         hql.append(" and pl.end is null");
	     }else{
	         hql.append(" and pl.end is not null");
	     }
		return hql;	 
	}
	/**
	 * 工作流结点查当前用户任务
	 * 
	 * @param task_name
	 * @param page
	 *            工作流结点名称
	 * @param args参数表
	 * @return
	 */
	private List<Map<String, Object>> getTasks(String task_name, int page, int pageSize, Map<String, Object> args)
	{
		 StringBuffer hql = new StringBuffer("select DISTINCT new map(o.sioid as no")
        .append(",o.applicationPublic.applyEnterprise.name as enterprise ")
        .append(",o.applicationPublic.applyEnterprise.state as state ")
        .append(",o.applicationPublic.applyEnterprise.location as region ")
        .append(",o.applicationPublic.businessType as business_type ")
        .append(",o.applicationPublic.productCatalog as product_catalog ")
        .append(",ap.productModel.surveyReport.surverOrgName as surver_orgname ")
        .append(",o.applicationPublic.applyType as apply_type ")
        .append(",o.applicationPublic.applydate as apply_date ")
        .append(",pl.end as end ") 
        .append(",case when pl.end is null then '处理' else '查看' end as butlabel ")
        .append(",o.sioid as applyno ") 
        .append(")")
        .append(" from ApplicationInfo o , ApplicationInfoProductModel ap")
        .append(" ,ProcessLog pl")
        .append(" where o = ap.applicationInfo ")
        .append(" and pl.boId=o.id ")
        .append(" and taskInstanceName='").append(task_name).append("' ")
        .append(getWhere());  
      
  System.out.println(hql);
		 List list =applicationPublicService.queryListForPage(page, pageSize, hql.toString());  
		 return list;
	}
	public void onButForward(ForwardEvent event){
		//tastklist.getSelectedItems();
		//Listitem item = (Listitem) event.getOrigin().getTarget().getParent().getParent();
		
		System.out.println(tastklist.getSelectedItem().getValue()); //;
	}
	/**
	 * 工作流结点查当前用户任务数
	 * 
	 * @param task_name
	 * @param page
	 *            工作流结点名称
	 * @param args参数表
	 * @return
	 */
	private int getTasksSize(String task_name, int pageSize, Map<String, Object> args)
	{
		 StringBuffer hql = new StringBuffer("select count(o) ")
	        .append(" from ApplicationInfo o , ApplicationInfoProductModel ap")
	        .append(" ,ProcessLog pl")
	        .append(" where o = ap.applicationInfo ")
	        .append(" and pl.boId=o.id ")
	        .append(" and ap.taskInstanceName='").append(task_name).append("' ")
	        .append(getWhere())  
	        .append(" group by o"); 
		 System.out.println(hql); 
		 
		return applicationPublicService.queryForResultSize(hql.toString()); 
	}
	
	/**
	 * 选择分工
	 * @param event
	 * @throws InterruptedException 
	 */
	public void onDivision(ForwardEvent event) throws InterruptedException{
		if(tastklist.getSelectedCount()==0){
			Messagebox.show("请选择申请！", "提示", Messagebox.OK, Messagebox.INFORMATION); 
		}else{ 
		    List list = new ArrayList();
		    for (Object obj : tastklist.getSelectedItems()){
		    	Listitem item = (Listitem)obj;
		    	list.add(item.getValue());
		    }
		    HashMap param = new HashMap();
		    param.put("selectedItems", list);
			Window win = (Window) Executions.createComponents("/views/AcceptDivision/form.zul", null, param); 
			win.doModal();
		//	win.getAttribute(name);
		}
		
	}
}

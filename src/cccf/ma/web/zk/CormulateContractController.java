package cccf.ma.web.zk;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import openjframework.bpm.model.TaskInstanceInfo;
import openjframework.bpm.service.TaskInstanceInfoServiceUtil;
import openjframework.service.UserInfoServiceUtil;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.db.TaskMgmtSession;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.api.Window;

import com.aidi.bpm.service.BpmUtil;
import com.itextpdf.text.DocumentException;

import cccf.ma.model.AppStatusRecordInfo;
import cccf.ma.model.ApplicationInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.pdfutil.ApplicationPdfDataBeanImpl;
import cccf.ma.pdfutil.PdfDataBean;
import cccf.ma.pdfutil.PdfUtil;
import cccf.ma.service.AppStatusRecordInfoServiceUtil;
import cccf.ma.service.ApplicationInfoServiceUtil;
import cccf.ma.web.zk.TodoTaskList.TaskInfo;

public class CormulateContractController extends GenericForwardComposer{


	private static final long serialVersionUID = 1L;
	static TaskMgmtSession		taskMgmtSession;
	static JbpmConfiguration	jbpmConfiguration	= JbpmConfiguration.getInstance();
	private Tree tlTre;
	private String userId;
	List<TaskInfo> a_list=new ArrayList<TaskInfo>();
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		
		super.doAfterCompose(comp);
	}

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) {
		userId=UserInfoServiceUtil.getCurrentLoginUser().getId();
		List<TaskInstance> todoTask=getTodoTaskList();
		InitAllTaskList(todoTask);//得到所有任务列表转换为TaskInfo
		page.setAttribute("epgroup",getEnterpriseList());
		return super.doBeforeCompose(page, parent, compInfo);
	}
	@SuppressWarnings("unchecked")
	public void onFormulateContract(ForwardEvent event)
	{
		Button ori= (Button) event.getOrigin().getTarget();
		Treeitem oriitem=(Treeitem)ori.getParent().getParent().getParent().getParent();
		List<Treeitem> items=oriitem.getTreechildren().getChildren();
		for(Treeitem item:items)
		{
			List<Treeitem> citems=item.getTreechildren().getChildren();
			for(Treeitem citem:citems)
			{
				if(citem.isSelected())
					System.out.println(citem.getLabel());
			}
		}
		
	}

	public void onTreeLargeClassOpen(ForwardEvent event)
	{
		Treeitem selitem = (Treeitem) event.getOrigin().getTarget();
		Treecols cols=selitem.getTree().getTreecols();
		Treecol col=(Treecol) cols.getFirstChild();
		col.setLabel("企业名称——产品大类");
		if(!selitem.isOpen())
			col.setLabel("企业名称");
		if(!cols.isVisible())
			cols.setVisible(true);
		String enName=(String) selitem.getValue();
		selitem.getTreechildren().detach();
		selitem.appendChild(new Treechildren());
		List<String> lgroup=getLargeClassNameListByEnterpriseName(enName);
//		System.out.println(lgroup.toString());
		if(!lgroup.isEmpty()){
			Treeitem item=null;
			Treerow row=null;
			Treecell cell=null;
			for(String s:lgroup)
			{
				item=new Treeitem();
				item.setOpen(false);
				item.setCheckable(false);
				row=new Treerow();
				cell=new Treecell(s);
				cell.setSpan(5);
				row.appendChild(cell);
				item.appendChild(row);
				final List<TaskInfo> tasks=getTaskListByLargeClassName(s);
				if(!tasks.isEmpty())
				{
					final Treechildren tc=new Treechildren();//创建之后才有增加打开监听
					item.addEventListener(Events.ON_OPEN, new EventListener(){

						@Override
						public void onEvent(Event arg0) throws Exception {
							Treeitem target=(Treeitem) arg0.getTarget();
							Treecols cols=target.getTree().getTreecols();
							Treecol col=(Treecol) cols.getFirstChild();
							col.setLabel("企业名称——产品大类——产品名称");
							
							if(!target.isOpen())
							{
								col.setLabel("企业名称——产品大类");
							}
							target.getTreechildren().detach();
							target.appendChild(new Treechildren());
							Treeitem citem=null;
							Treerow row=null;
							Treecell cell=null;
							for(TaskInfo t:tasks)
							{
								citem=new Treeitem();
								citem.setId(t.getTaskId());
								row=new Treerow();
								cell=new Treecell(t.getProductName());
								row.appendChild(cell);
								cell=new Treecell(t.getTaskName());
								row.appendChild(cell);
								cell=new Treecell(t.getPreviousUser());
								row.appendChild(cell);
								cell=new Treecell(t.getPreviousDate());
								cell.setSpan(2);
								row.appendChild(cell);
								citem.appendChild(row);
								citem.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener(){

									@Override
									public void onEvent(Event arg0)
											throws Exception {
										Treeitem target=(Treeitem) arg0.getTarget();
										System.out.println("你双击了"+target.getLabel());
									}
									
								});
								
								citem.addEventListener(Events.ON_RIGHT_CLICK, new EventListener(){

									@Override
									public void onEvent(Event arg0)
											throws Exception {
										Treeitem target=(Treeitem) arg0.getTarget();
										Window objWindow=(Window) Executions.createComponents("/SysForm/chargingform.zul", null, null);
										
										objWindow.doModal();
									}
									
								});
								target.getTreechildren().appendChild(citem);
							}
						}
						
					});
					item.appendChild(tc);
				   }
				selitem.getTreechildren().appendChild(item);
			}
		}
			
	}
	/**
	 * 根据大类名称得到所拥有的任务列表
	 * @param ln
	 * @return
	 */
	public List<TaskInfo> getTaskListByLargeClassName(String ln)
	{
		List<TaskInfo> t_list=new ArrayList<TaskInfo>();
		for(TaskInfo t:a_list)
		{
			if(ln.equals(t.getLargeClassName()))
				t_list.add(t);
		}
		return t_list;
	}
	/**
	 * 根据企业名称得到所拥有的大类名称列表
	 * @param en
	 * @return
	 */
	public List<String> getLargeClassNameListByEnterpriseName(String en)
	{
		List<String> p_list=new ArrayList<String>();
		boolean isE=true;
		for(TaskInfo t:a_list)
		{
			if(en.equals(t.getEnterpriseName()))
			{
				isE=false;
				for(String e:p_list)
				{
					if(e.equals(t.getLargeClassName()))
					{
						isE=true;
						break;
					}
				}
				if(!isE)
					p_list.add(t.getLargeClassName());
			}
			
		}
		return p_list;
	}
	/**
	 * 得到企业名称列表
	 * @param t_list
	 * @return
	 */
	public List<String> getEnterpriseList()
	{
		List<String> e_list=new ArrayList<String>();
		for(TaskInfo t:a_list)
		{
			boolean isE=false;
			for(String e:e_list)
			{
				if(e.equals(t.getEnterpriseName()))
				{
					isE=true;
					break;
				}
			}
			if(!isE)
				e_list.add(t.getEnterpriseName());
		}
		return e_list;
	}
	/**
	 * 根据从工作流中得到数据进行处理得到详细任务列表
	 * @return
	 */
	public void InitAllTaskList(List<TaskInstance> t_list)
	{
		TaskInfo taskInfo;
		SimpleDateFormat sdf_s=new SimpleDateFormat("yyyy-MM-dd");
		ApplicationInfo applicationInfo=null;
		ProductCatalogueInfo pro=null;
		for(TaskInstance taskInstance:t_list)
		{
			if(taskInstance.getName().equals("合同制定"))
			{
				taskInfo=new TaskInfo();
				taskInfo.setTaskId(String.valueOf(taskInstance.getId()));
				System.out.print(">-------taskInstanceId---->"+taskInstance.getId());
				JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
				taskMgmtSession = jbpmContext.getTaskMgmtSession();
				TaskInstance ti = jbpmContext.getTaskInstance(taskInstance.getId());
				System.out.println("?="+ti.getId());
				String entityName = "";
				// 产品
				String rowId = "", pname = "", ename = "";
				if (ti.getVariable("rowId") != null)
				{
					rowId = ti.getVariable("rowId").toString();
				}
				if (ti.getVariable("entityName") != null)
				{
					entityName = ti.getVariable("entityName").toString();
				}
				jbpmContext.close();
				
				if (entityName.equals("ApplicationInfo"))
				{
					applicationInfo = ApplicationInfoServiceUtil.getById(rowId);
					
				}else//entityName==AppStatusRecordInfo
				{
					AppStatusRecordInfo appStatusRecordInfo = AppStatusRecordInfoServiceUtil.getById(rowId);
					applicationInfo = appStatusRecordInfo.getApplication();
					
				}
				pname = applicationInfo.getProduction().getProductName();
				pro=applicationInfo.getProduction();
				String lname = null;
				while(pro.getProductParent()!=null)
				{
					lname=pro.getProductParent().getProductName();
					pro=pro.getProductParent();
				}
				ename = applicationInfo.getEnterprise().getName();
				taskInfo.setLargeClassName(lname);
				taskInfo.setEnterpriseName(ename);
				taskInfo.setProductName(pname);
				taskInfo.setTaskName(taskInstance.getName());
				// 前一任务
				Long taskInstanceId = taskInstance.getId();
				TaskInstanceInfo preTaskInstanceInfo = TaskInstanceInfoServiceUtil.getPreTaskInstanceInfo(taskInstanceId);
				taskInfo.setPreviousUser(preTaskInstanceInfo.getActorName());
				taskInfo.setPreviousDate(sdf_s.format(taskInstance.getCreate()));
				a_list.add(taskInfo);
			}
			
		}
	}
	public List<TaskInstance> getTodoTaskList()
	{
		List<TaskInstance> t_list=BpmUtil.getMyTaskList(userId);
		return t_list;
	}
	class TaskInfo{
		private String taskId;
		private String enterpriseName;
		private String productName;
		private String largeClassName;
		public String getTaskId() {
			return taskId;
		}
		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}
		public String getEnterpriseName() {
			return enterpriseName;
		}
		public void setEnterpriseName(String enterpriseName) {
			this.enterpriseName = enterpriseName;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public String getLargeClassName() {
			return largeClassName;
		}
		public void setLargeClassName(String largeClassName) {
			this.largeClassName = largeClassName;
		}
		public String getTaskName() {
			return taskName;
		}
		public void setTaskName(String taskName) {
			this.taskName = taskName;
		}
		public String getPreviousUser() {
			return previousUser;
		}
		public void setPreviousUser(String previousUser) {
			this.previousUser = previousUser;
		}
		public String getPreviousDate() {
			return previousDate;
		}
		public void setPreviousDate(String previousDate) {
			this.previousDate = previousDate;
		}
		private String taskName;
		private String previousUser;
		private String previousDate;
		
	}
}

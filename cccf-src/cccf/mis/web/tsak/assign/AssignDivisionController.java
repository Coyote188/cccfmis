package cccf.mis.web.tsak.assign;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.modellite.spring.BeanAdapter;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import cccf.ma.service.AssignDivisionService;

public class AssignDivisionController
		extends GenericForwardComposer
{
	private Map<?, ?> params = Executions.getCurrent().getArg();
	private Combobox reveiwUser; // 批次安排人员控件对象
	private Listbox	task_list;
	private Textbox	approveMemo;
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5135350317223241087L;
	
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{ 
		// 初始化符合性审查人列表
		List<?> reviewUserList = UserInfoServiceUtil.getByRoleName("批次安排人员");
		page.setAttribute("reviewUserList", reviewUserList);
		return super.doBeforeCompose(page, parent, compInfo);
	}
	
	public void onInited(ForwardEvent event)
	throws InterruptedException
	{ 
		ListModelList model = new ListModelList();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) params.get("values");
		if (list == null)
		{
			task_list.setModel(model);
			return;
		}
		model.addAll(list);
		task_list.setModel(model);
		task_list.selectAll();
	}
	
	@SuppressWarnings("rawtypes")
	private List<Map>	tasks	= new ArrayList<Map>();
	public void onSubmit()
			throws InterruptedException
	{ 
		 
		String reuserId = (String) reveiwUser.getSelectedItem().getValue(); 
		
		//申请
		@SuppressWarnings("unchecked")
		Set<Listitem> seltedItems = task_list.getSelectedItems();
		if (seltedItems.isEmpty())
		{
			Messagebox.show("请选择至少一个申请进行受理分工", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		} else
		{
			for (Listitem item : seltedItems)// 迭代选中的Item
			{
				tasks.add((Map<?, ?>) item.getValue());
			}
			AssignDivisionService svc = BeanAdapter.getBean("AssignDivisionService", AssignDivisionService.class);
			svc.doAssignDivision(tasks, UserInfoServiceUtil.getCurrentLoginUser().getId(), reuserId, "提交", approveMemo.getText()); 
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<Map> model = (List<Map>) task_list.getModel();
			// 保存成功清除选中对象
			model.removeAll(tasks);
			Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
			self.setAttribute("submits", tasks);
			
			if (task_list.getItems().size() == 0)
			{
				((Window) self).setVisible(false);
				
				// 操作成功
				// 
				self.setAttribute("submit", "OK");
				self.setVisible(false);
			}
		}
		 
	}
}

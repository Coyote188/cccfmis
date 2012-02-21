package cccf.mis.web.acceptdivision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.modellite.spring.BeanAdapter;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

import cccf.ma.service.AcceptDivisionService;

/**
 */
public class FormController extends GenericForwardComposer
{

	private static final long serialVersionUID = -3039777696982952204L;

	private Map<Object, Object> args = new HashMap<Object, Object>();
	Map params = Executions.getCurrent().getArg();
	private AcceptDivisionService acceptDivisionService;
	private Combobox reveiwUser; // 符合性审查人员控件对象
	private Listbox tastklist;
	private Vlayout includelayout;
	private Textbox approveMemo;

	@Override
	public void doAfterCompose(Component comp) throws Exception
	{
		super.doAfterCompose(comp);

		if (arg != null)
			args.putAll(arg);
		tastklist = (Listbox) Executions.createComponents(
				"/views/TaskList/ApplyList.zul", includelayout, args);

		ListModelList model = new ListModelList();
		List<Map> list = (List<Map>) params.get("selectedItems");
		if (list == null)
		{
			tastklist.setModel(model);
			return;
		}
		model.addAll(list);
		tastklist.setModel(model);
	}

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo)
	{
		acceptDivisionService = BeanAdapter.getBean("AcceptDivisionService",
				AcceptDivisionService.class);

		// 初始化符合性审查人列表
		List reviewUserList = UserInfoServiceUtil.getByRoleName("符合性审查人员");
		page.setAttribute("reviewUserList", reviewUserList);
		return super.doBeforeCompose(page, parent, compInfo);
	}

	public void onSubmit() throws InterruptedException
	{
		String reuserId = (String) reveiwUser.getSelectedItem().getValue();
		UserInfo user = UserInfoServiceUtil.getCurrentLoginUser();
		Set seltedItems = tastklist.getSelectedItems();
		if (seltedItems.isEmpty())
		{
			Messagebox.show("请选择至少一个申请进行受理分工", "操作提示", Messagebox.OK,
					Messagebox.INFORMATION);
			return;
		}
		else
		{
			List<Map> tasks = new ArrayList<Map>();
			for (Object obj : seltedItems)// 迭代选中的Item
			{
				Listitem item = (Listitem) obj;// 取得选中的Item
				tasks.add((Map) item.getValue());
			}
			acceptDivisionService.doAcceptDivision(tasks, user.getId(),
					reuserId, "符合性审查", approveMemo.getText());

			// 保存成功清除选中对象
			for (Object obj : seltedItems)
			{
				Listitem item = (Listitem) obj;

				tastklist.removeItemFromSelection(item);
			}

			Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK,
					Messagebox.INFORMATION);

			self.setAttribute("result", "OK");

			if (tastklist.getChildren().size() == 0)
			{
				// 关闭窗口

			}
		}
	}

}

package cccf.mis.web.tsak;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

public abstract class AbsTsakController
		extends GenericForwardComposer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8222592291923482133L;
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		this.page = page;
		this.page.setAttribute("itemButlabel", getItemButlabel());
		return super.doBeforeCompose(page, parent, compInfo);
	}

	public abstract String getItemButlabel();
	/**
	 * @param event
	 * @throws InterruptedException
	 */
	public void onDoSelectItems(ForwardEvent event)
			throws InterruptedException
	{
		Listbox tasksbox = getTasksbox();
		@SuppressWarnings("unchecked")
		Set<Listitem> items = tasksbox.getSelectedItems();
		if (items.size() == 0)
		{
			Messagebox.show("请选择要处理的记录", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		List<Map<String, Object>> selectedvalues = new ArrayList<Map<String, Object>>(items.size());
		for (Listitem item : items)
		{
			@SuppressWarnings("unchecked")
			Map<String, Object> value = (Map<String, Object>) item.getValue();
			selectedvalues.add(value);
		}
		doSelectItems(selectedvalues);
	}
	/**
	 * @return 取得任务列表
	 */
	protected abstract Listbox getTasksbox();
	/**
	 * 办理一个任务
	 * 
	 * @param event
	 * @throws InterruptedException
	 * @throws SuspendNotAllowedException
	 */
	@SuppressWarnings("unchecked")
	public void onDoSelectedItem(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
		if (getTasksbox().getSelectedItem() != null)
		{
			doSelectItems((Map<String, Object>) getTasksbox().getSelectedItem().getValue());
		}
	}
	/**
	 * 办理一个任务
	 * 
	 * @param event
	 * @throws InterruptedException
	 * @throws SuspendNotAllowedException
	 */
	@SuppressWarnings("unchecked")
	public void onDoSelfItem(ForwardEvent event)
			throws SuspendNotAllowedException, InterruptedException
	{
		Listitem item = (Listitem) event.getOrigin().getTarget().getParent().getParent();
		item.setSelected(true);
		doSelectItems((Map<String, Object>) item.getValue());
	}
	/**
	 * 显示隐藏列表中的办理按钮
	 * 
	 * @param fig
	 * @param listitems
	 */
	protected void setItemButVisible(Boolean fig, Listitem... listitems)
	{
		@SuppressWarnings("unchecked")
		List<Object> objs = getTasksbox().getChildren();
		if (listitems == null || listitems.length == 0)
		{
			for (Object obj : objs)
			{
				if (obj instanceof Listitem)
					setItemButVisible((Listitem) obj, fig);
			}
		} else
		{
			for (Listitem item : listitems)
				setItemButVisible(item, fig);
		}
	}
	/**
	 * 显示隐藏行中办理按钮
	 * 
	 * @param item
	 * @param fig
	 */
	protected void setItemButVisible(Listitem item, Boolean fig)
	{
		@SuppressWarnings("unchecked")
		List<Listcell> children = item.getChildren();
		children.get(children.size() - 1).getFirstChild().setVisible(fig);
	}
	/**
	 * 执行办理
	 * 
	 * @param item
	 * @throws InterruptedException
	 * @throws SuspendNotAllowedException
	 */
	protected abstract void doSelectItems(Map<String, Object> item)
			throws SuspendNotAllowedException, InterruptedException;
	/**
	 * 执行一组办理
	 * 
	 * @param selectedvalues
	 * @throws InterruptedException
	 * @throws SuspendNotAllowedException
	 */
	protected abstract void doSelectItems(List<Map<String, Object>> selectedvalues)
			throws SuspendNotAllowedException, InterruptedException;
}

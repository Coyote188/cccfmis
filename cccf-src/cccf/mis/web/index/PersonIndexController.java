package cccf.mis.web.index;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.sys.SessionCtrl;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;
import org.zkoss.zul.North;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Splitter;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

public class PersonIndexController
		extends GenericForwardComposer
{
	private static final long	serialVersionUID	= 2147002015265224478L;
	private Menubar				mainmenubar;
	private Timer				bartimer;
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		UserInfo userInfo = UserInfoServiceUtil.getCurrentLoginUser();
		if (userInfo == null)
		{
			Executions.sendRedirect("/login.zul");
			return;
		}
		super.doAfterCompose(comp);
		@SuppressWarnings("unchecked")
		List<Object> menus = mainmenubar.getChildren();
		bindForward(menus);
	}
	private void bindForward(List<Object> menus)
	{
		for (Object obj : menus)
		{
			if (obj instanceof Menuseparator)
				continue;
			if (obj instanceof Menuitem)
			{
				((Menuitem) obj).addForward("onClick", self, "onMenuItemAction");
			} else if (obj instanceof Menu)
			{
				@SuppressWarnings("unchecked")
				List<Object> children = ((Menu) obj).getMenupopup().getChildren();
				bindForward(children);
			}
		}
		bartimer.stop();
	}
	/**
	 * 初始化加载
	 * 
	 * @param event
	 * @throws SQLException
	 */
	public void onInit(ForwardEvent event)
			throws SQLException
	{
		// 3秒关闭TOP DIV
		bartimer.start();
	}
	public void onBartimer(ForwardEvent event)
	{
		System.out.append("onBartimer");
		((Splitter) self.getFellow("topsplitter")).setOpen(false);
		onNorthOpen(event);
		bartimer.stop();
	}
	/**
	 * 映射菜单到左工具栏
	 * 
	 * @param event
	 */
	public void onFalstTree(ForwardEvent event)
	{
		Menupopup menu = (Menupopup) event.getOrigin().getTarget();
		((Caption) self.getFellow("captionmenu")).setLabel(((Menu) menu.getParent()).getLabel());
		Tree falstmenutree = (Tree) self.getFellow("falstmenutree");
		falstmenutree.getTreechildren().getChildren().clear();
		@SuppressWarnings("unchecked")
		List<Object> items = (List<Object>) menu.getChildren();
		bindTree(falstmenutree.getTreechildren(), items);
	}
	/**
	 * 建立 左工具栏菜单树
	 * 
	 * @param children
	 * @param items
	 */
	private void bindTree(Treechildren children, List<Object> items)
	{
		for (Object obj : items)
		{
			if (obj instanceof Menuseparator)
				continue;
			if (obj instanceof Menuitem)
			{
				Menuitem item = (Menuitem) obj;
				Treeitem treeitem = new Treeitem();
				Treerow row = new Treerow();
				Treecell treecell = new Treecell();
				row.appendChild(treecell);
				//
				treecell.setLabel(item.getLabel());
				treeitem.setValue(item.getValue());
				treecell.setImage("/res/icons/page.png");
				//
				Treecell treecell2 = new Treecell();
				if (item.getImage() != null)
					treecell2.setImage(item.getImage());
				row.appendChild(treecell);
				row.appendChild(treecell2);
				treeitem.appendChild(row);
				//
				children.appendChild(treeitem);
				if (item.isDisabled())
					treeitem.setDisabled(true);
				treeitem.addForward("onClick", self, "onMenuItemAction");
			} else if (obj instanceof Menu)
			{
				Menu item = (Menu) obj;
				Treeitem treeitem = new Treeitem();
				Treerow row = new Treerow();
				Treecell treecell = new Treecell();
				row.appendChild(treecell);
				//
				treecell.setLabel(item.getLabel());
				treecell.setImage("/res/icons/folder_page.png");
				//
				Treecell treecell2 = new Treecell();
				if (item.getImage() != null)
					treecell2.setImage(item.getImage());
				row.appendChild(treecell);
				row.appendChild(treecell2);
				treeitem.appendChild(row);
				children.appendChild(treeitem);
				//
				Treechildren c2 = new Treechildren();
				treeitem.setOpen(true);
				treeitem.appendChild(c2);
				children.appendChild(treeitem);
				@SuppressWarnings("unchecked")
				List<Object> items2 = item.getMenupopup().getChildren();
				bindTree(c2, items2);
			} else
			{
				continue;
			}
		}
	}
	/**
	 * 上部区域关闭,调整主菜单位置
	 * 
	 * @param event
	 */
	public void onNorthOpen(ForwardEvent event)
	{
		// Div div = (Div) self.getFellow("menudiv");
		North north = (North) self.getFellow("mainnorth");
		Splitter splitter = (Splitter) self.getFellow("topsplitter");
		if (splitter.isOpen())
		{
			north.setHeight("115px");
		} else
		{
			north.setHeight("35px");
		}
		// Events.sendEvent("onOpen");
	}
	/**
	 * 退出
	 * 
	 * @param event
	 */
	public void onLogout(ForwardEvent event)
	{
		((SessionCtrl) Sessions.getCurrent()).invalidateNow();
		Sessions.getCurrent(true);
		Executions.sendRedirect("/login.zul");
	}
	/**
	 * 打开一个功能
	 * 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	public void onMenuItemAction(ForwardEvent event)
	{
		Component com = event.getOrigin().getTarget();
		String url = "";
		String lab = "";
		Map<String, Object> args = new HashMap<String, Object>();
		if (arg != null)
			args.putAll(arg);
		args.put("url", url);
		args.put("name", lab);
		if (com instanceof Menuitem)
		{
			url = ((Menuitem) com).getValue();
			lab = ((Menuitem) com).getLabel();
		} else if (com instanceof Treeitem)
		{
			url = (String) ((Treeitem) com).getValue();
			lab = ((Treeitem) com).getLabel();
		} else
		{
			throw new WrongValueException(com, "无法处理的菜单类型.");
		}
		if (url == null || url.isEmpty())
			throw new WrongValueException(com, "请设置URL");
		Tabbox box = (Tabbox) self.getFellow("maintabbox");
		Tab tab = getTaskTab(box.getTabs(), lab);
		if (tab == null)
		{
			tab = new Tab();
			tab.setLabel(lab);
			tab.setClosable(true);
			box.getTabs().appendChild(tab);
			Tabpanel tabpanel = new Tabpanel();
			tabpanel.setVflex("true");
			tabpanel.setHflex("true");
			box.getTabpanels().appendChild(tabpanel);
			Div div = (Div) Executions.createComponents("/views/Indexs/items/Progress.zul", tabpanel, args);
			desktop.enableServerPush(true);
			// Executions.setDelay(1, 5, 2);
			new Thread(new WorkingThread(div, tabpanel, url, args)).start();
		}
		tab.setSelected(true);
	}
	private Tab getTaskTab(Tabs tabs, String lab)
	{
		if (tabs == null)
			return null;
		@SuppressWarnings("unchecked")
		List<Tab> list = tabs.getChildren();
		for (Tab tab : list)
		{
			if (lab.equals(tab.getLabel()))
				return tab;
		}
		return null;
	}
	public class OpenWindow
			implements Runnable
	{
		private Map<?, ?>	args;
		private String		url;
		private Window		win;
		private Tabpanel	tab;
		public OpenWindow(String url2, Tabpanel tabpanel, Map<?, ?> args2)
		{
			this.url = url2;
			this.args = args2;
			tab = tabpanel;
		}
		@Override
		public void run()
		{
			try
			{
				Executions.activate(desktop);
				win = (Window) Executions.createComponents(url, null, args);
				win.setVisible(false);
				win.setTitle("");
				win.setVflex("true");
				win.setHflex("true");
				win.setBorder("none");
				win.setParent(tab);
			} catch (Exception e)
			{
				e.printStackTrace();
			} finally
			{
				Executions.deactivate(desktop);
			}
			// win.setParent(tabpanel);
		}
		public Window getRootComponent()
		{
			return win;
		}
	}
	public class WorkingThread
			implements Runnable
	{
		private final Progressmeter	pg;
		private Thread				opening;
		private OpenWindow			win;
		private Div					div;
		public WorkingThread(Div div, Tabpanel tabpanel, String url, Map<?, ?> args)
		{
			this.div = div;
			pg = (Progressmeter) div.getFellow("progInfo");
			win = new OpenWindow(url, tabpanel, args);
			opening = new Thread(win);
		}
		public void run()
		{
			try
			{
				opening.start();
				int i = 2;
				int v = 0;
				while (opening.isAlive())
				{
					Executions.activate(desktop);
					v += (100 - v) / i;
					if (v > 100)
						v = 100;
					pg.setValue(v);
					i++;
					Executions.deactivate(desktop);
				}
				Executions.activate(desktop);
				div.detach();
				win.getRootComponent().setVisible(true);
				Executions.deactivate(desktop);
			} catch (Exception ex)
			{
				ex.printStackTrace();
			} finally
			{
				desktop.enableServerPush(false);
			}
		}
	}
}

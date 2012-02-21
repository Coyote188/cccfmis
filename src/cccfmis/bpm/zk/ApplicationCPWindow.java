package cccfmis.bpm.zk;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import cccf.ma.model.ApplicationInfo;
import com.aidi.bpm.service.BpmUtil;
import com.aidi.bpm.zk.BpmZkUtil;

public class ApplicationCPWindow
		extends Window
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public ApplicationInfo		application;
	Map<?, ?>					params				= Executions.getCurrent().getArg();
	String						userId;
	String						processId			= "0";
	String						rowId;
	String						cdir				= "", attachfilePath = "";
	Long						taskInstanceId;
	String						entityName			= "ApplicationInfo";
	String[]					counterSignUsers;
	DataSource					source				= null;
	public void onCreate()
	{
		application = (ApplicationInfo) this.getPage().getVariable("inspectionApplication");
		rowId = application.getId();
		if (params.get("taskInstanceId") != null)
		{
			taskInstanceId = Long.parseLong(params.get("taskInstanceId").toString());
		}
		userId = params.get("userId").toString();
		Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow("appoveGrid");
		BpmZkUtil.generateApproveGrid(appoveGrid, taskInstanceId);
		source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		Grid grid = (Grid) getFellow("opiniongrid");
		// 加载证书
		try
		{
			JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_certification", " where applyid='" + application.getId() + "'");
			List<Map<String, Object>> list = adapter.load();
			Iterator<Map<String, Object>> listi = list.iterator();
			while (listi.hasNext())
			{
				Map<String, Object> map = listi.next();
				Row row = new Row();
				row.setValue(map.get("id"));
				System.out.println(map.get("id"));
				try
				{
					Label number = new Label(map.get("number").toString());
					Label date = new Label(map.get("certifaicationfdate") + "至" + map.get("certifaicationfdate"));
					Label productcatalogue = new Label(map.get("productcatalogue").toString());
					Label certifaication = new Label("新发证");
					Cell opinion = new Cell();
					Radiogroup rdp = new Radiogroup();
					Radio ty = new Radio("同意");
					ty.setChecked(true);
					Radio bty = new Radio("不同意");
					rdp.appendChild(ty);
					rdp.appendChild(bty);
					opinion.appendChild(rdp);
					row.appendChild(number);
					row.appendChild(productcatalogue);
					row.appendChild(date);
					row.appendChild(new Label((String) map.get("technicalRequirement")));
					row.appendChild(new Label((String) map.get("observedStandard")));
					row.appendChild(certifaication);
					row.appendChild(opinion);
					grid.getRows().appendChild(row);
				} catch (Exception e)
				{
					System.out.println(e.getMessage());
				}
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Cancel this screen
	 */
	public void onCancel()
	{
		this.detach();
	}
	public void onSave() throws SQLException
	{
		//
		Textbox tb = (Textbox) getFellow("disc");
		Grid grid = (Grid) getFellow("opiniongrid");
		Iterator<Row> rows = grid.getRows().getChildren().iterator();
		Session session = getDesktop().getSession();
		JEntityAdapter adapter;
		adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_opinion", "uid=? and aid=?");
		adapter.removes(adapter.load((String) session.getAttribute("userid"), application.getId()));
		while (rows.hasNext())
		{
			Row prow = rows.next();
			Map<String, Object> opinion;
			opinion = new HashMap<String, Object>();
			opinion.put("id", null);
			opinion.put("cid", prow.getValue());
			opinion.put("uid", getDesktop().getSession().getAttribute("userid"));
			opinion.put("uname", getDesktop().getSession().getAttribute("name"));
			opinion.put("aid", application.getId());
			Cell cell = (Cell) prow.getChildren().get(6);
			Radiogroup radiogroup = (Radiogroup) cell.getChildren().get(0);
			Radio ty = (Radio) radiogroup.getChildren().get(0);
			Radio bty = (Radio) radiogroup.getChildren().get(1);
			if (ty.isChecked())
				opinion.put("opinion", 1);
			else if (bty.isChecked())
				opinion.put("opinion", 0);
			opinion.put("disc", tb.getText());
			adapter.commit(opinion);
		}
	}
	public void onSubmit() throws SQLException
	{
		if (application.getStauts0() == 7)
		{
			try
			{
				Messagebox.show("企业已提交变更申请该流程处于挂起状态!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (taskInstanceId > 0)
		{
			Grid appoveGrid = (Grid) this.getSpaceOwner().getFellow("appoveGrid");
			BpmZkUtil.saveApprove(appoveGrid, taskInstanceId, userId, rowId, entityName);
			List<Row> approveRows = (List<Row>) appoveGrid.getRows().getChildren();
			Row row = approveRows.get(0);
			Listbox resultListbox = (Listbox) row.getFellow("resultListbox");
			String approveResult = null;
			if (resultListbox.getSelectedItem() != null)
			{
				approveResult = resultListbox.getSelectedItem().getLabel();
				onSave();
				// 结束tasktanc
				BpmUtil.endTaskPreviousActor(taskInstanceId, approveResult);
			}
			try
			{
				this.detach();
				Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
				EventQueues.lookup(userId + "refreshEndTaskListEvent", EventQueues.APPLICATION, true).publish(new Event("onMsgEventQueue", null, ""));
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
		{
			try
			{
				Messagebox.show("任务结点绑定错误!");
			} catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}

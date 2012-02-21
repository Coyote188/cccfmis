package cccfmis.bpm.zk;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.spi.JAdapterFactry;
import net.modellite.spring.BeanAdapter;
import openjframework.service.*;
import openjframework.util.ZkFileUtil;
import openjframework.bpm.model.TaskInstanceInfo;
import openjframework.model.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import com.aidi.bpm.model.Approve;
import com.aidi.bpm.service.*;
import com.aidi.bpm.zk.BpmZkUtil;
import com.aidi.core.spring.*;
import com.aidi.core.zk.*;
import cccf.ma.service.*;
import cccf.ma.model.*;
import cccf.ma.bpm.*;

public class ApplictionPdrwfpWindow
		extends Window
{
	public ApplicationInfo	application;
	Map						params				= Executions.getCurrent().getArg();
	String					userId;
	String					processId			= "0";
	String					rowId;
	String					cdir				= "", attachfilePath = "";
	Long					taskInstanceId;
	String					entityName			= "ApplicationInfo";
	String[]				counterSignUsers	= null;
	String					rePortUserId		= "";
	public void onCreate()
			throws SQLException, ParseException
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
		DataSource source = BeanAdapter.getBean("nativeDataSource", DataSource.class);
		JEntityAdapter adapter = JAdapterFactry.createJEntityAdapterByTable(source, "_certification", "applyid=?");
		List<Map<String, Object>> list = adapter.load(application.getId());
		// System.out.println(application.getId());
		System.out.println(list.size());
		System.out.println(application.getEnterprise().getName());
		Listbox box = (Listbox) this.getFellow("_certifications");
		//
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (Map<String, Object> map : list)
		{
			Listitem item = new Listitem();
			Listcell cell = new Listcell((String) map.get("number"));
			cell.setParent(item);
			Listcell cell1 = new Listcell(df.format(df.parse((String) map.get("certifaicationfdate"))));
			cell1.setParent(item);
			Listcell cell2 = new Listcell(df.format(df.parse((String) map.get("certifaicationldate"))));
			cell2.setParent(item);
			Listcell cell3 = new Listcell((String) map.get("productcatalogue"));
			cell3.setParent(item);
			Listcell cell5 = new Listcell((String) map.get("technicalRequirement"));
			cell5.setParent(item);
			Listcell cell6 = new Listcell((String) map.get("observedStandard"));
			cell6.setParent(item);
			Listcell cell4 = new Listcell("新发证");
			cell4.setParent(item);
			item.setParent(box);
		}
		// <listitem self="@{each=dataList}">
		// <listcell label="@{dataList.number}"></listcell>
		// <listcell label="@{dataList.certifaicationfdate}"></listcell>
		// /<listcell label="@{dataList.certifaicationldate}"></listcell>
		// <listcell label="@{dataList.productcatalogue}"></listcell>
		// <listcell label="@{dataList.appname}"></listcell>
		// </listitem>
		// this.getPage().setAttribute("zslist", list);
	}
	/**
	 * Cancel this screen
	 */
	public void onCancel()
	{
		this.detach();
	}
	public void onNext()
	{
		// 向桌面发送消息以刷新endtasklist
		EventQueues.lookup(userId + "nextListEvent", EventQueues.APPLICATION, true).publish(new Event("onMsgEventQueue", null, ""));
	}
	public void onSave()
	{
		ApplicationInfoServiceUtil.saveOrUpdate(application);
	}
	public void onSubmit()
			throws InterruptedException
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
				approveResult = resultListbox.getSelectedItem().getLabel();
			System.out.println(approveResult);
			// 结束tasktanc
			if (approveResult.equals("结论上报") || approveResult.equals("上报结论"))
			{
				Bandbox bdReportUserList = (Bandbox) this.getFellow("bdReportUserList");
				System.out.println(bdReportUserList);
				System.out.println(counterSignUsers);
				if (counterSignUsers == null || counterSignUsers.length != 3)
				{
					// Messagebox.show("评定人员必须是三个.", "提示", Messagebox.OK, Messagebox.INFORMATION);
					throw new WrongValueException(bdReportUserList, "评定人员必须是三个.");
				} else
				{
					System.out.println(counterSignUsers[0] + counterSignUsers[1] + counterSignUsers[2]);
					application.setReportUser(rePortUserId);
					onSave();
					BpmUtil.endTaskToCountSign(taskInstanceId, approveResult, counterSignUsers);
					// BpmUtil.endTaskToCountSign(taskInstanceId, approveResult, counterSignUsers);
				}
			} else
			{
				BpmUtil.endTaskPreviousActor(taskInstanceId, approveResult);
			}
			try
			{
				// /编号
				this.detach();
				Messagebox.show("数据成功提交!", "操作提示", Messagebox.OK, Messagebox.INFORMATION);
				// 向桌面发送消息以刷新endtasklist
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
	public void onReportUserSelect()
	{
		Listbox lbxReportUserList = (Listbox) this.getFellow("lbxReportUserList");
		Bandbox bdReportUserList = (Bandbox) this.getFellow("bdReportUserList");
		Set<Listitem> list = lbxReportUserList.getSelectedItems();
		String uName = "";
		rePortUserId = "";
		if (list.size() > 0)
		{
			counterSignUsers = new String[list.size()];
			int i = 0;
			for (Listitem item : list)
			{
				UserInfo user = (UserInfo) item.getValue();
				if (user != null && user.getId() != null)
				{
					uName = user.getName() + "," + uName;
					counterSignUsers[i] = user.getId();
					rePortUserId = user.getId() + "," + rePortUserId;
					i++;
				}
			}
			bdReportUserList.setText(uName);
		} else
		{
			counterSignUsers = null;
			bdReportUserList.setText(null);
		}
	}
}

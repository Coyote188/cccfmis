package cccf.mis.web.tsak.batch.sure;

import java.sql.SQLException;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import cccf.ma.model.AssignBatch; 
import cccf.ma.model.FactoryCheckUser;
import cccf.ma.service.AssignBatchService;

public class BatchSureController
		extends GenericForwardComposer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3193902771109473044L;
	private Map<?, ?>			params				= Executions.getCurrent().getArg();
	private Listbox				task_list;
	private Listbox				participantlist;
	private Popup				popup;
	private Textbox				approveMemo;
	private Textbox				batch_name;
	private AssignBatchService	svc					= BeanAdapter.getBean("AssignBatchService", AssignBatchService.class);
	private Map					selectItemValue;																			// 选中申请值
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		// TODO 生成一个批次
		return super.doBeforeCompose(page, parent, compInfo);
	}
	public void onInited(ForwardEvent event)
			throws InterruptedException
	{
		// TODO 回显数据
		ListModelList mmm = new ListModelList();
		// TODO 添加选择人员
		// mmm.add('')
		StringBuffer hql = new StringBuffer("select new map(id as id").append(",name as name").append(",organization as organization").append(",sex as sex").append(",qualification as qualification").append(",telephone as telephone")
				.append(",adress as adress").append(",station as station").append(",nature as nature").append(")").append(" from FactoryCheckUser o");
		List fcu_list = svc.getResultList(hql.toString());
		mmm.addAll(fcu_list);
		participantlist.setModel(mmm);
	}
	public void onSubmit()
			throws InterruptedException
	{
		Map selectItem = (Map) params.get("apply");
		String batchNo = (String) selectItem.get("batch_no");
		String approveResult = "确认";
		svc.doCheck(batchNo, 3, UserInfoServiceUtil.getCurrentLoginUser().getId(), approveResult, approveMemo.getText());
		
		((Window) self).setVisible(false);
		self.setAttribute("submit", "OK");
		self.setVisible(false);
	}
	public void onSaveParticipants(ForwardEvent event)
	{
		 
		// TODO 保存组员 uss
		popup.close();
	}
	public void onSelectParticipants(ForwardEvent event)
			throws SQLException
	{
		Listitem lsitem = ((Listitem) event.getOrigin().getTarget().getParent().getParent());
		popup.open(lsitem);
		selectItemValue = (Map) lsitem.getValue();
		 
	}
}

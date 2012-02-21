package cccf.mis.web.tsak.factory.datasend;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public class DataSendMgController
		extends GenericForwardComposer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6317329649888772037L;
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		return super.doBeforeCompose(page, parent, compInfo);
	}
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
	}
	public void onAddType(ForwardEvent event)
	{}
	public void onAddNode(ForwardEvent event)
	{}
}

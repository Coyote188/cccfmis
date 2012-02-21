package net.modellite.rim.zk.composer;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public abstract class NativeForwardComposer
		extends GenericForwardComposer
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8515422903658220632L;
	protected Component			comp;
	@Override
	public void doAfterCompose(Component comp)
			throws Exception
	{
		super.doAfterCompose(comp);
	}
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
	{
		return super.doBeforeCompose(page, parent, compInfo);
	}
}

package net.modellite.jdbcp;

public interface JEntityHandler
{
	public boolean accept(JEntityHandlerEvent event);

	public void inserted(JEntityHandlerEvent event);

	public void loaded(JEntityHandlerEvent event);

	public void removed(JEntityHandlerEvent event);

	public void updated(JEntityHandlerEvent event);
}

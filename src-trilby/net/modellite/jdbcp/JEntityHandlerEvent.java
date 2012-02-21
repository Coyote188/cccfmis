package net.modellite.jdbcp;

import java.util.Map;

public class JEntityHandlerEvent
{
	public JEntityHandlerEvent(Map<String, Object> entity)
	{
		this.entity = entity;
	}

	protected transient Map<String, Object>	entity;

	public Map<String, Object> getEntity()
	{
		return entity;
	}
}

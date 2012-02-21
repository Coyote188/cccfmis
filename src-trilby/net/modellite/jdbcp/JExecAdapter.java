package net.modellite.jdbcp;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface JExecAdapter
{
	public boolean execute(Object... args)
		throws SQLException;

	public List<Map<String, Object>> getChain()
		throws SQLException;

	public List<Map<String, Object>> getChain(int i)
		throws SQLException;

	public int getChainCount();

	public int getUpdateCount();
}

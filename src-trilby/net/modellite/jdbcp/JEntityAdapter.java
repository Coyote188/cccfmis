package net.modellite.jdbcp;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface JEntityAdapter
	extends Serializable
{
	public void appedHandler(JEntityHandler handler);

	public void clearHandler();

	public Map<String, Object> commit(Map<String, Object> arg)
		throws SQLException;

	public List<Map<String, Object>> commits(List<Map<String, Object>> args)
		throws SQLException;

	public Map<String, Object> create(Map<String, Object> arg)
		throws SQLException;

	public List<Map<String, Object>> creates(List<Map<String, Object>> args)
		throws SQLException;

	public List<Map<String, Object>> load(Serializable... args)
		throws SQLException;

	public Map<String, Object> loadByPrimaryKey(Serializable... args)
		throws SQLException;

	public List<Map<String, Object>> loadFragment(int start, int max, Serializable... args)
		throws SQLException;

	public Map<String, Object> newEntity();

	public Map<String, Object> remove(Map<String, Object> arg)
		throws SQLException;

	public Map<String, Object> removeByPrimaryKey(Serializable... args)
		throws SQLException;

	public void removeHandler(JEntityHandler handler);

	public List<Map<String, Object>> removes(List<Map<String, Object>> args)
		throws SQLException;

	public Map<String, Object> update(Map<String, Object> arg)
		throws SQLException;

	public List<Map<String, Object>> updates(List<Map<String, Object>> args)
		throws SQLException;
}

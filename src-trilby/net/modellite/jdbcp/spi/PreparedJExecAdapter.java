package net.modellite.jdbcp.spi;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import net.modellite.jdbcp.JExecAdapter;
import net.modellite.jdbcp.JWrapDriver;

public abstract class PreparedJExecAdapter
		implements JExecAdapter
{
	public PreparedJExecAdapter(String sql)
	{
		this.sql = sql;
	}
	private String								sql;
	private List<Vector<Map<String, Object>>>	chains		= new Vector<Vector<Map<String, Object>>>();
	private int									updateCount	= 0;
	private void addChain(ResultSet resultSet)
			throws SQLException
	{
		ResultSetMetaData met = resultSet.getMetaData();
		int conut = met.getColumnCount();
		Vector<Map<String, Object>> chain = new Vector<Map<String, Object>>();
		while (resultSet.next())
		{
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= conut; i++)
			{
				String lab = met.getColumnLabel(i);
				map.put(lab, resultSet.getObject(lab));
			}
			chain.add(map);
		}
		chains.add(chain);
	}
	@Override
	public boolean execute(Object... args)
			throws SQLException
	{
		Connection conn = getConnection();
		try
		{
			chains.clear();
			conn.setAutoCommit(false);
			PreparedStatement prep = this.prepareStatement(conn, sql);
			ParameterMetaData met = prep.getParameterMetaData();
			int c = met.getParameterCount();
			for (int i = 0; i < c;)
			{
				if (met.getParameterMode(i + 1) == ParameterMetaData.parameterModeOut)
					continue;
				if (args[i] == null)
				{
					prep.setNull(i + 1, met.getParameterType(i + 1));
				} else
				{
					try
					{
						Class<?> jdbcclz = Class.forName(met.getParameterClassName(i + 1));
						Class<?> laclclz = args[i].getClass();
						JWrapDriver driver = JWrapDriverManager.getJWrapper(laclclz, jdbcclz);
						if (driver != null)
							prep.setObject(i + 1, driver.wrap(args[i], jdbcclz));
						else
							prep.setObject(i + 1, args[i]);
					} catch (Exception e)
					{}
				}
				i++;
			}
			boolean flg = prep.execute();
			updateCount = prep.getUpdateCount();
			if (flg)
				addChain(prep.getResultSet());
			while (prep.getMoreResults())
				addChain(prep.getResultSet());
			conn.commit();
			conn.close();
			return flg;
		} finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	@Override
	public List<Map<String, Object>> getChain()
			throws SQLException
	{
		if (chains.size() > 0)
			return chains.get(0);
		return null;
	}
	@Override
	public List<Map<String, Object>> getChain(int i)
			throws SQLException
	{
		if (chains.size() > i)
			return chains.get(i);
		return null;
	}
	@Override
	public int getChainCount()
	{
		return chains.size();
	}
	protected abstract Connection getConnection()
			throws SQLException;
	@Override
	public int getUpdateCount()
	{
		return this.updateCount;
	}
	protected abstract PreparedStatement prepareStatement(Connection conn, String sql)
			throws SQLException;
}

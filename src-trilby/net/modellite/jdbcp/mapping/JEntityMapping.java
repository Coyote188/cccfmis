package net.modellite.jdbcp.mapping;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class JEntityMapping
{
	public static JEntityMapping parseMapping(ResultSetMetaData resmet)
			throws SQLException
	{
		JEntityMapping mapping = new JEntityMapping();
		int count = resmet.getColumnCount();
		for (int column = 1; column <= count; column++)
		{
			String tab = resmet.getTableName(column);
			if (mapping.table == null)
				mapping.table = tab;
			if (!mapping.table.equals(tab))
				mapping.readonly = true;
			String lab = resmet.getColumnLabel(column);
			String type = resmet.getColumnClassName(column);
			boolean auto = resmet.isAutoIncrement(column);
			mapping.addColumn(new Column(lab, type, auto));
		}
		return mapping;
	}
	protected String				table;
	private boolean					readonly	= false;
	protected Vector<String>		pks			= new Vector<String>();
	protected Map<String, Column>	columns		= new HashMap<String, Column>();
	private void addColumn(Column column)
	{
		if (columns.containsKey(column.getLab()))
			return;
		columns.put(column.getLab(), column);
	}
	public void addPk(String string)
	{
		if (!this.pks.contains(string))
			this.pks.add(string);
	}
	public boolean containsKey(String key)
	{
		return columns.containsKey(key);
	}
	public String getTable()
	{
		return this.table;
	}
	public Class<?> getType(String key)
	{
		return columns.get(key).getType();
	}
	public boolean isAuto(String key)
	{
		return columns.get(key).isAuto();
	}
	public boolean isRedonly()
	{
		return readonly;
	}
	public Iterable<String> lableIterator()
	{
		return columns.keySet();
	}
	public Iterable<String> primaryKeyIterator()
	{
		return pks;
	}
	protected static class Column
	{
		Column(String lab, String type, boolean auto)
		{
			this.lab = lab;
			this.type = type;
			this.auto = auto;
		}
		private String	lab;
		private String	type;
		private boolean	auto;
		public String getLab()
		{
			return lab;
		}
		public Class<?> getType()
		{
			try
			{
				return Class.forName(type);
			} catch (ClassNotFoundException e)
			{
				return null;
			}
		}
		public boolean isAuto()
		{
			return auto;
		}
	}
}

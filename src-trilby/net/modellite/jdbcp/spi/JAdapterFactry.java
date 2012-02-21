package net.modellite.jdbcp.spi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.JExecAdapter;
import net.modellite.jdbcp.mapping.JEntityMapping;

public class JAdapterFactry
{
	public static JExecAdapter createCallableJExecAdapter(DataSource source, String sql)
		throws SQLException
	{
		class temp
			extends PreparedJExecAdapter
		{
			public temp(DataSource source, String sql)
			{
				super(sql);
				this.source = source;
			}

			private DataSource	source;

			@Override
			protected Connection getConnection()
				throws SQLException
			{
				return source.getConnection();
			}

			@Override
			protected PreparedStatement prepareStatement(Connection conn, String sql)
				throws SQLException
			{
				return conn.prepareCall(sql);
			}
		}
		return new temp(source, sql);
	}

	public static JEntityAdapter createJEntityAdapterBySQL(DataSource source, String sql)
		throws SQLException
	{
		TableAdapter adapter = new TableAdapter(source, sql);
		JEntityMapping mapping = initMappingBySQL(source.getConnection(), sql);
		adapter.setMapping(mapping);
		if (!mapping.isRedonly()) mapping = initPk(source.getConnection(), mapping.getTable());
		return adapter;
	}

	public static JEntityAdapter createJEntityAdapterBySQL(DataSource source, String sql, List<String> pks)
		throws SQLException
	{
		TableAdapter adapter = new TableAdapter(source, sql);
		adapter.setMapping(initMappingBySQL(source.getConnection(), sql));
		for (String pk : pks)
			adapter.maping.addPk(pk);
		return adapter;
	}

	public static JEntityAdapter createJEntityAdapterBySQL(DataSource source, String catalog, String sql)
		throws SQLException
	{
		TableAdapter adapter = new TableAdapter(source, catalog, sql);
		JEntityMapping mapping = initMappingBySQL(source.getConnection(), sql);
		if (!mapping.isRedonly()) mapping = initPk(source.getConnection(), mapping.getTable());
		adapter.setMapping(mapping);
		return adapter;
	}

	private static JEntityMapping initPk(Connection conn, String table)
		throws SQLException
	{
		try
		{
			JEntityMapping maping = new JEntityMapping();
			PreparedStatement prep = conn.prepareStatement("select * from " + table + " where 1!=1");
			maping = JEntityMapping.parseMapping(prep.executeQuery().getMetaData());
			ResultSet res = conn.getMetaData().getPrimaryKeys(conn.getCatalog(), null, table);
			while (res.next())
				maping.addPk(res.getString("COLUMN_NAME"));
			return maping;
		} finally
		{
			if (conn != null && !conn.isClosed()) conn.close();
		}
	}

	public static JEntityAdapter createJEntityAdapterBySQL(DataSource source, String catalog, String sql, List<String> pks)
		throws SQLException
	{
		TableAdapter adapter = new TableAdapter(source, catalog, sql);
		adapter.setMapping(initMappingBySQL(source.getConnection(), sql));
		for (String pk : pks)
			adapter.maping.addPk(pk);
		return adapter;
	}

	public static JEntityAdapter createJEntityAdapterByTable(DataSource source, String table)
		throws SQLException
	{
		String sql = "select * from " + table;
		TableAdapter adapter = new TableAdapter(source, sql);
		adapter.setMapping(initMappingByTable(source.getConnection(), table));
		return adapter;
	}

	public static JEntityAdapter createJEntityAdapterByTable(DataSource source, String table, String where)
		throws SQLException
	{
		String sql = "select * from " + table;
		if (where != null && !(where = where.trim()).isEmpty()) sql += where.startsWith("where") ? " " + where : " where " + where;
		TableAdapter adapter = new TableAdapter(source, sql);
		adapter.setMapping(initMappingByTable(source.getConnection(), table));
		return adapter;
	}

	public static JEntityAdapter createJEntityAdapterByTable(DataSource source, String catalog, String table, String where)
		throws SQLException
	{
		String sql = "select * from " + table;
		if (where != null && !(where = where.trim()).isEmpty()) sql += where.startsWith("where") ? " " + where : " where " + where;
		TableAdapter adapter = new TableAdapter(source, catalog, sql);
		adapter.setMapping(initMappingByTable(source.getConnection(), table));
		return adapter;
	}

	public static JExecAdapter createPreparedJExecAdapter(DataSource source, String table, String where)
		throws SQLException
	{
		class temp
			extends PreparedJExecAdapter
		{
			public temp(DataSource source, String sql)
			{
				super(sql);
				this.source = source;
			}

			private DataSource	source;

			@Override
			protected Connection getConnection()
				throws SQLException
			{
				return source.getConnection();
			}

			@Override
			protected PreparedStatement prepareStatement(Connection conn, String sql)
				throws SQLException
			{
				return conn.prepareStatement(sql);
			}
		}
		return new temp(source, table);
	}

	private static JEntityMapping initMappingBySQL(Connection conn, String sqlstr)
		throws SQLException
	{
		try
		{
			JEntityMapping maping = new JEntityMapping();
			PreparedStatement prep = conn.prepareStatement(sqlstr);
			maping = JEntityMapping.parseMapping(prep.executeQuery().getMetaData());
			return maping;
		} finally
		{
			if (conn != null && !conn.isClosed()) conn.close();
		}
	};

	private static JEntityMapping initMappingByTable(Connection conn, String table)
		throws SQLException
	{
		try
		{
			JEntityMapping maping = new JEntityMapping();
			PreparedStatement prep = conn.prepareStatement("select * from " + table + " where 1!=1");
			maping = JEntityMapping.parseMapping(prep.executeQuery().getMetaData());
			ResultSet res = conn.getMetaData().getPrimaryKeys(conn.getCatalog(), null, table);
			while (res.next())
				maping.addPk(res.getString("COLUMN_NAME"));
			return maping;
		} finally
		{
			if (conn != null && !conn.isClosed()) conn.close();
		}
	}

	private static class TableAdapter
		extends PreparedJEntityAdapter
	{
		private static final long	serialVersionUID	= -6890176107968594887L;

		protected TableAdapter(DataSource source, String sql)
			throws SQLException
		{
			this(source, null, sql);
		}

		protected TableAdapter(DataSource source, String catalog, String sql)
			throws SQLException
		{
			this.source = source;
			this.catalog = catalog;
			this.select = sql;
		}

		private DataSource		source;
		private String			catalog;
		private String			select;
		private JEntityMapping	maping;

		@Override
		protected Connection getConnection()
			throws SQLException
		{
			Connection conn = source.getConnection();
			if (catalog != null) conn.setCatalog(catalog);
			return conn;
		}

		@Override
		protected JEntityMapping getMaping()
		{
			return this.maping;
		}

		protected String getPrimaryKeySelectString()
			throws SQLException
		{
			JEntityMapping maping = getMaping();
			if (maping.isRedonly()) throw new SQLException("不能对只读实体进行写操作.");
			String where = "";
			for (String pk : maping.primaryKeyIterator())
				where += " AND " + pk + "=?";
			if (where.isEmpty())
			{
				for (String key : maping.lableIterator())
					where += " AND " + key + "=?";
			}
			if (where.isEmpty()) throw new SQLException("不能对空实体建立查询语句.");
			where = where.replaceFirst(" AND", "");
			return "select * from " + maping.getTable() + " where " + where;
		}

		@Override
		protected String getSelectString()
		{
			return select;
		}

		protected void setMapping(JEntityMapping mapping)
		{
			this.maping = mapping;
		}
	}
}

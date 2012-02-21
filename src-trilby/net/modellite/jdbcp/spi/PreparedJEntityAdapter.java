package net.modellite.jdbcp.spi;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import net.modellite.jdbcp.JEntityAdapter;
import net.modellite.jdbcp.JEntityHandler;
import net.modellite.jdbcp.JEntityHandlerEvent;
import net.modellite.jdbcp.JWrapDriver;
import net.modellite.jdbcp.mapping.JEntityMapping;

public abstract class PreparedJEntityAdapter
		implements JEntityAdapter
{
	/**
	 * 
	 */
	private static final long		serialVersionUID	= -8465637180385787442L;
	private Vector<JEntityHandler>	handlers;
	{
		handlers = new Vector<JEntityHandler>();
	}
	@Override
	public void appedHandler(JEntityHandler handler)
	{
		synchronized (handlers)
		{
			if (!handlers.contains(handler))
				handlers.add(handler);
		}
	}
	@Override
	public void clearHandler()
	{
		synchronized (handlers)
		{
			handlers.clear();
		}
	}
	@Override
	public synchronized Map<String, Object> commit(Map<String, Object> arg)
			throws SQLException
	{
		JEntityMapping maping = getMaping();
		for (String key : maping.primaryKeyIterator())
			if (!arg.containsKey(key) && !maping.isAuto(key))
				throw new SQLException("传入的实体没有主建[" + key + "]");
		Connection conn = this.getConnection();
		try
		{
			conn.setAutoCommit(false);
			PreparedStatement prep = conn.prepareStatement(getPrimaryKeySelectString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			int i = 1;
			for (String key : maping.primaryKeyIterator())
				prep.setObject(i++, arg.get("$Original$" + key + "$"));
			ResultSet res = prep.executeQuery();
			boolean flg = res.next();
			if (!flg)
				res.moveToInsertRow();
			for (String key : maping.lableIterator())
			{
				if (!arg.containsKey(key))
					continue;
				if (arg.get(key) == null)
					res.updateNull(key);
				else
					res.updateObject(key, arg.get(key));
			}
			if (flg)
			{
				res.updateRow();
			} else
			{
				res.insertRow();
				res.last();
			}
			conn.commit();
			res.refreshRow();
			arg.clear();
			for (String lable : maping.lableIterator())
			{
				System.out.println(lable + ":" + res.getObject(lable));
				arg.put(lable, res.getObject(lable));
			}
			JEntityHandlerEvent event = new JEntityHandlerEvent(arg);
			doHandler(flg ? "insert" : "update", event);
			return arg;
		} finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	@Override
	public List<Map<String, Object>> commits(List<Map<String, Object>> args)
			throws SQLException
	{
		JEntityMapping maping = getMaping();
		Connection conn = this.getConnection();
		try
		{
			conn.setAutoCommit(false);
			PreparedStatement prep = conn.prepareStatement(getPrimaryKeySelectString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			for (Map<String, Object> arg : args)
			{
				for (String key : maping.primaryKeyIterator())
					if (!arg.containsKey(key) && !maping.isAuto(key))
						throw new SQLException("传入的实体没有主建[" + key + "]");
				prep.clearParameters();
				int i = 1;
				for (String key : maping.primaryKeyIterator())
					prep.setObject(i++, arg.get("$Original$" + key + "$"));
				ResultSet res = prep.executeQuery();
				boolean flg = res.next();
				if (!flg)
					res.moveToInsertRow();
				for (String key : maping.lableIterator())
				{
					if (!arg.containsKey(key))
						continue;
					if (arg.get(key) == null)
						res.updateNull(key);
					else
						res.updateObject(key, arg.get(key));
				}
				if (!flg)
				{
					res.insertRow();
					res.last();
				} else
					res.updateRow();
				res.refreshRow();
				arg.clear();
				for (String lable : maping.lableIterator())
					arg.put(lable, res.getObject(lable));
				res.close();
				JEntityHandlerEvent event = new JEntityHandlerEvent(arg);
				doHandler(flg ? "insert" : "update", event);
			}
			conn.commit();
			return args;
		} finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	@Override
	public synchronized Map<String, Object> create(Map<String, Object> arg)
			throws SQLException
	{
		JEntityMapping maping = getMaping();
		for (String key : maping.primaryKeyIterator())
			if (!arg.containsKey(key) && !maping.isAuto(key))
				throw new SQLException("传入的实体没有主建[" + key + "]");
		Connection conn = this.getConnection();
		try
		{
			conn.setAutoCommit(false);
			PreparedStatement prep = conn.prepareStatement(getPrimaryKeySelectString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			int i = 1;
			for (String key : maping.primaryKeyIterator())
				prep.setObject(i++, arg.get("$Original$" + key + "$"));
			ResultSet res = prep.executeQuery();
			if (res.next())
				throw new SQLException("实休已经在库中存在");
			res.moveToInsertRow();
			for (String key : maping.lableIterator())
			{
				if (!arg.containsKey(key))
					continue;
				if (arg.get(key) == null)
					res.updateNull(key);
				else
					res.updateObject(key, arg.get(key));
			}
			res.insertRow();
			res.last();
			res.refreshRow();
			conn.commit();
			arg.clear();
			for (String lable : maping.lableIterator())
				arg.put(lable, res.getObject(lable));
			JEntityHandlerEvent event = new JEntityHandlerEvent(arg);
			doHandler("insert", event);
			return arg;
		} finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	@Override
	public synchronized List<Map<String, Object>> creates(List<Map<String, Object>> args)
			throws SQLException
	{
		JEntityMapping maping = getMaping();
		Connection conn = this.getConnection();
		try
		{
			conn.setAutoCommit(false);
			PreparedStatement prep = conn.prepareStatement(getPrimaryKeySelectString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			for (Map<String, Object> arg : args)
			{
				for (String key : maping.primaryKeyIterator())
					if (!arg.containsKey(key) && !maping.isAuto(key))
						throw new SQLException("传入的实体没有主建[" + key + "]");
				prep.clearParameters();
				int i = 1;
				for (String key : maping.primaryKeyIterator())
					prep.setObject(i++, arg.get("$Original$" + key + "$"));
				ResultSet res = prep.executeQuery();
				if (res.next())
					throw new SQLException("实休已经在库中存在");
				for (String key : maping.lableIterator())
				{
					if (!arg.containsKey(key))
						continue;
					if (arg.get(key) == null)
						res.updateNull(key);
					else
						res.updateObject(key, arg.get(key));
				}
				res.insertRow();
				res.last();
				res.refreshRow();
				arg.clear();
				for (String lable : maping.lableIterator())
					arg.put(lable, res.getObject(lable));
				res.close();
				JEntityHandlerEvent event = new JEntityHandlerEvent(arg);
				doHandler("insert", event);
			}
			conn.commit();
			return args;
		} finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	private void doHandler(String type, JEntityHandlerEvent event)
	{
		if (type.equals("insert"))
		{
			for (JEntityHandler handler : handlers)
				handler.inserted(event);
		} else if (type.equals("update"))
		{
			for (JEntityHandler handler : handlers)
				handler.updated(event);
		} else if (type.equals("loaded"))
		{
			for (JEntityHandler handler : handlers)
				handler.loaded(event);
		} else
		{
			for (JEntityHandler handler : handlers)
				handler.removed(event);
		}
	}
	protected abstract Connection getConnection()
			throws SQLException;
	protected abstract JEntityMapping getMaping();
	protected abstract String getPrimaryKeySelectString()
			throws SQLException;
	protected abstract String getSelectString();
	@Override
	public synchronized JEntityChain load(Serializable... args)
			throws SQLException
	{
		Connection conn = this.getConnection();
		try
		{
			JEntityMapping maping = getMaping();
			PreparedStatement prep = conn.prepareStatement(getSelectString());
			setParameter(prep, args);
			ResultSet res = prep.executeQuery();
			JEntityChain ress = new JEntityChain();
			int l = 0;
			while (res.next())
			{
				SmipleJEntity entity = new SmipleJEntity();
				for (String lable : maping.lableIterator())
					entity.put(lable, res.getObject(lable));
				ress.add(entity);
				l++;
				JEntityHandlerEvent event = new JEntityHandlerEvent(entity);
				doHandler("loaded", event);
			}
			return ress;
		} finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	@Override
	public Map<String, Object> loadByPrimaryKey(Serializable... args)
			throws SQLException
	{
		Connection conn = this.getConnection();
		try
		{
			JEntityMapping maping = getMaping();
			PreparedStatement prep = conn.prepareStatement(getPrimaryKeySelectString());
			System.out.println(getPrimaryKeySelectString());
			setParameter(prep, args);
			ResultSet res = prep.executeQuery();
			if (!res.next())
				return null;
			if (!res.isFirst() || !res.isLast())
				throw new SQLException("实体不唯一");
			SmipleJEntity entity = new SmipleJEntity();
			for (String lable : maping.lableIterator())
				entity.put(lable, res.getObject(lable));
			JEntityHandlerEvent event = new JEntityHandlerEvent(entity);
			doHandler("loaded", event);
			return entity;
		} finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	@Override
	public synchronized JEntityChain loadFragment(int start, int max, Serializable... args)
			throws SQLException
	{
		if (start < 0 || max <= 0)
			throw new SQLException(" start must >= 0,max must > 0.");
		Connection conn = this.getConnection();
		try
		{
			JEntityMapping maping = getMaping();
			PreparedStatement prep = conn.prepareStatement(getSelectString());
			setParameter(prep, args);
			prep.setMaxRows(start + max);
			ResultSet res = prep.executeQuery();
			JEntityChain ress = new JEntityChain();
			int l = 0;
			res.relative(start);
			while (res.next())
			{
				SmipleJEntity entity = new SmipleJEntity();
				for (String lable : maping.lableIterator())
					entity.put(lable, res.getObject(lable));
				ress.add(entity);
				l++;
				JEntityHandlerEvent event = new JEntityHandlerEvent(entity);
				doHandler("loaded", event);
			}
			return ress;
		} finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	public Map<String, Object> newEntity()
	{
		return new SmipleJEntity();
	}
	@Override
	public synchronized Map<String, Object> remove(Map<String, Object> arg)
			throws SQLException
	{
		JEntityMapping maping = getMaping();
		for (String key : maping.primaryKeyIterator())
			if (!arg.containsKey(key))
				throw new SQLException("传入的实体没有主建[" + key + "]");
		Connection conn = this.getConnection();
		try
		{
			conn.setAutoCommit(false);
			PreparedStatement prep = conn.prepareStatement(getPrimaryKeySelectString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			int i = 1;
			for (String key : maping.primaryKeyIterator())
				prep.setObject(i++, arg.get("$Original$" + key + "$"));
			ResultSet res = prep.executeQuery();
			if (!res.next())
				return arg;
			if (!res.isFirst() || !res.isLast())
				throw new SQLException("更新对应的实体不唯一");
			res.deleteRow();
			conn.commit();
			JEntityHandlerEvent event = new JEntityHandlerEvent(arg);
			doHandler("remove", event);
			return arg;
		} finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	@Override
	public Map<String, Object> removeByPrimaryKey(Serializable... args)
			throws SQLException
	{
		Connection conn = this.getConnection();
		try
		{
			conn.setAutoCommit(false);
			JEntityMapping maping = getMaping();
			PreparedStatement prep = conn.prepareStatement(getPrimaryKeySelectString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			setParameter(prep, args);
			ResultSet res = prep.executeQuery();
			if (!res.next())
				return null;
			if (!res.isFirst() || !res.isLast())
				throw new SQLException("实体不唯一");
			SmipleJEntity entity = new SmipleJEntity();
			for (String lable : maping.lableIterator())
				entity.put(lable, res.getObject(lable));
			JEntityHandlerEvent event = new JEntityHandlerEvent(entity);
			doHandler("removed", event);
			res.deleteRow();
			conn.commit();
			return entity;
		} finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	@Override
	public void removeHandler(JEntityHandler handler)
	{
		synchronized (handlers)
		{
			if (handlers.contains(handler))
				handlers.remove(handler);
		}
	}
	@Override
	public List<Map<String, Object>> removes(List<Map<String, Object>> args)
			throws SQLException
	{
		JEntityMapping maping = getMaping();
		Connection conn = this.getConnection();
		try
		{
			conn.setAutoCommit(false);
			PreparedStatement prep = conn.prepareStatement(getPrimaryKeySelectString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			for (Map<String, Object> arg : args)
			{
				for (String key : maping.primaryKeyIterator())
					if (!arg.containsKey(key))
						throw new SQLException("传入的实体没有主建[" + key + "]");
				prep.clearParameters();
				int i = 1;
				for (String key : maping.primaryKeyIterator())
					prep.setObject(i++, arg.get("$Original$" + key + "$"));
				ResultSet res = prep.executeQuery();
				if (!res.next())
					continue;
				if (!res.isFirst() || !res.isLast())
					throw new SQLException("更新对应的实体不唯一");
				res.deleteRow();
				res.close();
				JEntityHandlerEvent event = new JEntityHandlerEvent(arg);
				doHandler("remove", event);
			}
			conn.commit();
			return args;
		} finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	protected void setParameter(PreparedStatement prep, Serializable... args)
			throws SQLException
	{
		ParameterMetaData met = prep.getParameterMetaData();
		int c = met.getParameterCount();
		if (args.length < c)
			throw new SQLException("传入的参数不够.");
		for (int i = 0; i < c; i++)
		{
			if (args[i] == null)
			{
				prep.setNull(i + 1, met.getParameterType(i + 1));
				continue;
			}
			prep.setObject(i + 1, args[i]);
		}
	}
	@Override
	public synchronized Map<String, Object> update(Map<String, Object> arg)
			throws SQLException
	{
		JEntityMapping maping = getMaping();
		for (String key : maping.primaryKeyIterator())
			if (!arg.containsKey(key))
				throw new SQLException("传入的实体没有主建[" + key + "]");
		Connection conn = this.getConnection();
		try
		{
			conn.setAutoCommit(false);
			PreparedStatement prep = conn.prepareStatement(getPrimaryKeySelectString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			int i = 1;
			for (String key : maping.primaryKeyIterator())
				prep.setObject(i++, arg.get("$Original$" + key + "$"));
			ResultSet res = prep.executeQuery();
			if (!res.next())
				throw new SQLException("数据库是没有对应实体");
			if (!res.isFirst() || !res.isLast())
				throw new SQLException("更新对应的实体不唯一");
			for (String key : maping.lableIterator())
			{
				if (!arg.containsKey(key))
					continue;
				if (arg.get(key) == null)
					res.updateNull(key);
				else
					res.updateObject(key, arg.get(key));
			}
			res.updateRow();
			res.refreshRow();
			conn.commit();
			arg.clear();
			for (String lable : maping.lableIterator())
				arg.put(lable, res.getObject(lable));
			JEntityHandlerEvent event = new JEntityHandlerEvent(arg);
			doHandler("update", event);
			return arg;
		} finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	@Override
	public List<Map<String, Object>> updates(List<Map<String, Object>> args)
			throws SQLException
	{
		JEntityMapping maping = getMaping();
		Connection conn = this.getConnection();
		try
		{
			conn.setAutoCommit(false);
			PreparedStatement prep = conn.prepareStatement(getPrimaryKeySelectString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			for (Map<String, Object> arg : args)
			{
				for (String key : maping.primaryKeyIterator())
					if (!arg.containsKey(key))
						throw new SQLException("传入的实体没有主建[" + key + "]");
				prep.clearParameters();
				int i = 1;
				for (String key : maping.primaryKeyIterator())
					prep.setObject(i++, arg.get("$Original$" + key + "$"));
				ResultSet res = prep.executeQuery();
				if (!res.next())
					throw new SQLException("数据库是没有对应实体");
				if (!res.isFirst() || !res.isLast())
					throw new SQLException("更新对应的实体不唯一");
				for (String key : maping.lableIterator())
				{
					if (!arg.containsKey(key))
						continue;
					if (arg.get(key) == null)
						res.updateNull(key);
					else
						res.updateObject(key, arg.get(key));
				}
				res.updateRow();
				res.refreshRow();
				arg.clear();
				for (String lable : maping.lableIterator())
					arg.put(lable, res.getObject(lable));
				res.close();
				JEntityHandlerEvent event = new JEntityHandlerEvent(arg);
				doHandler("update", event);
			}
			conn.commit();
			return args;
		} finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	protected class JEntityChain
			extends Vector<Map<String, Object>>
			implements List<Map<String, Object>>
	{
		private static final long	serialVersionUID	= 1L;
	}
	protected class SmipleJEntity
			implements Map<String, Object>
	{
		private static final long	serialVersionUID	= -2218957319044417167L;
		private Map<String, Object>	value;
		{
			value = new HashMap<String, Object>();
		}
		@Override
		public synchronized void clear()
		{
			value.clear();
		}
		@Override
		public synchronized boolean containsKey(Object key)
		{
			return value.containsKey((String) key);
		}
		@Override
		public synchronized boolean containsValue(Object val)
		{
			return value.containsValue(val);
		}
		public Object doPut(String key, Object val)
		{
			if (val == null && getMaping().isAuto(key))
				return this.remove(key);
			if (!value.containsKey(key))
				value.put("$Original$" + key + "$", val);
			return value.put(key, val);
		}
		@Override
		public Set<java.util.Map.Entry<String, Object>> entrySet()
		{
			return value.entrySet();
		}
		@Override
		public Object get(Object key)
		{
			return value.get(key);
		}
		@Override
		public synchronized boolean isEmpty()
		{
			return value.isEmpty();
		}
		@Override
		public Set<String> keySet()
		{
			return value.keySet();
		}
		@Override
		public synchronized Object put(String key, Object value)
		{
			if (key == null || key.startsWith("$Original$"))
				return value;
			if (value == null)
			{
				return doPut(key, value);
			}
			try
			{
				Class<?> dbclz = getMaping().getType(key);
				Class<?> clz = value.getClass();
				JWrapDriver driver = JWrapDriverManager.getJWrapper(clz, dbclz);
				if (driver != null)
					value = driver.wrap(value, dbclz);
			} catch (Exception e)
			{}
			return doPut(key, value);
		}
		@Override
		public synchronized void putAll(Map<? extends String, ? extends Object> m)
		{
			for (Entry<?, ?> entry : m.entrySet())
				put((String) entry.getKey(), entry.getValue());
		}
		@Override
		public synchronized Object remove(Object key)
		{
			value.remove("$Original$" + key + "$");
			return value.remove(key);
		}
		@Override
		public synchronized int size()
		{
			return value.size();
		}
		@Override
		public Collection<Object> values()
		{
			return value.values();
		}
	}
}

package net.modellite.jdbcp;

import java.sql.SQLException;

public class JdbcpException
	extends SQLException
{
	private static final long	serialVersionUID	= -4405256394113909274L;

	public JdbcpException()
	{
		super();
	}

	public JdbcpException(String string)
	{
		super(string);
	}

	public JdbcpException(String string, String code)
	{
		super(string, code);
	}

	public JdbcpException(Throwable throwable)
	{
		super(throwable);
	}
}

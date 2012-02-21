package com.ccfmis.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Executions;

public class WebFilter extends HttpServlet implements Filter
{

	private static final long serialVersionUID = 1L;

	protected FilterConfig filterConfig = null;
	private String redirectURL = null;
	private List notCheckURLList = new ArrayList();

	/**
	 * 用于检测用户是否登陆的过滤器，如果未登录，则重定向到指的登录页面
	 * 
	 * 配置参数
	 * 
	 * redirectURL 如果用户未登录，则重定向到指定的页面，URL不包括 ContextPath
	 */

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException
	{
		// TODO Auto-generated method stub

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		HttpSession session = request.getSession();
		if (session.getAttribute("username") != null)
		{
			filterChain.doFilter(request, response);
		}
		else
		{
			// request.getRequestDispatcher(redirectURL).forward(request,response);
			response.sendRedirect(request.getContextPath() + redirectURL);
			return;
		}

	}

	private boolean checkRequestURIIntNotFilterList(HttpServletRequest request)
	{
		String uri = request.getServletPath()
				+ (request.getPathInfo() == null ? "" : request.getPathInfo());
		return notCheckURLList.contains(uri);
	}

	/**
	 * 
	 */
	public void destroy()
	{
		filterConfig = null;

	}

	@Override
	public void init(FilterConfig filterConfig)
	{
		// TODO Auto-generated method stub
		this.filterConfig = filterConfig;
		redirectURL = (filterConfig).getInitParameter("redirectURL");
		String notCheckURLListStr = (filterConfig)
				.getInitParameter("notCheckURLList");

		if (notCheckURLListStr != null)
		{
			StringTokenizer st = new StringTokenizer(notCheckURLListStr, ";");
			notCheckURLList.clear();
			while (st.hasMoreTokens())
			{
				notCheckURLList.add(st.nextToken());
			}
		}

	}
}

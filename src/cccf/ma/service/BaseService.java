package cccf.ma.service;

import java.util.List; 

public interface BaseService {
	
	 
	/**
	 * 获取查询记录数——无参数
	 * @param hql 声明，表达式
	 * @return 查询记录数
	 */
	public int queryForResultSize(String hql); 
	/**
	 * 分页查询——无参数
	 * @param startPosition 起始位置
	 * @param maxresult     最大记录数
	 * @param hql     声明，表达式     
	 * @return 查询结果
	 */
	public List  queryListForPage(  int startPosition,  int maxresult ,  String hql);
	/**
	 * 返回一个对象
	 * @param hql
	 * @return
	 */
	public Object querySingleResult(String hql); 
	/**
	 * 返回列表
	 * @param hql
	 * @return
	 */
	public List  getResultList(String hql); 
	/**
	 * 输入 hql 列表
	 * 返回 结果集列表
	 * @param inputs
	 * @return
	 */
	public List  queryMultipleResults(List<String> inputs); 
}

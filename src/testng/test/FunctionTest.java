package testng.test;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

import cccf.ma.service.StateInfoService;
import cccf.ma.service.StateInfoServiceUtil;
import cccf.ma.service.impl.StateInfoServiceImpl;

public class FunctionTest{
	private static FunctionTest INSTALL = null;
	private BeanFactory beanFactory = null;
	private static final String SPRING_CFG = "file:WebContent/WEB-INF/ctx/applicationContext-hibernate.xml";

	
	StateInfoService ss ;
	
	@Test
	public void f() {
		ss = (StateInfoService) this.getInstance().getBean("StateInfoService");
		
		System.out.println(StateInfoServiceUtil.getAll());
	}
	
	public synchronized static FunctionTest getInstance() {
		if (INSTALL == null) {
			INSTALL = new FunctionTest();
		}
		return INSTALL;
	}
	
	private synchronized BeanFactory getBeanFactory() {
		if (this.beanFactory == null) {
			this.beanFactory = new ClassPathXmlApplicationContext(SPRING_CFG);
		}
		return this.beanFactory;
	}

	public Object getBean(String beanName) throws NullPointerException {
		if (beanName == null) {
			throw new java.lang.NullPointerException("beanName不能为空!");
		}
		return this.getBeanFactory().getBean(beanName);
	}

}

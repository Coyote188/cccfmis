package openjframework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.zhtml.S;

import openjframework.model.OrganizationInfo;
import openjframework.service.OrganizationInfoServiceUtil;

import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.service.ProductCatalogueInfoServiceUtil;

public class NextCodeCalculate {

	//产品目录自动编码
	/**
	 * 一级目录编码
	 * @return
	 */
	public static String nextCode()
	{
		int maxManti=100000;
		
		List<ProductCatalogueInfo>productList=ProductCatalogueInfoServiceUtil.getDataListByLevel(1);
		if(productList.size()>0)
		for(ProductCatalogueInfo _product:productList)
		{
			
			String productNo=_product.getProductNo();
			String mantissaStr=productNo.substring(productNo.length()-6, productNo.length());
			int mantissa=Integer.parseInt(mantissaStr);
			if(mantissa>maxManti)
				maxManti=mantissa;
			
		}
		StringBuffer sb = new StringBuffer("");    
		sb.append(String.valueOf(maxManti+1));
		
		return String.valueOf(sb);
	}
	
	/**
	 * 同级产品目录编码
	 * @param product
	 * @return
	 */
	public static String nextCode(ProductCatalogueInfo product)
	{
		int maxManti=100000;
		
//		List<ProductCatalogueInfo>productList=ProductCatalogueInfoServiceUtil.getDataListByLevel(product.getProductLevel());
//		if(productList.size()>0)
//		for(ProductCatalogueInfo _product:productList)
//		{
//			
//			String productNo=_product.getProductNo();
//			String mantissaStr=productNo.substring(productNo.length()-6, productNo.length());
//			int mantissa=Integer.parseInt(mantissaStr);
//			if(mantissa>maxManti)
//				maxManti=mantissa;
//			
//		}
		
		String productNo=product.getProductNo();
		String mantissaStr=productNo.substring(productNo.length()-6, productNo.length());
		int mantissa=Integer.parseInt(mantissaStr);
		if(mantissa>maxManti)
			maxManti=mantissa;
		
		StringBuffer sb = new StringBuffer("");   
		sb.append(productNo.substring(0,productNo.length()-6));
		sb.append(String.valueOf(maxManti+1));
		
		return String.valueOf(sb);
	}
	/**
	 * 子级产品目录编码
	 * @param product
	 * @return
	 */
	public  static String nextCodeForChild(ProductCatalogueInfo product)
	{
		int maxManti=100000;
		
		Set children=product.getProductChildren();
		List<ProductCatalogueInfo>productList=new ArrayList(children);
		if(productList.size()>0)
		for(ProductCatalogueInfo _product:productList)
		{
			String productNo=_product.getProductNo();
			String mantissaStr=productNo.substring(productNo.length()-6, productNo.length());
			int mantissa=Integer.parseInt(mantissaStr);
			if(mantissa>maxManti)
				maxManti=mantissa;
			
		}
		
		StringBuffer sb = new StringBuffer(product.getProductNo());    
		sb.append(String.valueOf(maxManti+1));
		
		return String.valueOf(sb);
	}
	
	//部门自动编码
	
	
	 /** 一级部门编码
	 * @return
	 */
	public static String nextCode_department()
	{
		int maxManti=100000;
		
		List<OrganizationInfo>departmentList=OrganizationInfoServiceUtil.getByOrganizationLevel(1);
		if(departmentList.size()>0)
		for(OrganizationInfo _department:departmentList)
		{
			
			String departNo=_department.getOrganizationNo();
			String mantissaStr=departNo.substring(departNo.length()-6, departNo.length());
			int mantissa=Integer.parseInt(mantissaStr);
			if(mantissa>maxManti)
				maxManti=mantissa;
			
		}
		StringBuffer sb = new StringBuffer("");    
		sb.append(String.valueOf(maxManti+1));
		
		return String.valueOf(sb);
	}
	
	/**
	 * 同级部门编码
	 * @param product
	 * @return
	 */
	public static String nextCode(OrganizationInfo organization)
	{
		int maxManti=100000;
		
		List<OrganizationInfo>organizationList=OrganizationInfoServiceUtil.getByOrganizationLevel(organization.getOrganizationLevel());
		if(organizationList.size()>0)
		for(OrganizationInfo _organization:organizationList)
		{
			
			String organizationNo=_organization.getOrganizationNo();
			String mantissaStr=organizationNo.substring(organizationNo.length()-6, organizationNo.length());
			int mantissa=Integer.parseInt(mantissaStr);
			if(mantissa>maxManti)
				maxManti=mantissa;
			
		}
		StringBuffer sb = new StringBuffer("");    
		sb.append(String.valueOf(maxManti+1));
		
		return String.valueOf(sb);
	}
	/**
	 * 子级部门编码
	 * @param product
	 * @return
	 */
	public  static String nextCodeForChild(OrganizationInfo organization)
	{
		int maxManti=100000;
		
		Set children=organization.getOrganizationChildren();
		List<OrganizationInfo>organizationList=new ArrayList(children);
		if(organizationList.size()>0)
		for(OrganizationInfo _organization:organizationList)
		{
			String organizationNo=_organization.getOrganizationNo();
			String mantissaStr=organizationNo.substring(organizationNo.length()-6, organizationNo.length());
			int mantissa=Integer.parseInt(mantissaStr);
			if(mantissa>maxManti)
				maxManti=mantissa;
			
		}
		
		StringBuffer sb = new StringBuffer(organization.getOrganizationNo());    
		sb.append(String.valueOf(maxManti+1));
		
		return String.valueOf(sb);
	}
	
	
}

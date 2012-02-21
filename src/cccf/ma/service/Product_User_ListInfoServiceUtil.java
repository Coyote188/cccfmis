package cccf.ma.service;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import openjframework.model.UserInfo;
import cccf.ma.model.ProductCatalogueInfo;
import cccf.ma.model.Product_User_ListInfo;

import com.aidi.core.spring.CustomerContextHolder;
import com.aidi.core.web.framework.ServiceAdapter;
public class Product_User_ListInfoServiceUtil
{
    public static Serializable  create(Product_User_ListInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
       Product_User_ListInfoService service=(Product_User_ListInfoService)ServiceAdapter.getServiceByName("Product_User_ListInfoService");
       return service.create(bean);
    }
    public static void delete(Product_User_ListInfo bean){
    	CustomerContextHolder.setCustomerType("cccf");
        Product_User_ListInfoService service=(Product_User_ListInfoService)ServiceAdapter.getServiceByName("Product_User_ListInfoService");
       service.delete(bean);
       }
    public static void saveOrUpdate(Product_User_ListInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        Product_User_ListInfoService service=(Product_User_ListInfoService)ServiceAdapter.getServiceByName("Product_User_ListInfoService");
       service.saveOrUpdate(bean);
       }
    public static void update(Product_User_ListInfo bean){
       CustomerContextHolder.setCustomerType("cccf");
        Product_User_ListInfoService service=(Product_User_ListInfoService)ServiceAdapter.getServiceByName("Product_User_ListInfoService");
       service.update(bean);
       }
    public static List getAll(){
       CustomerContextHolder.setCustomerType("cccf");
        Product_User_ListInfoService service=(Product_User_ListInfoService)ServiceAdapter.getServiceByName("Product_User_ListInfoService");
       return service.getAll();
       }
    public static Product_User_ListInfo getById(String id){
    	CustomerContextHolder.setCustomerType("cccf");
        Product_User_ListInfoService service=(Product_User_ListInfoService)ServiceAdapter.getServiceByName("Product_User_ListInfoService");
       return service.getById(id);
       }
     public static Product_User_ListInfo getByPrimaryKey(String key){
    	CustomerContextHolder.setCustomerType("cccf");
        Product_User_ListInfoService service=(Product_User_ListInfoService)ServiceAdapter.getServiceByName("Product_User_ListInfoService");
       return service.getByPrimaryKey(key);
       }
    public static List findByQuery(String querystr){
        CustomerContextHolder.setCustomerType("cccf");
        Product_User_ListInfoService service=(Product_User_ListInfoService)ServiceAdapter.getServiceByName("Product_User_ListInfoService");
       return service.findByQuery(querystr);
       }
    /**
     * 检索产品的管辖人员
     * @param product 产品
     * @param type  类型
     * @return
     */
    public static List<UserInfo> getUserByProductofJurisdiction(ProductCatalogueInfo product,int type)
    {
    	String qstr="from Product_User_ListInfo where product.id='"+product.getId()+"' and type="+type;
    	List<Product_User_ListInfo>pro_userlist=findByQuery(qstr);
    	List<UserInfo>userlist=new ArrayList();
    	if(pro_userlist!=null)
    	for(Product_User_ListInfo pul:pro_userlist)
    	{
    		userlist.add(pul.getUser());
    	}
    	return userlist;
    }
    /**
     * 检索人员管辖的产品
     * @param user  人员
     * @param type  类型
     * @return
     */
    public static List<ProductCatalogueInfo> getProductByuserofJurisdiction(UserInfo user,int type)
    {
    	String qstr="form Product_User_ListInfo where user.id='"+user.getId()+"' and type="+type;
    	List<Product_User_ListInfo>pro_userlist=findByQuery(qstr);
    	List<ProductCatalogueInfo>productlist=new ArrayList();
    	if(pro_userlist!=null)
    	for(Product_User_ListInfo pul:pro_userlist)
    	{
    		productlist.add(pul.getProduct());
    	}
    	return productlist;
    }
    public static List<ProductCatalogueInfo> findProductByUser(UserInfo user){
    	String qstr="from Product_User_ListInfo where user.id='"+user.getId()+"'";
    	List<Product_User_ListInfo>pro_userlist=findByQuery(qstr);
    	List<ProductCatalogueInfo>productlist=new ArrayList();
    	if(pro_userlist!=null)
    	for(Product_User_ListInfo pul:pro_userlist)
    	{
    		productlist.add(pul.getProduct());
    	}
    	return productlist;
    }
    @SuppressWarnings("unchecked")
	public static List<UserInfo> findUserByProductList(List<ProductCatalogueInfo> plist) {
		List<UserInfo> userList = new ArrayList<UserInfo>();
		for (ProductCatalogueInfo product : plist) {
			String qstr = "from Product_User_ListInfo where product.id='" + product.getId() + "'";
			List<Product_User_ListInfo> pro_userlist = findByQuery(qstr);
			if (!userList.isEmpty()) {
				boolean isExist = false;
				for (Product_User_ListInfo pul : pro_userlist) {
					for (Iterator it = userList.iterator(); it.hasNext();) {
						if (((UserInfo) it.next()).getId().equals(
								pul.getUser().getId()))
							isExist = true;
						if(isExist)
							break;
					}
					if (!isExist){
						userList.add(pul.getUser());
						break;
					}
				}
			}else{
				for(Product_User_ListInfo pul : pro_userlist){
					userList.add(pul.getUser());
				}
				
			}
		}

		return userList;
	}
    public boolean isExists(ProductCatalogueInfo product,UserInfo user,int type)
    {
    	String qstr="form Product_User_ListInfo where product.id='"+product.getId()+"' user.id='"+user.getId()+"' and type="+type;
    	List<Product_User_ListInfo>pro_userlist=findByQuery(qstr);
    	if(pro_userlist!=null)
    		return true;
    	return false;
    }
    public static void deleteByProduct(ProductCatalogueInfo product)
    {
    	String qstr="from Product_User_ListInfo where product.id='"+product.getId()+"'";
    	List<Product_User_ListInfo>pro_userlist=findByQuery(qstr);
    	if(pro_userlist!=null)
    	for(Product_User_ListInfo pul:pro_userlist)
    	{
    		delete(pul);
    	}
    }
    public static boolean isUserHasPermissionOfProduct(ProductCatalogueInfo product,UserInfo user){
    	String querystr = "from Product_User_ListInfo where product.id='"+product.getId()+"' and user.id='" + user.getId() + "'";
    	
    	return findByQuery(querystr).isEmpty()? true:false ;
    }
}
   
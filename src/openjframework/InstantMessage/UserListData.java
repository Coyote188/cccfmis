package openjframework.InstantMessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import openjframework.model.UserInfo;
import openjframework.service.UserInfoServiceUtil;

public class UserListData {
	
		private  List<UserInfo> userList=new ArrayList<UserInfo>();
		private static UserListData userListData=new UserListData();
		private UserListData(){}
		public static UserListData getInstance(){
			return userListData;
		}
		
		public  List<UserInfo> getAllUser(){
			return userList;
		}
		
		public  void addUser(UserInfo user ,int status){
			userList.add(user);
			//设置在线
			user.setStatus(status);
			UserInfoServiceUtil.update(user);
		}
		public boolean isExistsSelf(UserInfo user)
		{
			for(Iterator it=userList.iterator();it.hasNext();)
			{
				UserInfo _user=(UserInfo)it.next();
				if(user.getId().equals(_user.getId()))
					return true;
			}
			return false;
			
		}
		public void updateUser(UserInfo user ,int status)
		{
			for(Iterator it=userList.iterator();it.hasNext();)
			{
				UserInfo _user=(UserInfo)it.next();
				if(user.getId().equals(_user.getId()))
				{
					 it.remove();
					 
					}
			}
			addUser(user,status);
			 user.setStatus(status);
			 UserInfoServiceUtil.update(user);
		}
		
		public void delUser(UserInfo user){
		
			//list.remove(user);不能真正的移除user,
			//通过循环可以解决,但是又不能在循环中使用list.remove();
			//....................
			//好不容易通过迭代解决了这个诡异的问题
			for(Iterator it=userList.iterator();it.hasNext();)
			{
				UserInfo _user=(UserInfo)it.next();
				if(user.getId().equals(_user.getId()))
					it.remove();
			}
			
			
		}
		
	
	
}

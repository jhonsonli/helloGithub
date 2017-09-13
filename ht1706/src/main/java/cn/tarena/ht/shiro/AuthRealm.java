package cn.tarena.ht.shiro;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.org.apache.xerces.internal.xs.StringList;

import cn.tarena.ht.pojo.Role;
import cn.tarena.ht.pojo.User;
import cn.tarena.ht.service.UserService;

public class AuthRealm extends AuthorizingRealm{
	
	@Autowired
	private UserService userService;
	
	
	//权限认证
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		//获取用户的真实对象
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		
		//存放用户的真实的权限信息
		List<String> pList = userService.findPrivilegeList(user.getUserId());
		
		//pList.add("货运管理");
		//pList.add("基础信息");
		//pList.add("系统管理");
		
		//为权限控制提供真实数据
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(pList);
		
		return info;
	}
	
	
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//登陆认证   shiro安全中心调用realm查询用户真实信息，传递token数据
		//需要为Shiro安全中心提供真实的用户数据   需要根据用户名查询user对象
		//1.转化为usernamePasswordToken
		UsernamePasswordToken loginToken = (UsernamePasswordToken) token;
		
		//2.获取用户名
		String username = loginToken.getUsername();
		
		//3.根据用户名查询数据时，要求用户名必须唯一
		User user = userService.findUserByUserName(username);
		
		//4.将查询到的用户真实信息返回给Shiro安全中心
		/**
		 * 1.principal   表示用户的真实对象
		 * 2.credentials 校验密码时使用的  真实的密码
		 * 3.realmName   realm的名称
		 */
		
		AuthenticationInfo info = 
				new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
		
		//问题：用户输入的密码时明文密码，用户的真实密码是密文如何做到匹配？	
		return info;
	}
}

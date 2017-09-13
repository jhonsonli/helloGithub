package cn.tarena.ht.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.tarena.ht.pojo.User;
import cn.tarena.ht.service.UserService;
import cn.tarena.ht.tool.MD5HashPassword;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	//跳转到登陆页面
	@RequestMapping("/toLogin.action")
	public String toLogin(){
		
		return "/sysadmin/login/login";
	}
		
	//采用Shiro的登陆操作
	@RequestMapping("/login.action")
	public String login(String userName,String password,Model model,HttpSession session){
		/**
		 * 1.判断用户输入的内容是否为空
		 * 2.通过用户名和密码进行查询操作 得到user对象
		 * 3.如果对象为null,证明用户名和密码不正确  编辑提示信息 页面跳转到登陆页面
		 * 4.如果user对象不为null 则证明用户名和密码正确， 跳转到系统首页
		 */
		
		if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
			//证明用户名或密码为空
			model.addAttribute("errorInfo", "用户名或密码不能为空");
			
			//转向到登陆页面    通过return   转发    重定向
			return "/sysadmin/login/login";	
		}
		
		//1.获取subject对象
		Subject subject = SecurityUtils.getSubject();
		
		//2.定义登陆的令牌/票  将用户名和密码包装为令牌
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		
		
		//3.通过subject进行登陆操作
		try {
			subject.login(token);		
			
			//获取用户的真实信息
			User user = (User) subject.getPrincipal();
			
			//获取Session
			//subject.getSession().setAttribute("SessionUser", user);
			session.setAttribute("SessionUser", user);
			return "redirect:/home.action";   //如果登陆成功，shiro将会放行所有的请求		
			
		} catch (AuthenticationException a) {
			//表示登录失败
			model.addAttribute("errorInfo", "用户名或密码错误");
			a.printStackTrace();
			return "/sysadmin/login/login";
		} catch (Exception e){
			//表示一些未知错误
			e.printStackTrace();
			model.addAttribute("errorInfo", "发现未知错误，联系管理员！！");
			return "/sysadmin/login/login";
		}
	
	}
	
	
	
	
	
/*	@RequestMapping("/login.action")
	public String login(String userName,String password,Model model,HttpSession session){
		/**
		 * 1.判断用户输入的内容是否为空
		 * 2.通过用户名和密码进行查询操作 得到user对象
		 * 3.如果对象为null,证明用户名和密码不正确  编辑提示信息 页面跳转到登陆页面
		 * 4.如果user对象不为null 则证明用户名和密码正确， 跳转到系统首页
		 *//*
		
		if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
			//证明用户名或密码为空
			model.addAttribute("errorInfo", "用户名或密码不能为空");
			
			//转向到登陆页面    通过return   转发    重定向
			return "/sysadmin/login/login";
			
		}
		
		//将明文进行加密处理
		String md5Password = MD5HashPassword.getPassword(userName, password);
		
		//用户名和密码都有值
		User user = userService.findUserByUP(userName,md5Password);
		
		
		if(user==null){
			//表示用户名和密码错误
			model.addAttribute("errorInfo", "用户名或密码错误");
			return "/sysadmin/login/login";
		}

		//将对象写入Session
		session.setAttribute("SessionUser", user);
		
		//程序运行到此处 证明用户名和密码正确
		return "redirect:/home.action";

	}*/
	
	
	//系统登出
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		
		//将数据从session域中清除
		session.removeAttribute("SessionUser");
		
		return "/sysadmin/login/login";
	}
	
	
	
	
	
	
	
}

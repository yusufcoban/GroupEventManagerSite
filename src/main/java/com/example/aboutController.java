package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class aboutController{
@Autowired
public userDao userController;

@RequestMapping("/about")
public String showAbout(){
	int id = getcurrentuser().getUserid();
	if (id == 1) {
		return "/about_admin";
	} else if (id == 4) {
		return "/about";
	} else
		return "/about_user";

}

// get logged User
public user getcurrentuser() {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	String name = auth.getName();
	user logged = userController.findOne(Integer.parseInt(name));
	return logged;
}
}
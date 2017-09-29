package com.example;

import java.util.HashSet;
import java.util.Set;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class categoryEventController {

	@Autowired
	public eventDao eventsController;
	@Autowired
	public userDao userController;
	@Autowired
	public sportartenDao sportController;
	
	@RequestMapping("/categorylist")
	public String change_user(@RequestParam String cat, ModelMap modelmap) throws MessagingException {
		Iterable<events> fullList = eventsController.findAll();
		Set<events> returnList = new HashSet<events>();
		for (events u : fullList) {
			if (u.getsportart().equals(cat)) {
				returnList.add(u);
			}
		}
		modelmap.put("eventListe", returnList);
		
		int id = getcurrentuser().getUserid();
		if (id == 1) {
			return "/events_admin";
		} else if (id == 4) {
			return "/events";
		} else
			return "/events_user";
	}

	
	
	
	public user getcurrentuser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		user logged = userController.findOne(Integer.parseInt(name));
		return logged;
	}

}

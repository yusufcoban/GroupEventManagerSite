package com.example;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("myevents")
public class htmlgetinventedEvents {

	@Autowired
	public eventDao eventsController;
	@Autowired
	public userDao userController;
	@Autowired
	public sportartenDao sportController;

	@RequestMapping(method = RequestMethod.GET)
	public String user(ModelMap modelmap) {
		user logged = getcurrentuser();
		modelmap.put("eventListe", logged.getEventSet());
		if (logged.getUserid() <= 1) {
			return "myevents_admin";
		}if (logged.getUserid() == 4) {
			return "myevents";
		}
		else {
			return "myevents_user";
		}
	}

	// FILL ALL EVENTs FOR TESTING RESEASONS
	@RequestMapping("fillinall")
	public String checkinforallevents(ModelMap modelmap) {
		Iterable<events> eventtester = eventsController.findAll();
		Set<events> listetester = new HashSet<events>((Collection) eventtester);
		user logged = getcurrentuser();
		logged.setEventSet(listetester);
		userController.save(logged);
		modelmap.put("eventListe", logged.getEventSet());
		if (logged.getUserid() <= 1) {
			return "myevents_admin";
		}if (logged.getUserid() == 4) {
			return "myevents";
		}
		else {
			return "myevents_user";
		}

	}

	// get logged User
	public user getcurrentuser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		user logged = userController.findOne(Integer.parseInt(name));
		return logged;
	}

}

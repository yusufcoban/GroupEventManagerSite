package com.example;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("category")
public class categoryController {
	@Autowired
	public eventDao eventsController;
	@Autowired
	public sportartenDao sportController;
	@Autowired
	public userDao userController;

	@RequestMapping(method = RequestMethod.GET)
	public String user(ModelMap modelmap) {
		// Schaue nach Abonements und gebe weiter an Template
		user logged = getcurrentuser();
		Set<sportarten> abos = logged.getAbos();
		Set<String> inhalteabo = new HashSet<String>();
		for (sportarten a : abos) {
			try {
				inhalteabo.add(a.getsportart());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (inhalteabo.contains("Yoga")) {
			modelmap.addAttribute("yogaabo", "ja");
		} else
			modelmap.addAttribute("yogaabo", "nein");
		if (inhalteabo.contains("Fussball")) {
			modelmap.addAttribute("fussballabo", "ja");
		} else
			modelmap.addAttribute("fussballabo", "nein");
		if (inhalteabo.contains("Basketball")) {
			modelmap.addAttribute("basketballabo", "ja");
		} else
			modelmap.addAttribute("basketballabo", "nein");

		int id = getcurrentuser().getUserid();
		if (id == 1) {
			return "/showByCategory_admin";
		} else if (id == 4) {
			return "/showByCategory";
		} else
			return "/showByCategory_user";
	}

	@GetMapping("/{sporttyp}")
	public String getuser(@PathVariable("sporttyp") String sporttyp, ModelMap modelmap) {
		Iterable<events> fullList = eventsController.findAll();
		Set<events> returnList = new HashSet<events>();
		for (events u : fullList) {
			if (u.getsportart().equals(sporttyp)) {
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

	// get logged User
	public user getcurrentuser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		user logged = userController.findOne(Integer.parseInt(name));
		return logged;
	}

}

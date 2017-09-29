package com.example;

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
@RequestMapping("html")
public class htmlController {

	@Autowired
	public userDao userDDDao;

	@RequestMapping(method = RequestMethod.GET)
	public String user(ModelMap modelmap) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName(); // get logged in username
		Iterable<user> userListJava;
		userListJava = userDDDao.findAll();
		// Iterable<user> returnuser;
		for (user a : userListJava) {
			if (a.getUserid() == Integer.parseInt(name)) {
				modelmap.put("userliste", a);
				System.out.println("Ich bin genau der...");
				break;
			} else {
				System.out.println("nicht ich...");
			}
		}
		return "user";
	}

	// @GetMapping("/{id}")
	// public String getuser(@PathVariable("id") Integer id, ModelMap modelmap)
	// {
	// modelmap.put("userliste", userDDDao.findOne(id));
	// return "user";
	// }

	@GetMapping("/{id}")
	public String getuser(@PathVariable("id") Integer id, ModelMap modelmap) {
		modelmap.put("userliste", userDDDao.findOne(id));
		return "user";
	}

}

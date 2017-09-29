package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("sportarten")
public class htmlSportartenController {

	@Autowired
	public sportartenDao sportDao;

	@RequestMapping(method = RequestMethod.GET)
	public String user(ModelMap modelmap) {
		modelmap.put("sportartenListe", sportDao.findAll());
		return "sportarten";
	}

}

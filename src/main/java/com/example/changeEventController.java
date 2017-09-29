package com.example;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class changeEventController {

	@Autowired
	public eventDao eventsController;
	@Autowired
	public userDao userController;
	@Autowired
	public sportartenDao sportController;

	@RequestMapping("/change")
	public String change_begin(@RequestParam int id, ModelMap model) {
		events event = eventsController.findOne(id);
		Iterable<sportarten> sportart = sportController.findAll();
		model.addAttribute("events", event);
		model.addAttribute("sportarten", sportart);
		int ids = getcurrentuser().getUserid();
		if (ids <= 0) {
			return "change_event_admin";
		} else
			return "change_event";
	}

	@RequestMapping(value = "/change_submit", method = RequestMethod.POST)
	public String change_submit(@ModelAttribute events events, ModelMap modelmap,
			@DateTimeFormat(pattern = "dd/MM/YY HH:mm") Date fromDate) throws MessagingException {
		Date test = fromDate;
		events input = eventsController.findOne(events.getEventid());
		events.setUserliste(input.getUser());
		eventsController.save(events);
		System.out.println(events.getEventid());
		Iterable<user> userliste = userController.findAll();
		int b = events.getEventid();
		for (user a : userliste) {
			if (a.getEventSet().contains(eventsController.findOne(events.getEventid()))) {
				System.out.println("ich " + a.getVorname() + a.getNachname() + "bin in" + events.getEventid());
				sendmail(a, b);
			}
		}
		Iterable<events> eventtester = eventsController.findAll();
		Set<events> kompletteListe = new HashSet<events>((Collection) eventtester);
		Set<events> vergleichsListe = getcurrentuser().getEventSet();
		kompletteListe.removeAll(vergleichsListe);
		modelmap.put("eventListe", kompletteListe);
		int ids = getcurrentuser().getUserid();
		if (ids <= 1) {
			return "events_admin";
		} else
			return "events";
	}

	// get logged User
	public user getcurrentuser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		user logged = userController.findOne(Integer.parseInt(name));
		return logged;
	}

	// sending mail universal
	public void sendmail(user user, int b) throws MessagingException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();
		JavaMailSenderImpl mailSender = ctx.getBean(JavaMailSenderImpl.class);
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);
		mailMsg.setFrom("wieslaufannahsesslingen@gmail.com");
		mailMsg.setTo(user.getEmail());
		mailMsg.setSubject("Event wurde geändert...");
		mailMsg.setText("EVENT mit der ID: " + b + " wurde geändert.......");
		mailSender.send(mimeMessage);
		System.out.println("---Done---");

	}

}

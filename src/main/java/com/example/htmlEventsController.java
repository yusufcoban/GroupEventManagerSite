package com.example;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("events")
public class htmlEventsController {

	@Autowired
	public eventDao eventsController;
	@Autowired
	public userDao userController;
	@Autowired
	public sportartenDao sportController;

	@RequestMapping(method = RequestMethod.GET)
	public String user(ModelMap modelmap) {
		user logged = getcurrentuser();
		Iterable<events> eventtester = eventsController.findAll();
		Set<events> kompletteListe = new HashSet<events>((Collection) eventtester);
		Set<events> vergleichsListe = logged.getEventSet();
		kompletteListe.removeAll(vergleichsListe);
		modelmap.put("eventListe", kompletteListe);
		int id = getcurrentuser().getUserid();
		if (id <= 1) {
			Iterable<sportarten> sportart = sportController.findAll();
			modelmap.addAttribute("sportarten", sportart);
			return "events_admin";
		}
		if (id == 4) {
			return "events";
		} else
			return "events_user";
	}

	// Details über das Event
	@RequestMapping("/details")
	public String getinfos(@RequestParam int id, ModelMap modelmap) {
		events event = eventsController.findOne(id);
		modelmap.put("eventListe", event);
		modelmap.put("userListe", event.getUser());
		modelmap.put("infos", event.getInfos());
		modelmap.put("austragen", "/events/invent?id=" + event.getEventid());
		modelmap.put("eintragen", "/events/expend?id=" + event.getEventid());
		user current = getcurrentuser();
		// NEU
		boolean syso=current.getEventSet().contains(event);
		System.out.println(syso);
		if (current.getEventSet().contains(event)) {
			modelmap.put("status", "ja");
		} else
			modelmap.put("status", "nein");

		int ids = getcurrentuser().getUserid();
		if (ids <= 1) {
			return "eventdetails_admin";
		}
		if (ids == 4) {
			return "eventdetails";
		}
		return "eventdetails_user";
		// GEHT LAKa

	}

	// Event löschen
	@RequestMapping("/delete")
	public String delete(@RequestParam int id, ModelMap modelmap) throws MessagingException {
		events event = eventsController.findOne(id);
		System.out.println(event.getEventid() + "gelöscht!");
		sendmail(event, "gelöscht");
		eventsController.delete(event);
		modelmap.put("eventListe", event);
		return "redirect://localhost:7777/events.html";

	}

	@RequestMapping("/update")
	public String update(@RequestParam int id, ModelMap modelmap) {
		events event = eventsController.findOne(id);
		modelmap.put("eventListe", event);
		return "eventdetails";
		// GEHT LAK
	}

	// Eintragen
	@RequestMapping(value = "/invent", method = RequestMethod.GET)
	public String invevent(@RequestParam int id, ModelMap modelmap, HttpServletResponse http)
			throws MessagingException {
		user logged = getcurrentuser();
		events event = eventsController.findOne(id);
		Set<events> listetester = logged.getEventSet();
		events naaa = eventsController.findOne(id);

		// WENN VOLL DANN NIEMAND NEILASSEN
		if (event.getmaxspieler() == event.getUser().size()) {
			return "redirect://localhost:7777/events.html";
		}

		if (listetester.contains(naaa)) {
			System.out.println("Bist schon Mitglied");
			modelmap.put("eventListe", logged.getEventSet());
			return "redirect://localhost:7777/events.html";
			// Kann nicht mehr passieren
		} else {
			listetester.add(event);
			logged.setEventSet(listetester);
			userController.save(logged);
			modelmap.put("eventListe", logged.getEventSet());
			// sendmail(naaa, "hinzugefügt");
			sendmail(event);
			return "redirect://localhost:7777/events.html";
		}

	}

	// Austragen
	@RequestMapping(value = "/expend", method = RequestMethod.GET)
	public String expend(@RequestParam int id, ModelMap modelmap, HttpServletResponse http,
			HttpServletResponse httpServletResponse) {
		user logged = getcurrentuser();
		try {
			sendmail_neg(eventsController.findOne(id));
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<events> neueliste = logged.getEventSet();
		neueliste.remove(eventsController.findOne(id));
		userController.save(logged);

		return "redirect://localhost:7777/myevents.html";

	}

	// get logged User
	public user getcurrentuser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		user logged = userController.findOne(Integer.parseInt(name));
		return logged;
	}

	// Mail Sender universal
	public void sendmail(events events, String inhalt) throws MessagingException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();
		JavaMailSenderImpl mailSender = ctx.getBean(JavaMailSenderImpl.class);
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);
		for (user a : events.getUserliste()) {
			mailMsg.setFrom("projektserverhsesslingen@gmail.com");
			mailMsg.setTo(a.getEmail());
			mailMsg.setSubject("Event wurde " + inhalt + "...");
			mailMsg.setText("EVENT mit der ID: " + events.getEventid() + " wurde " + inhalt + "....");
			mailSender.send(mimeMessage);
			System.out.println("---Done---");
		}

	}

	// Mail Sender eintragen

	public void sendmail(events events) throws MessagingException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();
		JavaMailSenderImpl mailSender = ctx.getBean(JavaMailSenderImpl.class);
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);
		for (user a : events.getUserliste()) {
			mailMsg.setFrom("SPOGROMA");
			mailMsg.setTo("projektserverhsesslingen@gmail.com");
			mailMsg.setSubject("Event " + events.getEventid() + " hat ein neues Mitglied");
			mailMsg.setText("Benutzer mit der ID:" + getcurrentuser().getUserid() + "hat sich in das Event mit der ID: "
					+ events.getEventid() + "eingetragen");
			mailSender.send(mimeMessage);
			System.out.println("---Done---");
		}

	}

	// Mail Sender austragen

	public void sendmail_neg(events events) throws MessagingException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();
		JavaMailSenderImpl mailSender = ctx.getBean(JavaMailSenderImpl.class);
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);
		for (user a : events.getUserliste()) {
			mailMsg.setFrom("SPOGROMA");
			mailMsg.setTo("projektserverhsesslingen@gmail.com");
			mailMsg.setSubject("Event " + events.getEventid() + " hat einen Mitglied weniger");
			mailMsg.setText("Benutzer mit der ID:" + getcurrentuser().getUserid()
					+ "hat sich aus den Event mit der ID: " + events.getEventid() + " ausgetragen");
			mailSender.send(mimeMessage);
			System.out.println("---Done---");
		}

	}

	// Daten Datum
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		dateFormat.setLenient(false);
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}

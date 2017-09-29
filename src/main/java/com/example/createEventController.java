package com.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class createEventController {

	@Autowired
	public eventDao eventsController;
	@Autowired
	public sportartenDao sportController;
	@Autowired
	public userDao userController;

	@GetMapping("/createevent")
	public String eventForm(Model model) {
		int id = getcurrentuser().getUserid();
		if (id != 1 && id != 4) {
			return "login";
		}
		events neu = new events();
		Iterable<sportarten> sportart = sportController.findAll();
		model.addAttribute("events", neu);// hjlökjh
		model.addAttribute("sportarten", sportart);
		int ids=getcurrentuser().getUserid();
		if (ids <= 1) {
			return "create_event_admin";
		} else
			return "create_event";
	}

	@PostMapping("/createevent")
	public String Submit(@ModelAttribute events event, @DateTimeFormat(pattern = "dd/MM/yy HH:mm") Date fromDate,
			ModelMap modelmap) {
		event.setMaxspieler(setmax(event.getsportart()));
		eventsController.save(event);
		String abo = event.getsportart();
		try {
			sendmail(abo);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Iterable<events> backwards=eventsController.findAll();
		modelmap.addAttribute("eventListe", event);

		int id = getcurrentuser().getUserid();
		if (id == 1) {
			return "/eventdetails_admin";
		} else if (id == 4) {
			return "/eventdetails";
		} else
			return "/eventdetails_user";

	}

	@InitBinder // 1
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
		dateFormat.setLenient(false);
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	// Setzen MaxSpieler
	public int setmax(String y) {
		sportarten neu = sportController.findOne(y);
		return neu.getMaxspieler();
	}

	// sending mail universal
	public void sendmail(String x) throws MessagingException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();
		JavaMailSenderImpl mailSender = ctx.getBean(JavaMailSenderImpl.class);
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);
		Iterable<user> empfänger = userController.findAll();
		for (user a : empfänger) {
			if (a.getAbos().contains(sportController.findOne(x))) {
				mailMsg.setFrom("projektserverhsesslingen@gmail.com");
				mailMsg.setTo(a.getEmail());
				mailMsg.setSubject("Neues Event in  " + x);
				mailMsg.setText(
						"Neues Event von " + getcurrentuser().getVorname() + " " + getcurrentuser().getNachname()
								+ " mit der Userid: " + getcurrentuser().getUserid() + " wurde erstellt...");
				mailSender.send(mimeMessage);
			}
		}
		mailMsg.setFrom("projektserverhsesslingen@gmail.com");
		mailMsg.setTo("projektserverhsesslingen@gmail.com");
		mailMsg.setSubject("Neues Event in  " + x);
		mailMsg.setText("Neues Event von " + getcurrentuser().getVorname() + " " + getcurrentuser().getNachname()
				+ " mit der Userid: " + getcurrentuser().getUserid() + " wurde erstellt...");
		mailSender.send(mimeMessage);
		System.out.println("---Done---");
	}

	// get logged User
	public user getcurrentuser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		user logged = userController.findOne(Integer.parseInt(name));
		return logged;
	}

}

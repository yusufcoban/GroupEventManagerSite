package com.example;

import java.util.Date;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class contactController {

	@Autowired
	public userDao userController;
	@Autowired
	public modelDao modelDao;

	@RequestMapping("/contact")
	public String showAbout(ModelMap modelmap) {
		model model = new model();
		modelmap.addAttribute("model", model);
		int id = getcurrentuser().getUserid();
		if (id == 1) {
			return "/contact_admin";
		} else if (id == 4) {
			return "/contact";
		} else
			return "/contact_user";

	}

	@RequestMapping("/contactregister")
	public String contacteveryone(ModelMap modelmap) {
		model model = new model();
		modelmap.addAttribute("model", model);
		return "/contactnon";
	}

	@PostMapping("/sendmailtoadmin")
	public String Submit(@ModelAttribute model model, @DateTimeFormat(pattern = "dd/MM/yy HH:mm") Date fromDate,
			ModelMap modelmap) {

		try {
			sendmail(model.getInhalt());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.setInhalt(null);
		return "contact";
	}

	@PostMapping("/sendmailtoadminnon")
	public String Submitnon(@ModelAttribute model model, @DateTimeFormat(pattern = "dd/MM/yy HH:mm") Date fromDate,
			ModelMap modelmap) {

		try {
			sendmailnon(model.getInhalt());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.setInhalt(null);
		return "login";
	}

	// sending mail universal
	public void sendmailnon(String x) throws MessagingException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();
		JavaMailSenderImpl mailSender = ctx.getBean(JavaMailSenderImpl.class);
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);
		mailMsg.setFrom("projektserverhsesslingen@gmail.com");
		mailMsg.setTo("projektserverhsesslingen@gmail.com");
		mailMsg.setSubject("Kontaktanfrage...von nicht registrierten User");
		mailMsg.setText(" Nachrichtinhalt lautet " + x);
		mailSender.send(mimeMessage);
		System.out.println("---Done---");
	}

	// sending mail universal
	public void sendmail(String x) throws MessagingException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();
		JavaMailSenderImpl mailSender = ctx.getBean(JavaMailSenderImpl.class);
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);
		mailMsg.setFrom("projektserverhsesslingen@gmail.com");
		mailMsg.setTo("projektserverhsesslingen@gmail.com");
		mailMsg.setSubject("Kontaktanfrage...von " + getcurrentuser().getVorname() + getcurrentuser().getNachname());
		mailMsg.setText(" Nachricht von " + getcurrentuser().getVorname() + " " + getcurrentuser().getNachname()
				+ " mit der Userid: " + getcurrentuser().getUserid() + "Inhalt der Nachricht lautet: " + x);
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

package com.example;

import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class aboController<ActionResult, JavaScriptResult> {

	@Autowired
	public userDao userController;
	@Autowired
	public modelDao modelDao;
	@Autowired
	public sportartenDao sportController;

	@RequestMapping("/abos")
	public String abo(ModelMap modelmap) {
		model model = new model();
		modelmap.addAttribute("model", model);
		return "/contact";
	}

	// Kategorien abonnieren
	@RequestMapping("/abo")
	public String aboplus(@RequestParam String sportart){
		user logged = getcurrentuser();
		Set<sportarten> abos = logged.getAbos();
		
		
		if(sportController.exists(sportart)){
			sportarten selectedone = sportController.findOne(sportart);
			if (abos.contains(selectedone)) {
				System.out.println("schnon abonniert");
				      return "redirect:/category";
				  
			} else {
				//wenn man sich erfolgreich abonniert
				abos.add(selectedone);
				logged.setAbos(abos);
				userController.save(logged);
				System.out.println("abonnieren erfolgreich");
				return "redirect:/category";
			}
		}
		else {
			System.out.println("Diese Kategorie gibt es nicht");
		      return "Warum du versuchen:D";
		   
		}
		
	}

	@RequestMapping("/deabo")
	public String abominus(@RequestParam String sportart) throws MessagingException {
		user logged = getcurrentuser();
		Set<sportarten> abos = logged.getAbos();
		sportarten selectedone = sportController.findOne(sportart);
		abos.remove(selectedone);
		userController.save(getcurrentuser());
		return "redirect:/category";
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

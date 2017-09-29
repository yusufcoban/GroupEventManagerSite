package com.example;

import java.util.Collection;
import java.util.HashSet;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("useradmin")
public class adminconfig {

	@Autowired
	public eventDao eventsController;
	@Autowired
	public userDao userController;
	@Autowired
	public sportartenDao sportController;

	// get all User
	@RequestMapping(method = RequestMethod.GET)
	public String user(ModelMap modelmap) {
		if (getcurrentuser().getUserid()!=1){
			return "login";
		}
		Iterable<user> userlist = userController.findAll();
		Set<user> kompletteListe = new HashSet<user>((Collection) userlist);
		modelmap.put("userliste", kompletteListe);
		return "userlist";
	}

	@RequestMapping("/delete")
	public String getinfos(@RequestParam int id, ModelMap modelmap) throws MessagingException {
		user usr = userController.findOne(id);
		System.out.println(usr.getUserid() + "gelöscht!");
		sendmail_delete(usr);
		userController.delete(id);
		return "redirect://localhost:7777/useradmin.html";
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

	// Notification Delete User
	public void sendmail_delete(user user) throws MessagingException {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();
		JavaMailSenderImpl mailSender = ctx.getBean(JavaMailSenderImpl.class);
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);
		mailMsg.setFrom("wieslaufannahsesslingen@gmail.com");
		mailMsg.setTo(user.getEmail());
		mailMsg.setSubject("Account wurde gelöscht...");
		mailMsg.setText("Sie wurden leider gelöscht...");
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

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class changeUserController {
	@Autowired
	public eventDao eventsController;
	@Autowired
	public userDao userController;
	@Autowired
	public sportartenDao sportController;

	// Change UserDetails Start
	@RequestMapping("/changeuser")
	public String change_user(@RequestParam int id, ModelMap model) throws MessagingException {
		user usr = userController.findOne(id);
		if (getcurrentuser().getUserid() != 1) {
			return "login";
		}
		model.addAttribute("user", usr);
		return "change_user";
	}

	// Changed UserDetails Save
	@RequestMapping(value = "/change_submit_user", method = RequestMethod.POST)
	public String change_submit(@ModelAttribute user usr, ModelMap modelmap) throws MessagingException {
		userController.save(usr);
		Iterable<user> userlist = userController.findAll();
		Set<user> kompletteListe = new HashSet<user>((Collection) userlist);
		modelmap.put("userliste", kompletteListe);
		return "userlist";
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

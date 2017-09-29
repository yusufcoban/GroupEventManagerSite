package com.example;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	public userDao userDao;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/home").permitAll().anyRequest().authenticated().and().formLogin()
				.defaultSuccessUrl("/myevents.html", true).loginPage("/login").permitAll().and().logout().permitAll();
		http.authorizeRequests().antMatchers("/resources/**").permitAll().anyRequest().permitAll();
	}

	// Liste Benutzer laden
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		List<user> dutchlauf = (List<user>) userDao.findAll();
		for (int i = 0; i <= userDao.count(); i++) {
			for (user a : dutchlauf) {
				String username = "" + a.getUserid();
				auth.inMemoryAuthentication().withUser(username).password(a.getUserpwd()).roles("USER");
			}

		}

	}
}

package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class userController {
	private List<user> userListe = null;

	@Autowired
	public userDao userDDao;
	List<user> userListereturn;

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<user> getAll() {

		userListe = (List<user>) userDDao.findAll();
		return userListe;
	}

	// @RequestMapping(value = "/allhtml", method = RequestMethod.GET)
	// public String getAllhtml(ModelMap modelmap) {
	// modelmap.put("userliste", userDDao.findAll());
	// return "user";
	// }

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public void test() throws Exception {
		// String user =http.sessionManagement().toString();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName(); // get logged in username
		System.out.println(name);
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public int count() {
		int anzahl = (int) userDDao.count();
		return anzahl;
	}

	@GetMapping("/{id}")
	public ResponseEntity getuser(@PathVariable("id") Integer id) {
		user user = userDDao.findOne(id);
		if (user == null) {
			return new ResponseEntity("Kein Benutzer mit der ID= " + id, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteall", method = RequestMethod.GET)
	public ResponseEntity deleteall() {
		userDDao.deleteAll();
		return new ResponseEntity("Alle weg....!", null);

	}

	@GetMapping("/delete/{id}")
	public ResponseEntity deleteuser(@PathVariable("id") Integer id) {
		user user = userDDao.findOne(id);
		if (user == null) {
			return new ResponseEntity("Kein Benutzer mit der ID= " + id, HttpStatus.NOT_FOUND);
		} else
			userDDao.delete(id);
		return new ResponseEntity(user.getVorname() + "  " + user.getNachname() + "wurde gelöscht !", HttpStatus.OK);
	}

	// @RequestMapping(value = "/id", method = RequestMethod.GET)
	// public user id() {
	// user user = userDDao.findOne(1);
	// return user;
	// }

	// @RequestMapping(value = "/id", method = RequestMethod.GET)
	// public user id(int choose) {
	// user user = userDDao.findOne(choose);
	// return user;
	// }
	//
	// @RequestMapping(value = "/user/", method = RequestMethod.GET)
	// public ResponseEntity<List<user>> listAllusers() {
	// List<user> users = (List<user>) userDDao.findAll();
	// if (users.isEmpty()) {
	// return new ResponseEntity<List<user>>(HttpStatus.NO_CONTENT);// You
	// // many
	// // decide
	// // to
	// // return
	// // HttpStatus.NOT_FOUND
	// }
	// return new ResponseEntity<List<user>>(users, HttpStatus.OK);
	// }
	//
	// // -------------------Retrieve Single
	// // user--------------------------------------------------------
	//
	// @RequestMapping(value = "/user/{id}", method = RequestMethod.GET,
	// produces = MediaType.APPLICATION_JSON_VALUE)
	// public ResponseEntity<user> getuser(@PathVariable("id") long id) {
	// System.out.println("Fetching user with id " + id);
	// user user = userDDao.findOne((int) id);
	// if (user == null) {
	// System.out.println("user with id " + id + " not found");
	// return new ResponseEntity<user>(HttpStatus.NOT_FOUND);
	// }
	// return new ResponseEntity<user>(user, HttpStatus.OK);
	// }
	//
	// // ------------------- Delete a user
	// // --------------------------------------------------------
	//
	// @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	// public ResponseEntity<user> deleteuser(@PathVariable("id") long id) {
	// System.out.println("Fetching & Deleting user with id " + id);
	//
	// user user = userDDao.findOne((int) id);
	// if (user == null) {
	// System.out.println("Unable to delete. user with id " + id + " not
	// found");
	// return new ResponseEntity<user>(HttpStatus.NOT_FOUND);
	// }
	//
	// userDDao.delete((int) id);
	// return new ResponseEntity<user>(HttpStatus.NO_CONTENT);
	// }
	//
	// // ------------------- Delete All users
	// // --------------------------------------------------------
	//
	// @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
	// public ResponseEntity<user> deleteAllusers() {
	// System.out.println("Deleting All users");
	//
	// userDDao.deleteAll();
	// return new ResponseEntity<user>(HttpStatus.NO_CONTENT);
	// }

}

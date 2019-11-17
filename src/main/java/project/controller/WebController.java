package project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.beans.User;
import project.repository.ItemRepository;
import project.repository.UserRepository;

@Controller
public class WebController {
	@Autowired
	UserRepository ur;
	ItemRepository ir;
	
	@GetMapping("/login")public String login(@RequestParam(name="username", required=true) String userName,
			@RequestParam(name="password", required=false) String password, Model model) {
		
		//checks if user did not input items into from
		if(userName.equals(null) || password.equals(null)) {
			return "index";
		} 
		
		//looks up user name in database
		List<User> userlookup = ur.findByUsername(userName);
		
		//returns to login if no items is found in database
		if(userlookup.isEmpty() == true) {
			return "index";
		} 
		
		//forward to mainpage if password is found
		//only one unique user, so it will be first item in list
		if(userlookup.get(0).getPassword().equals(password)) {
			model.addAttribute("username", userlookup.get(0).getUsername());
			return "mainpage";
		} else {
			return "index";
		}
				
		}
	
	@GetMapping("/logout")public String logout(Model model) {
		//returns to login page
		return "index";
		}

	@GetMapping("/adduser")public String addUser(@RequestParam(name="newusername", required=true) String newUserName,
			@RequestParam(name="newpassword", required=true) String newPassword, Model model) {
		
		//check for empty fields
		if(newUserName.equals(null) || newPassword.equals(null)) {
			return "index";
		}
				
		//search database for name to check for duplicates
		List<User> newUserDatabase = ur.findByUsername(newUserName);
		
		//check for duplicates or process login
		if(newUserDatabase.isEmpty() == false) {
			return "index";
		} else {
			User newUser = new User();
			newUser.setUsername(newUserName);
			newUser.setPassword(newPassword);
			ur.save(newUser);
			model.addAttribute("username", newUser.getUsername());
			return "mainpage";
		}
		
		}
	
}

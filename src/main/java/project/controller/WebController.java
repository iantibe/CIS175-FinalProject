package project.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import project.beans.BorrowItem;
import project.beans.Item;
import project.beans.User;
import project.repository.BorrowItemRepository;
import project.repository.ItemRepository;
import project.repository.UserRepository;

@Controller
public class WebController {
	@Autowired
	UserRepository ur;
	
	@Autowired
	ItemRepository ir;
	
	@Autowired
	BorrowItemRepository bir;

	@PostMapping("/login")
	public String userLogin(@ModelAttribute User u, Model model, HttpServletRequest request) {
		String sendTo = "index";
		
		List<User> users = ur.findAll();
		
		for (User x : users) {
			if (x.getUsername().equals(u.getUsername())) {
				if (x.getPassword().equals(u.getPassword())) {
					sendTo = "mainpage";
					
					// saving the current user's id and username to session
					request.getSession().setAttribute("logId", x.getId());
					request.getSession().setAttribute("currentUsername", x.getUsername());
					
					// NOTE:  In WebController, whenever you need to access the currently logged in user's ID or username from session
					// in your method, include the argument "HttpServletRequest request" to get the current HttpRequest
					// and then save the attribute as userId = request.getSession().getAttribute("logId");
					// or userName = request.getSession().getAttribute("currentUsername");
					// this will allow any other WebController method to access the currently logged in user's
					// id or password.
					
					List<BorrowItem> userItems = bir.findByLender(x);
					System.out.println("User Items Size: " + userItems.size());
					model.addAttribute("userItems", userItems);
				}
			}
		}
		
		return sendTo;
	}

	@GetMapping("/logout")
	public String logout(Model model, HttpServletRequest request) {
		// destroys current session
		request.getSession().invalidate();
		
		// returns to login page
		return "index";
	}

	@PostMapping("/adduser")
	public String addUser(Model model, HttpServletRequest request) {
		String newUserName = request.getParameter("newusername");
		String newPassword = request.getParameter("newpassword");
		
		// check for empty fields
		if (newUserName.equals(null) || newPassword.equals(null)) {
			return "index";
		}

		// search database for name to check for duplicates
		List<User> newUserDatabase = ur.findByUsername(newUserName);

		// check for duplicates or process login
		if (newUserDatabase.isEmpty() == false) {
			return "index";
		} else {
			User newUser = new User();
			newUser.setUsername(newUserName);
			newUser.setPassword(newPassword);
			ur.save(newUser);
			
			// get new user's ID from the DB for session
			newUserDatabase = ur.findByUsername(newUserName);
			for (User x : newUserDatabase) {
				if (x.getUsername().equals(newUserName)) {
					// add new user's ID and username to session.
					request.getSession().setAttribute("logId", x.getId());
					request.getSession().setAttribute("currentUsername", x.getUsername());
				}
			}
			
			List<Item> allItems = ir.findAll();
			model.addAttribute("allItems", allItems);
			
			return "mainpage";
		}

	}
	
	//This is a find all for the items
	//The pages need a bit of adjusting so that they go through this before
	//returning things to the mainpage where it displays the items
	@GetMapping("/viewAll")
	public String viewAllBooks(Model model) {
		model.addAttribute("allItems", ir.findAll());
		return "mainpage";
	}
	
	//I also need to talk with you guys as to how things actually count as borrowed, so this is it for now!

}

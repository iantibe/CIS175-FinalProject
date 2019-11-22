package project.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import project.beans.BorrowItem;
import project.beans.User;
import project.beans.UserItem;
import project.repository.BorrowItemRepository;
import project.repository.ItemRepository;
import project.repository.UserItemRepository;
import project.repository.UserRepository;

@Controller
public class WebController {
	@Autowired
	UserRepository ur;

	@Autowired
	ItemRepository ir;

	@Autowired
	UserItemRepository uir;

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

					// NOTE: In WebController, whenever you need to access the currently logged in
					// user's ID or username from session
					// in your method, include the argument "HttpServletRequest request" to get the
					// current HttpRequest
					// and then save the attribute as userId =
					// request.getSession().getAttribute("logId");
					// or userName = request.getSession().getAttribute("currentUsername");
					// this will allow any other WebController method to access the currently logged
					// in user's
					// id or password.

					List<UserItem> userItems = uir.findByUser(x);
					System.out.println("User Items Size: " + userItems.size());
					model.addAttribute("userItems", userItems);

					List<BorrowItem> itemsLentOut = new ArrayList<BorrowItem>();
					for (UserItem i : userItems) {
						if (bir.findByUserItem(i) != null) {
							BorrowItem temp = bir.findByUserItem(i);
							if (temp.getReturnDate() == null) {
								itemsLentOut.add(bir.findByUserItem(i));
							}
						}
					}
					System.out.println("items lent out: " + itemsLentOut.size());
					model.addAttribute("lentItems", itemsLentOut);
				}
			}
		}

		return sendTo;
	}

	// Whenever you want to return back to the "Mainpage" landing page, use the link
	// "/home". That will call
	// this method. It will pull the user from session, and populate teh appropriate
	// data needed on Mainpage.
	@GetMapping("/home")
	public String returnHome(Model model, HttpServletRequest request) {
		String sendTo = "index";

		if (request.getSession().getAttribute("logId") != null) {
			long uid = (long) request.getSession().getAttribute("logId");
			User currentUser = ur.findById(uid)
					.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + uid));
			sendTo = "mainpage";

			List<UserItem> userItems = uir.findByUser(currentUser);
			model.addAttribute("userItems", userItems);

			List<BorrowItem> itemsLentOut = new ArrayList<BorrowItem>();
			for (UserItem i : userItems) {
				if (bir.findByUserItem(i) != null) {
					BorrowItem temp = bir.findByUserItem(i);
					if (temp.getReturnDate() == null) {
						itemsLentOut.add(bir.findByUserItem(i));
					}
				}
			}
			model.addAttribute("lentItems", itemsLentOut);
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

					List<UserItem> userItems = uir.findByUser(x);
					model.addAttribute("userItems", userItems);

					List<BorrowItem> itemsLentOut = new ArrayList<BorrowItem>();
					for (UserItem i : userItems) {
						if (bir.findByUserItem(i) != null) {
							BorrowItem temp = bir.findByUserItem(i);

							if (temp.getReturnDate() == null) {
								itemsLentOut.add(temp);
							}
						}
					}
					model.addAttribute("lentItems", itemsLentOut);
				}
			}

			return "mainpage";
		}

	}

	// This is a find all for the items
	// The pages need a bit of adjusting so that they go through this before
	// returning things to the mainpage where it displays the items
	@GetMapping("/viewAll")
	public String viewAllBooks(Model model) {
		model.addAttribute("allItems", ir.findAll());
		return "mainpage";
	}

	// I also need to talk with you guys as to how things actually count as
	// borrowed, so this is it for now!
	// see the "returnHome" method above.

	@GetMapping("/return")
	public String returnAnItem(Model model, HttpServletRequest request) {
		String sendTo = "index";

		if (request.getSession().getAttribute("logId") != null) {
			long uid = (Long) request.getSession().getAttribute("logId");
			User currentUser = ur.findById(uid)
					.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + uid));
			sendTo = "returnItemPage";

			List<UserItem> userItems = uir.findByUser(currentUser);
			List<BorrowItem> itemsLentOut = new ArrayList<BorrowItem>();
			for (UserItem i : userItems) {
				if (bir.findByUserItem(i) != null) {
					BorrowItem temp = bir.findByUserItem(i);

					if (temp.getReturnDate() == null) {
						itemsLentOut.add(temp);
					}

				}
			}
			model.addAttribute("lentItems", itemsLentOut);
		}
		System.out.println("Sending to: " + sendTo);
		return sendTo;
	}

	@GetMapping("/returnItem/{id}")
	public String returnItemById(@PathVariable("id") long id, Model model, HttpServletRequest request) {
		String sendTo = "returnItemDatePage";
		BorrowItem theItem = bir.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		model.addAttribute("currentItem", theItem);
		return sendTo;
	}

	@PostMapping("/saveReturnItem")
	public String saveReturnItem(@ModelAttribute BorrowItem i, Model model, HttpServletRequest request) {
		String sendTo = "index";
		System.out.println("");
		System.out.println("BorrowItem ID: " + i.getId());
		System.out.println("");

		if (i != null) {
			BorrowItem updateThis = bir.findById(i.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + i.getId()));
			updateThis.setReturnDate(i.getReturnDate());
			sendTo = "returnItemPage";
			bir.save(updateThis);
			User currentUser = updateThis.getUserItem().getUser();
			List<UserItem> userItems = uir.findByUser(currentUser);
			List<BorrowItem> itemsLentOut = new ArrayList<BorrowItem>();

			for (UserItem ui : userItems) {
				if (bir.findByUserItem(ui) != null) {
					BorrowItem temp = bir.findByUserItem(ui);

					if (temp.getReturnDate() == null) {
						itemsLentOut.add(temp);
					}

				}
			}
			model.addAttribute("lentItems", itemsLentOut);
		}
		return sendTo;
	}
}

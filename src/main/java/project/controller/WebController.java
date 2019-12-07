package project.controller;

import java.io.IOException;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


import project.beans.AddItem;
import project.beans.BorrowItem;
import project.beans.BorrowRating;
import project.beans.Image;
import project.beans.Item;
import project.beans.User;
import project.beans.UserItem;
import project.beans.UserRatingModel;
import project.repository.BorrowItemRepository;
import project.repository.BorrowRatingRepository;
import project.repository.ImageRepository;
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
	
	@Autowired
	BorrowRatingRepository brr;
	
	@Autowired
	ImageRepository img;

	@PostMapping("/login")
	public String userLogin(@ModelAttribute User u, Model model, HttpServletRequest request) {
		String sendTo = "index";

		List<User> users = ur.findAll();

		for (User x : users) {
			if (x.getUsername().equals(u.getUsername())) {
				if (x.getPassword().equals(u.getPassword())) {
					sendTo = "mainpage";
					
					// get logged in user's borrower rating to save in session for use later
					double userRating = brr.findByUserID((int)x.getId());
					
					// saving the current user's id and username to session
					request.getSession().setAttribute("logId", x.getId());
					request.getSession().setAttribute("currentUsername", x.getUsername());
					request.getSession().setAttribute("myRating", userRating);
					request.getSession().setAttribute("currentDate", LocalDate.now());

					List<UserItem> userItems = uir.findByUser(x);
					model.addAttribute("userItems", userItems);

					List<BorrowItem> itemsLentOut = new ArrayList<BorrowItem>();
					for (UserItem i : userItems) {
						if (bir.findByUserItem(i) != null) {
							List<BorrowItem> userBorrowItems = bir.findByUserItem(i);
							
							if (userBorrowItems != null) {
								for (BorrowItem bi : userBorrowItems) {
									if (bi.getReturnDate() == null) {
										itemsLentOut.add(bi);
									}
								}
							}
						}
					}
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
				List<BorrowItem> userBorrowItems = bir.findByUserItem(i);
				if (userBorrowItems != null) {
					BorrowItem temp = null;
					for (BorrowItem bi : userBorrowItems) {
						if (bi.getReturnDate() == null) {
							if (bi.getBorrowDate() != null) {
								itemsLentOut.add(bi);
							}
						}
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
					request.getSession().setAttribute("myRating", 0.0);
					request.getSession().setAttribute("currentDate", LocalDate.now());

					List<UserItem> userItems = uir.findByUser(x);
					model.addAttribute("userItems", userItems);

					List<BorrowItem> itemsLentOut = new ArrayList<BorrowItem>();
					for (UserItem i : userItems) {
						List<BorrowItem> userBorrowItems = bir.findByUserItem(i);
						if (userBorrowItems != null) {
							BorrowItem temp = null;
							for (BorrowItem bi : userBorrowItems) {
								if (bi.getReturnDate() == null) {
									itemsLentOut.add(bi);
								}
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
			User currentUser = ur.findById(uid).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + uid));
			sendTo = "returnItemPage";

			List<UserItem> userItems = uir.findByUser(currentUser);
			List<BorrowItem> itemsLentOut = new ArrayList<BorrowItem>();
			for (UserItem i : userItems) {
				List<BorrowItem> userBorrowItems = bir.findByUserItem(i);
				if (userBorrowItems != null) {
					BorrowItem temp = null;
					for (BorrowItem bi : userBorrowItems) {
						if (bi.getReturnDate() == null) {
							itemsLentOut.add(bi);
						}
					}
	
				}
			}
			model.addAttribute("lentItems", itemsLentOut);
		}
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

		if (i != null) {
			BorrowItem updateThis = bir.findById(i.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + i.getId()));
			updateThis.setReturnDate(i.getReturnDate());
			sendTo = "rateBorrower";
			bir.save(updateThis);
			//BorrowItem newBorrow = new BorrowItem();
			//newBorrow.setBorrower(updateThis.getBorrower());
			//newBorrow.setUserItem(updateThis.getUserItem());
			//bir.save(newBorrow);
			
			BorrowRating newRating = new BorrowRating ();
			newRating.setBorrowItem(updateThis);
			model.addAttribute("borrowrating", newRating);
			
		}
		return sendTo;
	}
	
	@PostMapping("/saveRating")
	public String saveRating(@ModelAttribute BorrowRating br, Model model, HttpServletRequest request) {
		brr.save(br);
		return returnAnItem(model, request);
	}
	
	@GetMapping("/lend")
	public String lendItem(Model model, HttpServletRequest request) {
		Long currentUserId = (Long) request.getSession().getAttribute("logId");
		User currentUser = ur.findById(currentUserId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + currentUserId));
		
		//remove yourself from list. You can lend stuff to yourself
		List<User> userList = ur.findAll();
		userList.remove(userList.indexOf(currentUser));
		
		model.addAttribute("user", userList);
			
		List<UserItem> currentUserItems = uir.findByUser(currentUser);
		List<UserItem> displayList = new ArrayList<UserItem>();
				
		for (UserItem i : currentUserItems) {
			if (bir.findByUserItemAndReturnDate(i, null).isEmpty()) {
				displayList.add(i);
			}
		}
				
		model.addAttribute("item", displayList);
		
		return "borrow";
	}
	
	@PostMapping("/lend")
	public String saveLentItem(Model model, HttpServletRequest request) {
		Long name = Long.parseLong(request.getParameter("user"));
		Long item = Long.parseLong(request.getParameter("item"));
		
		User lendee = ur.findById(name).orElseThrow(() -> new IllegalArgumentException("Invalid user:" + name));
		UserItem itemToLend = uir.findById(item).orElseThrow(() -> new IllegalArgumentException("Invalid "));
				
		String lendDateString = request.getParameter("lenddate");
		String dueDateString = request.getParameter("duedate");
		
		LocalDate dueDate = LocalDate.parse(dueDateString);
		LocalDate lendDate = LocalDate.parse(lendDateString);
		
		BorrowItem itemToStore = new BorrowItem();
		itemToStore.setBorrowDate(lendDate);
		itemToStore.setBorrower(lendee);
		itemToStore.setDueDate(dueDate);
		itemToStore.setUserItem(itemToLend);
			
		bir.save(itemToStore);
		
		model.addAttribute("result", "Item Sucessfully Lent! Lend Another item or select item from menu");
		
		Long currentUserId = (Long) request.getSession().getAttribute("logId");
		User currentUser = ur.findById(currentUserId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + currentUserId));
		
		//remove yourself from list. You can left stuff to yourself
		List<User> userList = ur.findAll();
		userList.remove(userList.indexOf(currentUser));
		
		model.addAttribute("user", userList);
			
		List<UserItem> currentUserItems = uir.findByUser(currentUser);
		List<UserItem> displayList = new ArrayList<UserItem>();
				
		for (UserItem i : currentUserItems) {
			if (bir.findByUserItemAndReturnDate(i, null).isEmpty()) {
				displayList.add(i);
			}
		}
				
		model.addAttribute("item", displayList);
				
		return "borrow";
	}
	@GetMapping("/viewAllBorrowers")
	public String viewAllBorrowers(Model model) {
		List<User> userList = ur.findAll();
		List<UserRatingModel> userRatingList = new ArrayList<UserRatingModel>();
		System.out.println(brr.findByUserID(1));
		
		for(User u: userList ) {
			// for debugging
			//System.out.println("User ID: " + u.getId());
			//System.out.println("User Rating: " + brr.findByUserID((int)u.getId()));
			//System.out.println("");
			
			UserRatingModel temp = new UserRatingModel();
			temp.setUserName(u.getUsername());
			String tempRating = "";
			tempRating += brr.findByUserID((int)u.getId());
			temp.setRating(tempRating);
			userRatingList.add(temp);
		}
		
		model.addAttribute("allItems", userRatingList);
		return "viewAllBorrowers";
	}
	
	//Manage items and choose who the item belongs to
	@GetMapping("/manage")
	public String addNewItems(Model model, HttpServletRequest request) {
		Item i = new Item();
		model.addAttribute("newItems", i);
		return "manage";
	}
	
	@PostMapping("/manage")
	public String addNewItems(@RequestParam("image") MultipartFile fileToGet, @ModelAttribute Item i,  Model model, HttpServletRequest request)  {
				
		ir.save(i);
		
		//start image code
		Item itemToGet = ir.findByName(i.getName());
		Image imageToSave = new Image();
		try {
			imageToSave.setImage(fileToGet.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imageToSave.setImageid(itemToGet.getId());
		
		//comment out save function until image code is fully implemented
		//img.save(imageToSave);
		//end image code
		
		//Grabs current user and saves that as the owner of the item in the table
		Long currentUserId = (Long) request.getSession().getAttribute("logId");
		User currentUser = ur.findById(currentUserId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + currentUserId));
		
		UserItem itemOwner = new UserItem();
		itemOwner.setItem(i);
		itemOwner.setUser(currentUser);
		uir.save(itemOwner);
		
		
		return returnHome(model, request);
	}
	
	@GetMapping("/image/{imageid}")
	@ResponseBody
	public byte[] getImage(@PathVariable("imageid") long imageid ) {
		Image item = img.findByImageid(imageid);
			
		return item.getImage();
	}
	
}

package com.Impex.loginRegistratin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.Impex.loginRegistratin.entities.User;
import com.Impex.loginRegistratin.repos.UserRepository;
import com.Impex.loginRegistratin.services.SecurityService;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private SecurityService securityService;

	@GetMapping("/")
	public String viewHomePage() {
		return "index";
	}

	@RequestMapping("/showReg")
	public String showRegistrationPage() {

		return "registerUser";
	}

	@RequestMapping(value = "registerUser", method = RequestMethod.POST)
	public String register(@ModelAttribute("user") User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
		return "login";

	}

	@RequestMapping("/loginPage")
	public String loginPage() {
		return "login";
	}

	@RequestMapping("/logout-success")
	public String logoutPage() {
		return "login";

	}

	/*@RequestMapping("/forgot")
	public String forgotPassword() {

		return "forgotPassword";
	}*/

	@RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
	public String updatePassword(@RequestParam("email") String email, @RequestParam("lastName") String LastName,
			ModelMap modelmap) {
		User user = userRepository.findByEmail(email);
		if (user.getLastName().equals(LastName)) {
			modelmap.addAttribute("user", user);
			return "login/updatePassword";
		} else {
			modelmap.addAttribute("msg", "Invalid User name or Password. Please Try Again!!!");
			return "login/forgotPassword";
		}

	}

	@RequestMapping(value = "updateUser", method = RequestMethod.POST)
	public String updateUser(@RequestParam("password") String password, @ModelAttribute("user") User user,
			ModelMap modelmap) {
		user.setPassword(password);
		userRepository.save(user);
		modelmap.addAttribute("msg", "updated Successfully");
		return "login/updatePassword";

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("email") String email, @RequestParam("password") String password,
			ModelMap modelMap) {
		boolean loginResponse = securityService.login(email, password);
		if (loginResponse) {
			if (email.equalsIgnoreCase("ruchisinghmahi@gmail.com")) {
				return "adminPage";
			} else {
				return "userPage";
			}
		}else {
		modelMap.addAttribute("msg", "Invalid User name or Password. Please Try Again!!!");
		return "login";
		}
	}
	
	@GetMapping("/403")
    public String error403() {
        return "login";
    }

}

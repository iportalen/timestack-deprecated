package com.iportalen.timestack.web.api.user;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iportalen.timestack.domain.dao.UserRepository;
import com.iportalen.timestack.domain.model.User;

@RestController
@RequestMapping("/api/client/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/me")
	public User me(HttpServletResponse res) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findByUsername(name);
	}
	
	@GetMapping("/updateEmail") 
	public User updateEmail(@RequestParam String newEmail) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(name);
		user.setEmail(newEmail);
		return userRepository.save(user);
	}
	
}
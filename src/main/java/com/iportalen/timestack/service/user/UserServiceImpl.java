package com.iportalen.timestack.service.user;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.iportalen.timestack.config.SecurityConfig.Roles;
import com.iportalen.timestack.domain.dao.RoleRepository;
import com.iportalen.timestack.domain.dao.UserRepository;
import com.iportalen.timestack.domain.model.Role;
import com.iportalen.timestack.domain.model.User;
import com.iportalen.timestack.service.shared.RegisterUserInit;

import lombok.extern.slf4j.Slf4j;

@Service(value = UserServiceImpl.NAME)
@Slf4j
public class UserServiceImpl implements UserService {

	public final static String NAME = "UserService";

	@Autowired
	private UserRepository userDao;

	@Autowired
	private RoleRepository roleRepository;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Trying to find user by username" + username);
		UserDetails userDetails = userDao.findByUsername(username);
		if (userDetails == null)
			return null;

		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		for (GrantedAuthority role : userDetails.getAuthorities()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}

		return new org.springframework.security.core.userdetails.User(userDetails.getUsername(),
				userDetails.getPassword(), userDetails.getAuthorities());
	}
	
	@Override
	public UserDetails loadUserByFirebaseUid(String firebaseUid) throws UsernameNotFoundException {
		log.info("Trying to find user by firebase uid" + firebaseUid);
		UserDetails userDetails = userDao.findByFirebaseUid(firebaseUid).orElseGet(null);
		if (userDetails == null)
			return null;

		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		for (GrantedAuthority role : userDetails.getAuthorities()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}

		return new org.springframework.security.core.userdetails.User(userDetails.getUsername(),
				userDetails.getPassword(), userDetails.getAuthorities());
	}

	@Override
	@Transactional
	@Secured(value = Roles.ROLE_ANONYMOUS)
	public User registerUser(RegisterUserInit init) {

		Optional<User> userLoaded = userDao.findByFirebaseUid(init.getUid());

		if (!userLoaded.isPresent()) {
			User newUser = new User();
			newUser.setUsername(init.getPhonenumber());
			newUser.setPhonenumber(init.getPhonenumber());
			newUser.setFirebaseUid(init.getUid());
			newUser.setAuthorities(getUserRoles());
			newUser.setPassword(UUID.randomUUID().toString()); // random password for now
			userDao.save(newUser);
			log.info("registerUser -> user created");
			return newUser;
		} else {
			log.info("registerUser -> user exists");
			return userLoaded.get();
		}
	}

	@PostConstruct
	public void init() {

//		if (userDao.count() == 0) {
//			User adminEntity = new User();
//			adminEntity.setUsername("admin");
//			adminEntity.setPassword("admin");
//			adminEntity.setEmail("savic.prvoslav@gmail.com");
//
//			adminEntity.setAuthorities(getAdminRoles());
//			userDao.save(adminEntity);
//
//			User userEntity = new User();
//			userEntity.setUsername("user1");
//			userEntity.setPassword("user1");
//			userEntity.setEmail("savic.prvoslav@gmail.com");
//			userEntity.setAuthorities(getUserRoles());
//
//			userDao.save(userEntity);
//		}
	}

	private List<Role> getUserRoles() {
		return Collections.singletonList(getRole(Roles.ROLE_USER));
	}

	/**
	 * Get or create role
	 * @param authority
	 * @return
	 */
	private Role getRole(String authority) {
		Role adminRole = roleRepository.findByAuthority(authority);
		if (adminRole == null) {
			return new Role(authority);
		} else {
			return adminRole;
		}
	}

}

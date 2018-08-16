package com.iportalen.timestack.config.auth.firebase;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.iportalen.timestack.service.exception.FirebaseTokenInvalidException;
import com.iportalen.timestack.service.firebase.FirebaseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FirebaseFilter extends OncePerRequestFilter {

	private static String HEADER_NAME = "X-Authorization-Firebase";

	private FirebaseService firebaseService;

	public FirebaseFilter(FirebaseService firebaseService) {
		this.firebaseService = firebaseService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		log.info("onceper req");
		String xAuth = request.getHeader(HEADER_NAME);
		if (Strings.isBlank(xAuth)) {
			filterChain.doFilter(request, response);
			return;
		} else {
			try {
				FirebaseTokenHolder tokenHolder = firebaseService.parseToken(xAuth);

				String firebaseUid = tokenHolder.getUid();

				Authentication auth = new FirebaseAuthenticationToken(firebaseUid, tokenHolder);
				SecurityContextHolder.getContext().setAuthentication(auth);

				filterChain.doFilter(request, response);
			} catch (FirebaseTokenInvalidException e) {
				throw new SecurityException(e);
			}
		}
	}

}
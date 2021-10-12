package com.xoriant.xorpay.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.xoriant.xorpay.constants.ConfigConstants;

@Component
public class JwtResourceFilter extends OncePerRequestFilter {

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	MyUserDetailService myUserDetail;
	
	@Autowired
	ConfigConstants config;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String userName = null;
		String authorizationHeader = request.getHeader("Authorization");
		String jwtToken = null;

		try {
			/*Enumeration<String> e = request.getHeaderNames();
			while (e.hasMoreElements()) {
				logger.info("-Key->" + e.nextElement() + ":");
				if (e.nextElement() != null)
					logger.info("-Value->" + request.getHeader(e.nextElement()));
			}
			*/
			/*
			 * while(request.getHeaderNames().hasMoreElements()) {
			 * logger.info(request.getHeaderNames().nextElement()); }
			 */
			if (authorizationHeader != null && authorizationHeader.startsWith("XorToken ")) {
				jwtToken = authorizationHeader.substring(9);
				//logger.info(jwtToken);
				userName = jwtUtil.extractUsername(jwtToken);
				//logger.info(userName);
			}
			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.myUserDetail.loadUserByUsername(userName);
				if (jwtUtil.validateToken(jwtToken, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

					response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
					response.setHeader("Access-Control-Allow-Credentials", "true");
					response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
					response.setHeader("Access-Control-Allow-Headers",
							"Content-Type, Accept, X-Requested-With, remember-me");

				}
			}

		} catch (Exception e) {
			logger.info(e);
			request.setAttribute("Exception", e);
		}
		filterChain.doFilter(request, response);

	}

}

package com.xoriant.xorpay.security;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService{

	public String secondParam = null;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//logger.info("Returning User............>" + username + "," + secondParam);
		return new User(username,secondParam, new ArrayList<>());
	}
}
 
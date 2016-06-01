package br.com.authspringsecurity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.authspringsecurity.domain.UserAuthentication;
import br.com.authspringsecurity.userlogged.UserLogged;

@RestController
public class UserController {
	
	// @formatter:off
    @RequestMapping(
            value = "/api/get",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    // @formatter:on
    public @ResponseBody ResponseEntity<UserAuthentication> login(@UserLogged UserAuthentication user) throws Exception {
    	return new ResponseEntity<UserAuthentication>(user, HttpStatus.OK);
    }
}

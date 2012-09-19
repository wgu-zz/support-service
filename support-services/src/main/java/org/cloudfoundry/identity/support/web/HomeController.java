/*
 * Cloud Foundry 2012.02.03 Beta
 * Copyright (c) [2009-2012] VMware, Inc. All Rights Reserved.
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product includes a number of subcomponents with
 * separate copyright notices and license terms. Your use of these
 * subcomponents is subject to the terms and conditions of the
 * subcomponent's license, as noted in the LICENSE file.
 */
package org.cloudfoundry.identity.support.web;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.Map;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.apache.commons.codec.digest.DigestUtils;
import org.cloudfoundry.identity.uaa.openid.OpenIdUserDetails;

@Controller
public class HomeController {

	private String token = null;
	private String returnTo = null;
	private JdbcTemplate jdbcTemplate;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	protected long getTime() {
		Date date = new Date();

		return date.getTime();
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setReturnTo(String returnTo) {
		this.returnTo = returnTo;
	}

	@RequestMapping("/")
	public String home(HttpServletRequest request, Principal principal, Map<String, Object> model) throws IOException {
		OpenIdUserDetails activeUser = (OpenIdUserDetails) ((Authentication) principal).getPrincipal();

		String name;

		//Convert ms to seconds
		Long time = getTime();
		Long timeStamp = time/1000;

		if (activeUser.getName().equals(activeUser.getEmail().replace("@", " ")) ||
			activeUser.getName().equals(activeUser.getEmail() + ' ' + activeUser.getEmail())) {
			name = activeUser.getEmail().split("@")[0];
		} else {
			name = activeUser.getName();
		}

		String hash = null;
		String userIpAddress = request.getHeader("x-cluster-client-ip");

		//Saving user info for history
		try {
			jdbcTemplate.update("INSERT INTO users (EMAIL, IP, TIMESTMP) VALUES (?, ?, ?)", activeUser.getEmail(), userIpAddress, new Timestamp(time));
		} catch (Exception e) {
			logger.warn("NOT ABLE TO INSERT HISTORY RECORD: " + e.getMessage());
		}

		hash = DigestUtils.md5Hex(name + activeUser.getEmail() + activeUser.getId() + token + timeStamp);

		model.put("name", name);
		model.put("email", activeUser.getEmail());
		model.put("external_id", activeUser.getId());
		model.put("timestamp", timeStamp);
		model.put("hash", hash);

		//Clearing the Session because the support-signon app doesn't need it
		org.springframework.security.core.context.SecurityContextHolder.clearContext();

		HttpSession session = request.getSession(false);

		if (session != null) {
			session.invalidate();
		}

		logger.info("The user's name is: " + name + " and email address: " + activeUser.getEmail() + " and external-id: " + activeUser.getId());

		return "redirect:" + returnTo;
	}
}
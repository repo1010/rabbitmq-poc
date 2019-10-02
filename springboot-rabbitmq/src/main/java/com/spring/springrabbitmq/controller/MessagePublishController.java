package com.spring.springrabbitmq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.springrabbitmq.model.Employee;
import com.spring.springrabbitmq.service.MessagePublishService;

@RestController
public class MessagePublishController {

	@Autowired
	MessagePublishService messagePublisher;

	@GetMapping("/publish")
	public String publishMessage(@RequestParam String empname, @RequestParam String address) {

		Employee emp = new Employee(empname, address);
		messagePublisher.send(emp);
		return "Message Sent Successfully";
	}
}

package com.spring.springrabbitmq.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.spring.springrabbitmq.model.Employee;

@Service
public class MessagePublishService {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Value("${app.rabbitmq.routingkey}")
	private String routingKey;

	@Value("${app.rabbitmq.exchange}")
	private String exchang;

	public void send(Employee emp) {
		rabbitTemplate.convertAndSend(exchang, routingKey, emp);
		System.out.println("Send Message: " + emp);
	}

}

package com.spring.topicexchange;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@SpringBootApplication
public class TopicExchangeMessageProducerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TopicExchangeMessageProducerApplication.class, args);
	}

	private static final String EXCHANGE_NAME = "topic-exchange";

	@Override
	public void run(String... args) throws Exception {

		ConnectionFactory cf = new ConnectionFactory();
		cf.setHost("localhost");

		try (Connection conn = cf.newConnection(); Channel channel = conn.createChannel()) {

			channel.exchangeDeclare(EXCHANGE_NAME, "topic");

			String message = getMessage(args).get(0);
			List<String> severities = getSeverity(args);

			for (String sev : severities) {
				channel.basicPublish(EXCHANGE_NAME, sev, null, message.getBytes());
				System.out.println("Published Message :" + message + "with routing key :" + sev);
			}
		}
	}

	private List<String> getSeverity(String[] args) {

		return Arrays.asList(args).stream().filter(e -> !args[args.length - 1].equals(e)).collect(Collectors.toList());
	}

	private List<String> getMessage(String[] args) {

		return Arrays.asList(args).stream().filter(e -> args[args.length - 1].equals(e)).collect(Collectors.toList());
	}

}

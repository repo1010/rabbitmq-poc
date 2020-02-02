package com.spring.directexchangemsgproducer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@SpringBootApplication
public class DirectExchangeMsgProducerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DirectExchangeMsgProducerApplication.class, args);
	}

	private static final String EXCHNAGE_NAME = "direct_4_exchange";

	@Override
	public void run(String... args) throws Exception {

		ConnectionFactory cf = new ConnectionFactory();
		cf.setHost("localhost");

		try (Connection conn = cf.newConnection(); Channel channel = conn.createChannel()) {
			channel.exchangeDeclare(EXCHNAGE_NAME, "direct");

			List<String> severities = getSeverity(args);
			String message = getMessage(args);

			/*
			 * severities.stream().map( s -> {channel.basicPublish(EXCHNAGE_NAME, s, null,
			 * messages.getBytes("UTF-8")); System.out.println("Published Message :"+
			 * message + "With Severity "+ s); });
			 */

			for (String s : severities) {
				channel.basicPublish(EXCHNAGE_NAME, s, null, message.getBytes("UTF-8"));
				System.out.println("Published Message :" + message + "With Severity " + s);
			}

		}

	}

	private List<String> getSeverity(String[] args) {

		List<String> severities = Arrays.asList(args).stream().filter(e -> !args[args.length - 1].equals(e))
				.collect(Collectors.toList());

		return severities;
	}

	private String getMessage(String[] args) {

		String message = Arrays.asList(args).stream().filter(e -> args[args.length - 1].equals(e)).findFirst().get();
		return message;
	}

}

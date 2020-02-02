package com.spring.directexchangemsgconsumer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

@SpringBootApplication
public class DirectExchangeMsgConsumerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DirectExchangeMsgConsumerApplication.class, args);
	}

	private static final String EXCHNAGE_NAME = "direct_4_exchange";

	@Override
	public void run(String... args) throws Exception {

		ConnectionFactory cf = new ConnectionFactory();
		cf.setHost("localhost");

		Connection conn = cf.newConnection();
		Channel channel = conn.createChannel();

		String queueName = channel.queueDeclare().getQueue();

		if (args.length < 1) {
			System.out.println("Required severity for Direct Exchange ");
			System.exit(1);
		}


		for (String s : args) {
			System.out.println("Severity: "+s);
			channel.queueBind(queueName, EXCHNAGE_NAME, s);
		}

		System.out.println("Waiting for Message to arrieve");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String m = new String(delivery.getBody(), "UTF-8");
			System.out
					.println("Received Message: " + m + " With Routing key :" + delivery.getEnvelope().getRoutingKey());
		};

		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
	}

	
}

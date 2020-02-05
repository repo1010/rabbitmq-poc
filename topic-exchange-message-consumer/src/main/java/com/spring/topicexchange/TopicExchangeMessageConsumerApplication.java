package com.spring.topicexchange;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

@SpringBootApplication
public class TopicExchangeMessageConsumerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TopicExchangeMessageConsumerApplication.class, args);
	}

	private static final String EXCHANGE_NAME = "topic-exchange";

	@Override
	public void run(String... args) throws Exception {

		ConnectionFactory cf = new ConnectionFactory();
		cf.setHost("localhost");

		Connection conn = cf.newConnection();
		Channel channel = conn.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "topic");

		String queueName = channel.queueDeclare().getQueue();

		if (args.length < 1) {
			System.out.println("Require message and routing key");
			System.exit(1);
		}

		for (String sev : args) {
			channel.queueBind(queueName, EXCHANGE_NAME, sev);

		}

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String m = new String(delivery.getBody(), "UTF-8");
			System.out.println("Consumed Message : " + m + "With key :" + delivery.getEnvelope().getRoutingKey());
		};

		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
	}

}

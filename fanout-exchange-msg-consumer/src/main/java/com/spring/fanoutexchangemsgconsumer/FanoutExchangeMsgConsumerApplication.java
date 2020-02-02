package com.spring.fanoutexchangemsgconsumer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

@SpringBootApplication
public class FanoutExchangeMsgConsumerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FanoutExchangeMsgConsumerApplication.class, args);
	}

	private static final String EXCHANGE_NAME = "fanout_3_exchange";

	@Override
	public void run(String... args) throws Exception {
		ConnectionFactory cf = new ConnectionFactory();
		cf.setHost("localhost");

		Connection conn = cf.newConnection();
		Channel channel = conn.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

		String QueueName = channel.queueDeclare().getQueue();
		System.out.println("QueueName :" + QueueName);

		channel.queueBind(QueueName, EXCHANGE_NAME, "");

		System.out.println("Waiting for the message");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String msg = new String(delivery.getBody(), "UTF-8");
			System.out.println("Received Message :" + msg);
		};

		channel.basicConsume(QueueName, false, deliverCallback, consumerTag -> {

		});
	}

}

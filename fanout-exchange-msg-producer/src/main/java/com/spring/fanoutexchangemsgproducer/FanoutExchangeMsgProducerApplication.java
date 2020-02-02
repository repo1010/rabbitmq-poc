package com.spring.fanoutexchangemsgproducer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@SpringBootApplication
public class FanoutExchangeMsgProducerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FanoutExchangeMsgProducerApplication.class, args);
	}

	private static final String EXCHANGE_NAME = "fanout_3_exchange";

	@Override
	public void run(String... args) throws Exception {

		ConnectionFactory cf = new ConnectionFactory();
		cf.setHost("localhost");

		try (Connection conn = cf.newConnection(); Channel channel = conn.createChannel()) {

			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
			System.out.println("args length:" + args.length);

			String msg = args.length < 1 ? "Hello I m default message" : String.join("", args);

			channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes("UTF-8"));

			System.out.println("Published Message :" + msg);

		}

	}

}

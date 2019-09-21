package com.rabbitmq.customconsumer;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.lang.SerializationUtils;

public class QueueProducer extends EndPoint {

	public QueueProducer(String endpointName) throws IOException {
		super(endpointName);
	}

	public static void main(String[] args) throws IOException {
		new QueueProducer("test-durable-queue");

		for (int i = 0; i < 100000; i++) {
			HashMap message = new HashMap();
			message.put("message number :", i);
			sendMessage(message);
			System.out.println("Message Number " + i + " sent.");
		}
	}

	public static void sendMessage(Serializable object) throws IOException {
		channel.basicPublish("", endPointName, null, SerializationUtils.serialize(object));
	}
}
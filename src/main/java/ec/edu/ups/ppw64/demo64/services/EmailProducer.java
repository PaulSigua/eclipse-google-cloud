package ec.edu.ups.ppw64.demo64.services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.google.gson.Gson;

public class EmailProducer {
    private final static String QUEUE_NAME = "email_queue";

    public static void sendEmailToQueue(Email email) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // Cambia a la dirección de tu servidor RabbitMQ
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            String message = new Gson().toJson(email);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}

class Email {
    private String to;
    private String subject;
    private String body;
    
    // Getters y Setters

    public Email(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }
}

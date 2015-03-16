package cgl.iotrobots;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import cgl.iotrobots.slam.core.app.LaserScan;

import java.io.ByteArrayInputStream;

public class LaserScanReceiver {

    private static final String EXCHANGE_NAME = "simbard_laser";

    public static void main(String[] argv)
            throws java.io.IOException,
            java.lang.InterruptedException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "test.test.laser_scan");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        Kryo kryoLaserReading = new Kryo();
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            byte [] body = delivery.getBody();
            LaserScan ls = kryoLaserReading.readObject(new Input(new ByteArrayInputStream(body)), LaserScan.class);

            System.out.println(" [x] Received laser scan");
            System.out.println(ls.getString());
        }
    }
}
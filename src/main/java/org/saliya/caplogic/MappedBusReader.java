package org.saliya.caplogic;

import io.mappedbus.MappedBusMessage;
import io.mappedbus.sample.object.ObjectReader;
import io.mappedbus.sample.object.PriceUpdate;

public class MappedBusReader {
    public static void main(String[] args) {
        ObjectReader reader = new ObjectReader();
        reader.run();
    }

    public void run() {
        try {
            io.mappedbus.MappedBusReader
                reader = new io.mappedbus.MappedBusReader("E:\\test-message", 2000000L, 12);
            reader.open();

            PriceUpdate priceUpdate = new PriceUpdate();

            MappedBusMessage message = null;

            while (true) {
                if (reader.next()) {
                    boolean recovered = reader.hasRecovered();
                    int type = reader.readType();
                    switch (type) {
                        case PriceUpdate.TYPE:
                            message = priceUpdate;
                            break;
                        default:
                            throw new RuntimeException("Unknown type: " + type);
                    }
                    reader.readMessage(message);
                    System.out.println("Read: " + message + ", hasRecovered=" + recovered);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

package org.saliya.resourceloading;

import java.io.InputStream;

public class ResourceLoader {
    public static void main(String[] args) {
        InputStream inputStream = ResourceLoader.class.getClassLoader().getResourceAsStream("RectC");
        System.out.println(inputStream == null);
    }
}

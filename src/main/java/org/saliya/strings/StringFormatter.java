package org.saliya.strings;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringFormatter {
    public static void main(String[] args) {
        String str = String.format("==== MDS %1$s ==== Option:%2$s PointCount_Global:%3$s ==== Run %4$s %5$s == File %6$s == %7$s ==== ", 1, 2, 3, 4, 5, 6, 7);
        System.out.println(str);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = formatter.format(new Date());
        System.out.println(s);
        System.out.println(new Date().toString().replace(' ','_'));

        float f = 10.3456789f;
        System.out.println(String.format("%.4E",f));

        System.out.println(String.format("%1$-15s%2$-15s%3$-8s%4$-12s%5$-8s%6$-12s%7$-12s%8$-16s%9$-12s%10$-25s%11$-25s%12$-25s", "Duration(ms)", "HPDuration(ms)", "Thread#", "MPIperNode", "Node#", "Pt#/Process", "Pt#/Global", "Run Label", "Run Number", "DataFileName", "CurrentTime", "ProcessorName"));

        final String liTemplate = "<li><a href=\"%1$s\">%2$s</a></li>";
//        final String liTemplate = "%1$s%2$s";
        System.out.println(String.format(liTemplate, "wow", "hi"));

    }
}

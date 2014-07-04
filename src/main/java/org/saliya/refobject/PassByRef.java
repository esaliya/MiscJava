package org.saliya.refobject;

public class PassByRef {
    public static void main(String[] args) {
        DumberObject dumber = new DumberObject(100);
        DumbObject dumb = new DumbObject(10, dumber);

        System.out.println(dumb);
        RefObject<DumbObject> refDumb = new RefObject<>(dumb);
        doSomething(refDumb);
        System.out.println(dumb);
        dumb = refDumb.argValue;
        System.out.println(dumb);

        DumbObject dumb2 = null;
        RefObject<DumbObject> refDumb2 = new RefObject<>(null);
        doSomething2(refDumb2);
        dumb2 = refDumb2.argValue;
        System.out.println(dumb2);
    }

    public static void doSomething(RefObject<DumbObject> refDumb){
        refDumb.argValue = new DumbObject(5, refDumb.argValue.dumberObject);
    }

    public static void doSomething2(RefObject<DumbObject> refDumb){
        refDumb.argValue = new DumbObject(5, new DumberObject(50));
    }

}

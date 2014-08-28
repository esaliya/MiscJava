package exceptions;

public class PassingMethodsThatThrowExceptions {
    public interface IncrementerSignature{
        public int apply(String value) throws NotANumberException;
    }

    public static int addOne(String value) throws NotANumberException{
        int v = 0;
        try{
            v = Integer.parseInt(value);
        } catch (NumberFormatException e){
            throw new NotANumberException();
        }
        return v+1;
    }

    public static void increment(IncrementerSignature incrementer, String value) throws NotANumberException {
        System.out.println(incrementer.apply(value));
    }

    public static void main(String[] args) {
        try {
            increment(PassingMethodsThatThrowExceptions::addOne, "10");
        } catch (NotANumberException e) {
            e.printStackTrace();
        }
    }
}

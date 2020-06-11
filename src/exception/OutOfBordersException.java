package exception;

public class OutOfBordersException extends Exception{
    public OutOfBordersException(String field) {
        System.out.println("Ошибка! Значение для " + field + " у одного из элементов не попадает в допустимые границы.");
    }
}

package Exception;

public class OutOfBordersException extends Exception{
    public OutOfBordersException(String field) {
        System.out.println("Ошибка! Значение поля " + field + " одного из элементов имеет недопустимое значение.");
    }
}

package exception;

public class NullFieldException extends Exception{
    public NullFieldException(String field) {
        System.out.println("Ошибка! Значение поля " + field + " отсутствует.");
    }
}

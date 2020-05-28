package Exception;

public class NonUnicIdException extends Exception{
    public NonUnicIdException() {
        System.out.println("Ошибка! Id элемента не уникален.");
    }
}

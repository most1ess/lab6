package exception;

public class NonUnicKeyException extends Exception{
    public NonUnicKeyException() {
        System.out.println("Ошибка! Ключ элемента не уникален.");
    }
}

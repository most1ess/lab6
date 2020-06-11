package exception;

public class WrongKeyException extends Exception {
    public WrongKeyException() {
        System.out.println("Ошибка! Ключ не найден.");
    }
}

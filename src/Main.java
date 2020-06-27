import collection.Collection;
import parser.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Отключение программы.");
            }
        });

        try {
            boolean workingStatus = true;
            Scanner consoleIn = new Scanner(System.in);
            String currentCommand;
            String[] currentCommandSeparated;
            System.out.println("Добро пожаловать! Чтобы начать работу, введите нужную вам команду. \nДля просмотра списка команд введите help.");

            Collection collection = new Collection();

            Parser parser = new Parser();
            collection.setParser(parser);
            parser.parseFrom(collection);

            while (workingStatus) {
                currentCommand = consoleIn.nextLine();
                if(currentCommand.equals("exit")) {
                    workingStatus = false;
                } else {
                    currentCommandSeparated = currentCommand.trim().split(" ");
                    collection.commandDefiner(currentCommandSeparated);
                }
            }

            System.out.println("Работа программы завершена!");
        } catch (NoSuchElementException e) {
            System.out.println("Работа программы была принудительно прервана.");
        }
    }
}

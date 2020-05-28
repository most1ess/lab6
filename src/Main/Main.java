package Main;

import Collection.Collection;
import Parser.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static boolean workingStatus = true;

    public static void main(String[] args) {
        try {
            Scanner consoleIn = new Scanner(System.in);
            String currentCommand;
            String[] currentCommandSeparated;
            System.out.println("Добро пожаловать! Чтобы начать работу, введите нужную вам команду. \nДля просмотра списка команд введите help.");

            Parser.parseFrom();

            while (workingStatus) {
                currentCommand = consoleIn.nextLine();
                currentCommandSeparated = currentCommand.trim().split(" ");

                Collection.commandDefiner(currentCommandSeparated);
            }
        } catch (NoSuchElementException e) {
            System.out.println("Работа программы была принудительно прервана.");
        }
    }
}

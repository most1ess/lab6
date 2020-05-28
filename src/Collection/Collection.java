package Collection;

import Person.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.Map.Entry;
import Exception.*;
import Main.*;
import Parser.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Collection {

    public static TreeMap<Integer, Person> People = new TreeMap<>();
    private static java.time.LocalDateTime creationDate;
    private static boolean scriptExecutionStatus = false;
    private static byte executeIterationsCounter = 0;

    public static void setCreationDate(LocalDateTime creationDate) {
        Collection.creationDate = creationDate;
    }

    public static LocalDateTime getCreationDate() {
        return creationDate;
    }

    public static void commandDefiner(String[] input) {
        try {
            switch (input[0]) {
                case "help":
                    help();
                    break;
                case "info":
                    info();
                    break;
                case "show":
                    show();
                    break;
                case "insert":
                    insert(input[1]);
                    break;
                case "update":
                    update(input[1]);
                    break;
                case "remove_key":
                    remove_key(input[1]);
                    break;
                case "clear":
                    clear();
                    break;
                case "save":
                    save();
                    break;
                case "execute_script":
                    execute_script(input[1]);
                    executeIterationsCounter = 0;
                    break;
                case "exit":
                    exit();
                    break;
                case "remove_greater":
                    remove_greater(input[1]);
                    break;
                case "replace_if_lower":
                    replace_if_lower(input[1], input[2]);
                    break;
                case "remove_greater_key":
                    remove_greater_key(input[1]);
                    break;
                case "group_counting_by_creation_date":
                    group_counting_by_creation_date();
                    break;
                case "count_greater_than_location":
                    count_greater_than_location(input[1]);
                    break;
                case "filter_starts_with_name":
                    filter_starts_with_name(input[1]);
                    break;
                default:
                    scriptExecutionStatus = false;
                    System.out.println("Введенной вами команды не существует! \nДля просмотра списка команд введите help.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("У введенной вами команды не хватает аргументов!");
        } catch (NumberFormatException e) {
            System.out.println("У введенной вами команды неверный аргумент!");
        }
    }

    public static void help() {
        System.out.println("Список доступных команд:");
        System.out.println("help - вывод справки по доступным командам");
        System.out.println("info - вывод информации о коллекции");
        System.out.println("show - вывод всех элементов коллекции");
        System.out.println("insert <key> - добавление нового элемента с заданным ключом");
        System.out.println("remove_key <key> - удаление элемента по его ключу");
        System.out.println("clear - очистка коллекции");
        System.out.println("save - сохренение коллекции в файл");
        System.out.println("execute_script <file_name> - исполнение скрипта из указанного файла");
        System.out.println("exit - завершение программы (без записи в файл)");
        System.out.println("remove_greater <value> - удаление из коллекции всех элементов, в которых координата Х меньше введенной");
        System.out.println("replace_if_lower <key> <value> - замена значения роста по ключу, если новое меньше старого");
        System.out.println("remove_greater_key <value> - удаление всех элементов коллекции, в которых координата х превышает введенную");
        System.out.println("group_counting_by_creation_date - группировка данных по значению поля creation_date и вывод сгруппированных данных");
        System.out.println("count_greater_than_location <location> - вывод количества элементов, значение поля location которых больше заданного");
        System.out.println("filter_starts_with_name <name> - вывод всех элементов, значение поля name которых начинается с заданной подстроки");
    }

    public static void info() {
        System.out.println("Вид коллекции: TreeMap");
        System.out.println("Тип хранимых значений: Person");
        System.out.println("Количество элементов: " + Collection.People.size());
        System.out.println("Дата создания: " + creationDate.toString().replaceAll("T", " "));
    }

    public static void show() {
        Iterator<Entry<Integer, Person>> iterator = People.entrySet().iterator();
        while (iterator.hasNext()) {
            System.out.println();
            Map.Entry<Integer, Person> entry = iterator.next();
            System.out.println("Ключ элемента: " + entry.getKey());
            System.out.println("ID: " + entry.getValue().getId());
            System.out.println("Имя: " + entry.getValue().getName());
            System.out.println("Координата X: " + entry.getValue().getCoordinates().getX());
            System.out.println("Координата Y: " + entry.getValue().getCoordinates().getY());
            System.out.println("Дата создания: " + entry.getValue().getCreationDate());
            System.out.println("Рост: " + entry.getValue().getHeight());
            System.out.print("День Рождения: ");
            try {
                System.out.println(entry.getValue().getBirthday());
            } catch (NullPointerException e) {
                System.out.println("null");
            }
            System.out.println("Вес: " + entry.getValue().getWeight());
            System.out.println("Национальность: " + entry.getValue().getNationality());
            System.out.println("Местоположение по X: " + entry.getValue().getLocation().getX());
            System.out.println("Местоположение по Y: " + entry.getValue().getLocation().getY());
            System.out.println("Местоположение по Z: " + entry.getValue().getLocation().getZ());
        }
    }

    public static void insert(String key) {
        try {
            if(Integer.parseInt(key) <= 0) {
                throw new OutOfBordersException("id");
            }
            boolean isValueWritten = false;
            Person person = new Person();
            person.setId(Integer.valueOf(key));

            Iterator<Entry<Integer, Person>> iterator = People.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<Integer, Person> entry = iterator.next();
                if (entry.getKey().equals(person.getId())) {
                    throw new NonUnicIdException();
                }
            }

            Scanner parameterInput = new Scanner(System.in);


            while (!isValueWritten) {
                try {
                    System.out.print("Введите имя: ");
                    person.setName(parameterInput.nextLine());
                    if (person.getName().equals("")) {
                        throw new NullFieldException("name");
                    } else {
                        isValueWritten = true;
                    }
                } catch (NullFieldException e) {
                    System.out.println("Введенное вами значение неверно!");
                }
            }

            isValueWritten = false;
            Coordinates coordinates = new Coordinates();
            while (!isValueWritten) {
                try {
                    System.out.print("Введите координату Х: ");
                    coordinates.setX(Double.parseDouble(parameterInput.nextLine()));
                    isValueWritten = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Введенное вами значение неверно!");
                }
            }

            isValueWritten = false;
            while (!isValueWritten) {
                try {
                    System.out.print("Введите координату Y: ");
                    String yCoordinate = parameterInput.nextLine();
                    if(Double.parseDouble(yCoordinate) > 355) {
                        throw new OutOfBordersException("yCoordinate");
                    }
                    coordinates.setY(Double.parseDouble(yCoordinate));
                    isValueWritten = true;
                } catch (IllegalArgumentException | OutOfBordersException e) {
                    System.out.println("Введенное вами значение неверно!");
                }
            }
            person.setCoordinates(coordinates);

            isValueWritten = false;
            while (!isValueWritten) {
                try {
                    System.out.print("Введите рост: ");
                    String height = parameterInput.nextLine();
                    if(Integer.parseInt(height) <= 0) {
                        throw new OutOfBordersException("height");
                    }
                    person.setHeight(Integer.parseInt(height));
                    isValueWritten = true;
                } catch (IllegalArgumentException | OutOfBordersException e) {
                    System.out.println("Введенное вами значение неверно!");
                }
            }

            isValueWritten = false;
            while (!isValueWritten) {
                try {
                    System.out.print("Введите День Рождения в формате YYYY-MM-DD HH:MM: ");
                    String birthday = parameterInput.nextLine();
                    if (birthday.equals("")) {
                        person.setBirthday(null);
                    } else {
                        person.setBirthday(birthday);
                    }
                    isValueWritten = true;
                } catch (java.time.format.DateTimeParseException e) {
                    System.out.println("Введенное вами значение неверно!");
                }
            }

            isValueWritten = false;
            while (!isValueWritten) {
                try {
                    System.out.print("Введите вес: ");
                    String weight = parameterInput.nextLine();
                    if(Long.parseLong(weight) <= 0) {
                        throw new OutOfBordersException("weight");
                    }
                    person.setWeight(Long.parseLong(weight));
                    isValueWritten = true;
                } catch (IllegalArgumentException | OutOfBordersException e) {
                    System.out.println("Введенное вами значение неверно!");
                }
            }

            isValueWritten = false;
            while (!isValueWritten) {
                try {
                    System.out.print("Введите национальность (USA, FRANCE, ITALY): ");
                    person.setNationality(Country.valueOf(parameterInput.nextLine()));
                    isValueWritten = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Введенное вами значение неверно!");
                }
            }

            Location location = new Location();
            isValueWritten = false;
            while (!isValueWritten) {
                try {
                    System.out.print("Введите местоположение по Х: ");
                    location.setX(Long.valueOf(parameterInput.nextLine()));
                    isValueWritten = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Введенное вами значение неверно!");
                }
            }

            isValueWritten = false;
            while (!isValueWritten) {
                try {
                    System.out.print("Введите местоположение по Y: ");
                    location.setY(Float.parseFloat(parameterInput.nextLine()));
                    isValueWritten = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Введенное вами значение неверно!");
                }
            }

            isValueWritten = false;
            while (!isValueWritten) {
                try {
                    System.out.print("Введите местоположение по Z: ");
                    location.setZ(Double.parseDouble(parameterInput.nextLine()));
                    isValueWritten = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Введенное вами значение неверно!");
                }
            }
            person.setLocation(location);

            person.setCreationDate(LocalDateTime.now().withNano(0).withSecond(0).toString().replace("T", " "));

            People.put(Integer.valueOf(key), person);

            System.out.println("Элемент с ключом " + key + " успешно добавлен!");
        } catch (NonUnicIdException | OutOfBordersException e) {

        }
    }

    public static void update(String key) {
        try {
            if (!People.containsKey(Integer.valueOf(key))) {
                throw new WrongKeyException();
            }
            Person person = People.get(Integer.valueOf(key));

            People.remove(Integer.valueOf(key));

            Scanner parameterInput = new Scanner(System.in);
            byte fieldCounter = 0;
            Coordinates coordinates = person.getCoordinates();
            Location location = person.getLocation();
            while (fieldCounter < 10) {
                try {
                    switch (fieldCounter) {
                        case 0:
                            System.out.println("Текущее имя: " + person.getName());
                            System.out.print("Введите имя: ");
                            person.setName(parameterInput.nextLine());
                            fieldCounter++;
                            break;

                        case 1:
                            System.out.println("Текущая координата Х: " + coordinates.getX());
                            System.out.print("Введите координату Х: ");
                            coordinates.setX(Double.valueOf(parameterInput.nextLine()));
                            fieldCounter++;
                            break;

                        case 2:
                            try {
                                System.out.println("Текущая координата Y: " + coordinates.getY());
                                System.out.print("Введите координату Y: ");
                                String yCoordinate = parameterInput.nextLine();
                                if(Double.parseDouble(yCoordinate) > 355) {
                                    throw new OutOfBordersException("yCoordinate");
                                }
                                coordinates.setY(Double.parseDouble(yCoordinate));
                                person.setCoordinates(coordinates);
                                fieldCounter++;
                            } catch(OutOfBordersException e) {

                            }
                            break;

                        case 3:
                            try {
                                System.out.println("Текущий рост: " + person.getHeight());
                                System.out.print("Введите рост: ");
                                String height = parameterInput.nextLine();
                                if(Integer.parseInt(height) <= 0) {
                                    throw new OutOfBordersException("height");
                                }
                                person.setHeight(Integer.parseInt(height));
                                fieldCounter++;
                            } catch(OutOfBordersException e) {

                            }
                            break;

                        case 4:
                            try {
                                System.out.println("Текущий День Рождения в формате YYYY-MM-DD HH:MM: " + person.getBirthday());
                                System.out.print("Введите День Рождения: ");
                                String birthday = parameterInput.nextLine();
                                if (birthday.equals("")) {
                                    person.setBirthday(null);
                                } else {
                                    person.setBirthday(parameterInput.nextLine());
                                }
                                fieldCounter++;
                            } catch(DateTimeParseException e) {
                                System.out.println("Вы ввели неверное значение.");
                            }
                            break;

                        case 5:
                            try {
                                System.out.println("Текущий вес: " + person.getWeight());
                                System.out.print("Введите вес: ");
                                String weight = parameterInput.nextLine();
                                if(Long.parseLong(weight) <= 0) {
                                    throw new OutOfBordersException("weight");
                                }
                                person.setWeight(Long.parseLong(weight));
                                fieldCounter++;
                            } catch(OutOfBordersException e) {

                            }
                            break;

                        case 6:
                            System.out.println("Текущая национальность (USA, FRANCE, ITALY): " + person.getNationality());
                            System.out.print("Введите национальность: ");
                            person.setNationality(Country.valueOf(parameterInput.nextLine()));
                            fieldCounter++;
                            break;

                        case 7:
                            System.out.println("Текущее местоположение по Х: " + location.getX());
                            System.out.print("Введите местоположение по Х: ");
                            location.setX(Long.valueOf(parameterInput.nextLine()));
                            fieldCounter++;
                            break;

                        case 8:
                            System.out.println("Текущее местоположение по Y: " + location.getY());
                            System.out.print("Введите местоположение по Y: ");
                            location.setY(Float.valueOf(parameterInput.nextLine()));
                            fieldCounter++;
                            break;

                        case 9:
                            System.out.println("Текущее местоположение по Z: " + location.getZ());
                            System.out.print("Введите местоположение по Z: ");
                            location.setZ(Double.valueOf(parameterInput.nextLine()));
                            person.setLocation(location);
                            fieldCounter++;
                            break;
                    }
                } catch(IllegalArgumentException e) {
                    System.out.println("Вы ввели неверное значение.");
                }
            }

            person.setCreationDate(LocalDateTime.now().withNano(0).withSecond(0).toString().replace("T", " "));

            People.put(Integer.valueOf(key), person);

            System.out.println("Элемент с ключом " + key + " успешно изменен!");
        } catch(WrongKeyException e) {

        }
    }

    public static void remove_key(String key) {
        try {
            if (!People.containsKey(Integer.valueOf(key))) {
                throw new WrongKeyException();
            }
            People.remove(Integer.valueOf(key));
            System.out.println("Элемент с ключом " + key + " успешно удален!");
        } catch (WrongKeyException e) {

        }
    }


    public static void clear() {
        try {
            People.tailMap(People.firstKey() - 1).clear();
            System.out.println("Коллекция была успешно очищена!");
        } catch (NoSuchElementException e) {
            System.out.println("Невозможно очистить коллекцию. Она уже чиста.");
        }
    }

    public static void save() {
        Parser.parseTo();
        System.out.println("Коллекция успешно сохранена в файл!");
    }

    public static void execute_script(String file) {
        File scriptFile = new File(file);
        if(scriptFile.canRead()) {
            executeIterationsCounter++;
            if (executeIterationsCounter > 5) {
                System.out.println("Скрипт зацикливается, поэтому его невозможно исполнить.");
                return;
            }
            scriptExecutionStatus = true;
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = bufferedReader.readLine();

                String[] separatedLine;

                while (line != null) {
                    separatedLine = line.trim().split(" ");
                    switch (separatedLine[0]) {
                        case "update":
                            People.remove(Integer.valueOf(separatedLine[1]));
                        case "insert":
                            Person person = new Person();

                            person.setId(Integer.valueOf(separatedLine[1]));
                            line = bufferedReader.readLine();
                            person.setName(line);
                            line = bufferedReader.readLine();
                            Coordinates coordinates = new Coordinates();
                            coordinates.setX(Double.parseDouble(line));
                            line = bufferedReader.readLine();
                            if (Double.parseDouble(line) > 355) {
                                throw new OutOfBordersException("yCoordinate");
                            }
                            coordinates.setY(Double.parseDouble(line));
                            person.setCoordinates(coordinates);
                            line = bufferedReader.readLine();
                            if (Integer.parseInt(line) <= 0) {
                                throw new OutOfBordersException("height");
                            }
                            person.setHeight(Integer.parseInt(line));
                            line = bufferedReader.readLine();
                            person.setBirthday(line);
                            line = bufferedReader.readLine();
                            if (Long.parseLong(line) <= 0) {
                                throw new OutOfBordersException("weight");
                            }
                            person.setWeight(Long.parseLong(line));
                            line = bufferedReader.readLine();
                            person.setNationality(Country.valueOf(line));
                            line = bufferedReader.readLine();
                            Location location = new Location();
                            location.setX(Long.valueOf(line));
                            line = bufferedReader.readLine();
                            location.setY(Float.parseFloat(line));
                            line = bufferedReader.readLine();
                            location.setZ(Double.parseDouble(line));
                            person.setLocation(location);

                            person.setCreationDate(java.time.LocalDateTime.now().withNano(0).withSecond(0).toString().replace("T", " "));

                            People.put(person.getId(), person);
                            break;
                        default:
                            commandDefiner(separatedLine);
                            break;
                    }
                    line = bufferedReader.readLine();
                }

                if (scriptExecutionStatus) {
                    System.out.println("Скрипт из указанного файла успешно исполнен!");
                } else {
                    System.out.println("Скрипт из указанного файла не был исполнен корректно!");
                }
            } catch (FileNotFoundException e) {
                System.out.println("Файл не найден!");
                scriptExecutionStatus = false;
            } catch (IOException e) {
                System.out.println("Во время чтения файла произошла неизвестная ошибка.");
                scriptExecutionStatus = false;
            } catch (OutOfBordersException | NumberFormatException | java.time.format.DateTimeParseException | NullPointerException e) {
                System.out.println("Файл заполнен неверно.");
                scriptExecutionStatus = false;
            }
        } else {
            System.out.println("Невозможно исполнить скрипт из файла, так как файл недоступен для чтения.");
        }
    }

    public static void exit() {
        Main.workingStatus = false;
        System.out.println("Работа программы завершена.");
    }

    public static void remove_greater(String key) {
        double xCoordinate = Double.parseDouble(key);
        Iterator<Entry<Integer, Person>> iterator = People.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<Integer, Person> entry = iterator.next();
            if (entry.getValue().getCoordinates().getX() > xCoordinate) {
                People.remove(entry.getKey());
            }
        }

        System.out.println("Все искомые объекты успешно удалены!");
    }

    public static void replace_if_lower(String key, String value) {
        if (People.get(Integer.valueOf(key)).getHeight() > Integer.parseInt(value)) {
            People.get(Integer.valueOf(key)).setHeight(Integer.parseInt(value));
            System.out.println("Рост успешно изменен!");
        } else {
            System.out.println("Рост не был изменен, поскольку введенное значение не меньше текущего.");
        }
    }

    public static void remove_greater_key(String key) {
        Integer inputKey = Integer.valueOf(key);

        Iterator<Entry<Integer, Person>> iterator = People.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<Integer, Person> entry = iterator.next();
            if (entry.getKey() > inputKey) {
                People.remove(entry.getKey());
            }
        }

        System.out.println("Все объекты, ключ которых превышает " + inputKey + ", успешно удалены!");
    }

    public static void group_counting_by_creation_date() {
        Iterator<Entry<Integer, Person>> iterator = People.entrySet().iterator();
        TreeMap<java.time.LocalDateTime, Integer> creationDateGrouping = new TreeMap<>();
        java.time.LocalDateTime currentCreationDate;
        boolean currentDateFound = false;
        Integer tempValue;

        while (iterator.hasNext()) {
            Entry<Integer, Person> entry = iterator.next();
            currentCreationDate = entry.getValue().getCreationDate();

            Iterator<Entry<java.time.LocalDateTime, Integer>> iterator1 = creationDateGrouping.entrySet().iterator();
            while (iterator1.hasNext() & !currentDateFound) {
                Entry<java.time.LocalDateTime, Integer> entry1 = iterator1.next();

                if (entry1.getKey().equals(currentCreationDate)) {
                    currentDateFound = true;
                    tempValue = creationDateGrouping.get(currentCreationDate);
                    tempValue++;
                    creationDateGrouping.remove(currentCreationDate);
                    creationDateGrouping.put(currentCreationDate, tempValue);
                }
            }

            if (!currentDateFound) {
                creationDateGrouping.put(currentCreationDate, 1);
            } else {
                currentDateFound = false;
            }
        }

        System.out.println("Элементы коллекции успешно рассортированы по дате создания!");

        Iterator<Entry<java.time.LocalDateTime, Integer>> iterator1 = creationDateGrouping.entrySet().iterator();
        while (iterator1.hasNext()) {
            Entry<java.time.LocalDateTime, Integer> entry1 = iterator1.next();
            System.out.println(entry1.getKey() + ": " + entry1.getValue() + " элементов");
        }
    }

    public static void count_greater_than_location(String locationX) {
        Iterator<Entry<Integer, Person>> iterator = People.entrySet().iterator();
        Long inputLocationX = Long.valueOf(locationX);

        int amount = 0;

        while (iterator.hasNext()) {
            Entry<Integer, Person> entry = iterator.next();
            if (entry.getValue().getLocation().getX() > inputLocationX) {
                amount++;
            }
        }

        System.out.println("Количество элементов со значением поля х в поле location, больших чем " + locationX + ": " + amount);
    }

    public static void filter_starts_with_name(String name) {
        Iterator<Entry<Integer, Person>> iterator = People.entrySet().iterator();
        String regex = "^" + name + ".*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        boolean anythingFound = false;

        while (iterator.hasNext()) {
            Entry<Integer, Person> entry = iterator.next();
            matcher = pattern.matcher(entry.getValue().getName());
            if (matcher.lookingAt()) {
                System.out.println(entry.getValue().getId());
                anythingFound = true;
            }
        }
        if (anythingFound) {
            System.out.println("Элементы с указанными выше id имеют поле name, начинающееся с '" + name + "'");
        } else {
            System.out.println("Элементов не найдено!");
        }

    }
}


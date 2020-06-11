package collection;

import person.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import parser.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Collection {

    private TreeMap<Integer, Person> People = new TreeMap<>();

    private java.time.LocalDateTime creationDate;
    private boolean scriptExecutionStatus = false;
    private byte executeIterationsCounter = 0;

    /**
     * Выборка команды, исходя из ввода пользователя.
     *
     * @param input
     */
    public void commandDefiner(String[] input) {
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
                    removeKey(input[1]);
                    break;
                case "clear":
                    clear();
                    break;
                case "save":
                    save();
                    break;
                case "execute_script":
                    executeScript(input[1]);
                    executeIterationsCounter = 0;
                    break;
                case "remove_greater":
                    removeGreater(input[1]);
                    break;
                case "replace_if_lower":
                    replaceIfLower(input[1], input[2]);
                    break;
                case "remove_greater_key":
                    removeGreaterKey(input[1]);
                    break;
                case "group_counting_by_creation_date":
                    groupCountingByCreationDate();
                    break;
                case "count_greater_than_location":
                    countGreaterThanLocation(input[1]);
                    break;
                case "filter_starts_with_name":
                    filterStartsWithName(input[1]);
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

    /**
     * Список доступных команд.
     */
    public void help() {
        System.out.println("Список доступных команд:");
        System.out.println("help - вывод справки по доступным командам");
        System.out.println("info - вывод информации о коллекции");
        System.out.println("show - вывод всех элементов коллекции");
        System.out.println("insert <key> - добавление нового элемента с заданным ключом (ключ должен находиться в пределах от 1 до 50000");
        System.out.println("remove_key <key> - удаление элемента по его ключу");
        System.out.println("clear - очистка коллекции");
        System.out.println("save - сохренение коллекции в файл");
        System.out.println("execute_script <file_name> - исполнение скрипта из указанного файла");
        System.out.println("exit - завершение программы (без записи в файл)");
        System.out.println("remove_greater <value> - удаление из коллекции всех элементов, в которых координата Х меньше введенной");
        System.out.println("replace_if_lower <key> <value> - замена значения роста по ключу, если новое меньше старого (ключ должен находиться в пределах от 1 до 50000");
        System.out.println("remove_greater_key <value> - удаление всех элементов коллекции, в которых координата Х превышает введенную");
        System.out.println("group_counting_by_creation_date - группировка данных по значению поля creation_date и вывод сгруппированных данных");
        System.out.println("count_greater_than_location <location> - вывод количества элементов, значение поля location которых больше заданного");
        System.out.println("filter_starts_with_name <name> - вывод всех элементов, значение поля name которых начинается с заданной подстроки");
    }

    /**
     * Информация о коллекции.
     */
    public void info() {
        System.out.println("Вид коллекции: TreeMap");
        System.out.println("Тип хранимых значений: Person");
        System.out.println("Количество элементов: " + People.size());
        System.out.println("Дата создания: " + creationDate.toString().replaceAll("T", " "));
    }

    /**
     * Вывод элементов коллекции.
     */
    public void show() {
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

    /**
     * Вставка нового элемента в коллекцию.
     *
     * @param key
     */
    public void insert(String key) {
        if (Integer.parseInt(key) < 1 || Integer.parseInt(key) > 50000) {
            System.out.println("Ключ должен находиться в пределах от 1 до 50000. Попробуйте ввести другой ключ.");
        } else {
            Iterator<Map.Entry<Integer, Person>> iterator = People.entrySet().iterator();
            boolean isIdGood = true;
            while (iterator.hasNext() & isIdGood) {
                Map.Entry<Integer, Person> entry = iterator.next();
                if (entry.getKey().equals(Integer.valueOf(key))) {
                    isIdGood = false;
                }
            }
            if (isIdGood) {
                Insert insert = new Insert(key, People);

                insert.id();
                insert.name();
                insert.xCoordinate();
                insert.yCoordinate();
                insert.height();
                insert.birthday();
                insert.weight();
                insert.nationality();
                insert.xLocation();
                insert.yLocation();
                insert.zLocation();

                insert.genCreationDate();
                insert.applyToCollection();

                System.out.println("Объект успешно добавлен!");
            } else {
                System.out.println("Элемент с таким ключом уже есть в коллекции! Попробуйте ввести другой ключ.");
            }
        }
    }


    /**
     * Обновление элемента коллекции.
     *
     * @param key
     */
    public void update(String key) {

        if (!People.containsKey(Integer.valueOf(key))) {
            System.out.println("Элемента с таким ключом нет в коллекции! Попробуйте ввести другой ключ.");
        } else {
            Update update = new Update(key, People);

            update.name();
            update.xCoordinate();
            update.yCoordinate();
            update.height();
            update.birthday();
            update.weight();
            update.nationality();
            update.xLocation();
            update.yLocation();
            update.zLocation();

            update.genCreationDate();
            update.applyToCollection();

            System.out.println("Элемент с ключом " + key + " успешно изменен!");
        }
    }

    /**
     * Удаление элемента по ключу.
     *
     * @param key
     */
    public void removeKey(String key) {
        if (!People.containsKey(Integer.valueOf(key))) {
            System.out.println("Элемента с таким ключом нет в коллекции! Попробуйте ввести другой ключ.");
        } else {
            People.remove(Integer.valueOf(key));
            System.out.println("Элемент с ключом " + key + " успешно удален!");
        }
    }

    /**
     * Очистка коллекции.
     */
    public void clear() {
        try {
            People.tailMap(People.firstKey() - 1).clear();
            System.out.println("Коллекция была успешно очищена!");
        } catch (NoSuchElementException e) {
            System.out.println("Невозможно очистить коллекцию. Она уже чиста.");
        }
    }

    /**
     * Сохранение коллекции в файл.
     */
    public void save() {
        Parser.parseTo(People);
    }

    /**
     * Исполнение скрипта из файла.
     *
     * @param file
     */
    public void executeScript(String file) {
        File scriptFile = new File(file);
        boolean isScriptFileGood = true;
        if (scriptFile.canRead()) {
            executeIterationsCounter++;
            if (executeIterationsCounter > 5) {
                System.out.println("Скрипт зацикливается, поэтому его исполнение было остановлено.");
                return;
            }
            scriptExecutionStatus = true;
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = bufferedReader.readLine();

                String[] separatedLine;

                while (line != null & isScriptFileGood) {
                    separatedLine = line.trim().split(" ");
                    switch (separatedLine[0]) {
                        case "update":
                            People.remove(Integer.valueOf(separatedLine[1]));
                        case "insert":
                            Person person = new Person();
                            Randomizer idRandomizer = new Randomizer();

                            person.setId(idRandomizer.generateId(People));
                            line = bufferedReader.readLine();
                            person.setName(line);
                            line = bufferedReader.readLine();
                            Coordinates coordinates = new Coordinates();
                            coordinates.setX(Double.parseDouble(line));
                            line = bufferedReader.readLine();
                            if (Double.parseDouble(line) > 355) {
                                isScriptFileGood = false;
                            }
                            coordinates.setY(Double.parseDouble(line));
                            person.setCoordinates(coordinates);
                            line = bufferedReader.readLine();
                            if (Integer.parseInt(line) <= 0) {
                                isScriptFileGood = false;
                            }
                            person.setHeight(Integer.parseInt(line));
                            line = bufferedReader.readLine();
                            person.setBirthday(line);
                            line = bufferedReader.readLine();
                            if (Long.parseLong(line) <= 0) {
                                isScriptFileGood = false;
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

                            People.put(Integer.parseInt(separatedLine[1]), person);
                            break;
                        default:
                            commandDefiner(separatedLine);
                            break;
                    }
                    line = bufferedReader.readLine();
                }

                if (!scriptExecutionStatus) {
                    System.out.println("Скрипт из указанного файла не был исполнен корректно!");
                }
            } catch (FileNotFoundException e) {
                System.out.println("Файл не найден!");
                scriptExecutionStatus = false;
            } catch (IOException e) {
                System.out.println("Во время чтения файла произошла неизвестная ошибка.");
                scriptExecutionStatus = false;
            } catch (NumberFormatException | java.time.format.DateTimeParseException | NullPointerException e) {
                System.out.println("Файл со скриптом заполнен неверно.");
                scriptExecutionStatus = false;
            }
        } else {
            System.out.println("Невозможно исполнить скрипт из файла, так как файл недоступен для чтения.");
        }
        if(!isScriptFileGood) {
            System.out.println("Файл со скриптом заполнен неверно!");
        } else {
            System.out.println("Скрипт из указанного файла успешно исполнен!");
        }
    }

    /**
     * Удаление всех элементов коллекции, значение поля xCoordinate которых превышает текущее..
     *
     * @param key
     */
    public void removeGreater(String key) {
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

    /**
     * Замена роста на введенное значение, если оно больше текущего.
     *
     * @param key
     * @param value
     */
    public void replaceIfLower(String key, String value) {
        if (People.get(Integer.valueOf(key)).getHeight() > Integer.parseInt(value)) {
            People.get(Integer.valueOf(key)).setHeight(Integer.parseInt(value));
            System.out.println("Рост успешно изменен!");
        } else {
            System.out.println("Рост не был изменен, поскольку введенное значение не меньше текущего.");
        }
    }

    /**
     * Удаление всех элементов, ключ которых больше введенного.
     *
     * @param key
     */
    public void removeGreaterKey(String key) {
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

    /**
     * Сортировка по дате создания.
     */
    public void groupCountingByCreationDate() {
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

    /**
     * Подсчет количества элементов с большим значением поля location.
     *
     * @param location
     */
    public void countGreaterThanLocation(String location) {
        Iterator<Entry<Integer, Person>> iterator = People.entrySet().iterator();
        Long inputLocation = Long.valueOf(location);

        int amount = 0;
        boolean isComparingDone = false;

        Scanner scanner = new Scanner(System.in);
        while (!isComparingDone) {
            System.out.print("Введите поле объекта location, по которому вы хотите провести сравнение (x, y, z): ");
            String fieldInput = scanner.nextLine();
            switch (fieldInput) {
                case "x":
                    while (iterator.hasNext()) {
                        Entry<Integer, Person> entry = iterator.next();
                        if (entry.getValue().getLocation().getX() > inputLocation) {
                            amount++;
                        }
                    }
                    isComparingDone = true;
                    break;
                case "y":
                    while (iterator.hasNext()) {
                        Entry<Integer, Person> entry = iterator.next();
                        if (entry.getValue().getLocation().getY() > inputLocation) {
                            amount++;
                        }
                    }
                    isComparingDone = true;
                    break;
                case "z":
                    while (iterator.hasNext()) {
                        Entry<Integer, Person> entry = iterator.next();
                        if (entry.getValue().getLocation().getZ() > inputLocation) {
                            amount++;
                        }
                    }
                    isComparingDone = true;
                    break;
                default:
                    System.out.println("Введенное вами значение должно быть одним из (x, y, z).");
                    break;
            }
        }

        System.out.println("Количество элементов со значением поля location, больших чем " + location + ": " + amount);
    }

    /**
     * Поиск элементов с именем, начинающимся на введенные буквы.
     *
     * @param name
     */
    public void filterStartsWithName(String name) {
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

    public void setPeople(TreeMap<Integer, Person> people) {
        People = people;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
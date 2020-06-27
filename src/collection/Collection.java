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

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    private Parser parser = new Parser();
    private TreeMap<String, Person> People = new TreeMap<>();

    private java.time.LocalDateTime creationDate;
    private boolean scriptExecutionStatus = false;
    private byte executeIterationsCounter = 0;
    private byte outsideScriptCounter = 0;

    /**
     * Выборка команды, исходя из ввода пользователя.
     *
     * @param input - строка, введенная пользователем
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
                    outsideScriptCounter++;
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
        System.out.println("insert <key> - добавление нового элемента с заданным ключом");
        System.out.println("remove_key <key> - удаление элемента по его ключу");
        System.out.println("clear - очистка коллекции");
        System.out.println("save - сохренение коллекции в файл");
        System.out.println("execute_script <file_name> - исполнение скрипта из указанного файла");
        System.out.println("exit - завершение программы (без записи в файл)");
        System.out.println("remove_greater <value> - удаление из коллекции всех элементов, в которых координата Х меньше введенной");
        System.out.println("replace_if_lower <key> <value> - замена значения роста по ключу, если новое меньше старого");
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
        Iterator<Entry<String, Person>> iterator = People.entrySet().iterator();
        while (iterator.hasNext()) {
            System.out.println();
            Map.Entry<String, Person> entry = iterator.next();
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
            if (entry.getValue().getLocation() == null) {
                System.out.println("Местоположение: null");
            } else {
                System.out.println("Местоположение по X: " + entry.getValue().getLocation().getX());
                System.out.println("Местоположение по Y: " + entry.getValue().getLocation().getY());
                System.out.println("Местоположение по Z: " + entry.getValue().getLocation().getZ());
            }
        }
        if (People.size() == 0) {
            System.out.println("Коллекция пуста!");
        }
    }

    /**
     * Вставка нового элемента в коллекцию.
     *
     * @param key - ключ, введенный пользователем
     */
    public void insert(String key) {
        Iterator<Map.Entry<String, Person>> iterator = People.entrySet().iterator();
        boolean isIdGood = true;
        while (iterator.hasNext() & isIdGood) {
            Map.Entry<String, Person> entry = iterator.next();
            if (entry.getKey().equals(key)) {
                isIdGood = false;
            }
        }
        if (isIdGood) {
            Insert insert = new Insert(key, People);

            insert.id(parser);
            insert.name();
            insert.xCoordinate();
            insert.yCoordinate();
            insert.height();
            insert.birthday();
            insert.weight();
            insert.nationality();
            insert.location();

            insert.genCreationDate();
            insert.applyToCollection();

            System.out.println("Объект успешно добавлен!");
        } else {
            System.out.println("Элемент с таким ключом уже есть в коллекции! Попробуйте ввести другой ключ.");
        }
    }


    /**
     * Обновление элемента коллекции.
     *
     * @param key - ключ, введенный пользователем
     */
    public void update(String key) {

        if (!People.containsKey(key)) {
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
            update.location();

            update.genCreationDate();
            update.applyToCollection();

            System.out.println("Элемент с ключом " + key + " успешно изменен!");
        }
    }

    /**
     * Удаление элемента по ключу.
     *
     * @param key - ключ, введенный пользователем
     */
    public void removeKey(String key) {
        if (!People.containsKey(key)) {
            System.out.println("Элемента с таким ключом нет в коллекции! Попробуйте ввести другой ключ.");
        } else {
            People.remove(key);
            System.out.println("Элемент с ключом " + key + " успешно удален!");
        }
    }

    /**
     * Очистка коллекции.
     */
    public void clear() {
        try {
            People.clear();
            System.out.println("Коллекция успешно очищена.");
        } catch (NoSuchElementException e) {
            System.out.println("Невозможно очистить коллекцию. Она уже чиста.");
        }
    }

    /**
     * Сохранение коллекции в файл.
     */
    public void save() {
        Parser parser = new Parser();
        parser.parseTo(People);
    }

    /**
     * Исполнение скрипта из файла.
     *
     * @param file - имя файла, введенное пользователем
     */
    public void executeScript(String file) {
        File scriptFile = new File(file);
        boolean isScriptFileGood = true;
        if (outsideScriptCounter < 3) {
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
                                People.remove(separatedLine[1]);
                            case "insert":
                                Person person = new Person();
                                Randomizer idRandomizer = new Randomizer();

                                person.setId(idRandomizer.generateId());
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
                                if (line.equals("")) {
                                    person.setBirthday(null);
                                } else {
                                    person.setBirthday(line);
                                }
                                line = bufferedReader.readLine();
                                if (Long.parseLong(line) <= 0) {
                                    isScriptFileGood = false;
                                }
                                person.setWeight(Long.parseLong(line));
                                line = bufferedReader.readLine();
                                person.setNationality(Country.valueOf(line));
                                line = bufferedReader.readLine();
                                Location location = new Location();
                                if (line == null) {
                                    location = null;
                                } else {
                                    location.setX(Long.valueOf(line));
                                    line = bufferedReader.readLine();
                                    location.setY(Float.parseFloat(line));
                                    line = bufferedReader.readLine();
                                    location.setZ(Double.parseDouble(line));
                                }
                                person.setLocation(location);

                                person.setCreationDate(java.time.LocalDateTime.now().withNano(0).withSecond(0).toString().replace("T", " "));

                                People.put(separatedLine[1], person);
                                break;
                            default:
                                commandDefiner(separatedLine);
                                break;
                        }
                        line = bufferedReader.readLine();
                    }

                    if (!scriptExecutionStatus) {
                        System.out.println("Скрипт из указанного файла не был исполнен корректно!");
                        isScriptFileGood = false;
                        outsideScriptCounter = 0;
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Файл не найден!");
                    scriptExecutionStatus = false;
                    isScriptFileGood = false;
                } catch (IOException e) {
                    System.out.println("Во время чтения файла произошла ошибка.");
                    scriptExecutionStatus = false;
                    isScriptFileGood = false;
                } catch (NumberFormatException | java.time.format.DateTimeParseException | NullPointerException e) {
                    System.out.println("Файл со скриптом заполнен неверно.");
                    scriptExecutionStatus = false;
                    isScriptFileGood = false;
                }
            } else {
                System.out.println("Невозможно исполнить скрипт из файла, так как файл недоступен для чтения или не найден.");
                scriptExecutionStatus = false;
                isScriptFileGood = false;
            }
        } else {
            outsideScriptCounter = 0;
            System.out.println("Выполнение скрипта зацикливается, поэтому исполнения скрипта было остановлено.");
            scriptExecutionStatus = false;
            isScriptFileGood = false;
        }
        if (isScriptFileGood) {
            System.out.println("Скрипт из указанного файла успешно исполнен!");
        }
    }

    /**
     * Удаление всех элементов коллекции, значение поля xCoordinate которых превышает текущее..
     *
     * @param key - ключ, введенный пользователем
     */
    public void removeGreater(String key) {
        double xCoordinate = Double.parseDouble(key);
        Iterator<Entry<String, Person>> iterator = People.entrySet().iterator();
        boolean hasRemoved = false;

        while (iterator.hasNext()) {
            hasRemoved = true;
            Entry<String, Person> entry = iterator.next();
            if (entry.getValue().getCoordinates().getX() > xCoordinate) {
                People.remove(entry.getKey());
            }
        }
        if (hasRemoved) {
            System.out.println("Все искомые объекты успешно удалены!");
        } else {
            System.out.println("Опа! А удалять то нечего!");
        }
    }

    /**
     * Замена роста на введенное значение, если оно больше текущего.
     *
     * @param key   - ключ, введенный пользоваталем
     * @param value - значение, введенное пользователем
     */
    public void replaceIfLower(String key, String value) {
        if (People.get(key).getHeight() > Integer.parseInt(value)) {
            People.get(key).setHeight(Integer.parseInt(value));
            System.out.println("Рост успешно изменен!");
        } else {
            System.out.println("Рост не был изменен, поскольку введенное значение не меньше текущего.");
        }
    }

    /**
     * Удаление всех элементов, ключ которых больше введенного.
     *
     * @param key - ключ, введенный пользователем
     */
    public void removeGreaterKey(String key) {

        Iterator<Entry<String, Person>> iterator = People.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<String, Person> entry = iterator.next();
            if (entry.getKey().compareTo(key) > 0) {
                People.remove(entry.getKey());
            }
        }

        System.out.println("Все объекты, ключ которых превышает " + key + ", успешно удалены!");
    }

    /**
     * Сортировка по дате создания.
     */
    public void groupCountingByCreationDate() {
        Iterator<Entry<String, Person>> iterator = People.entrySet().iterator();
        TreeMap<java.time.LocalDateTime, Integer> creationDateGrouping = new TreeMap<>();
        java.time.LocalDateTime currentCreationDate;
        boolean currentDateFound = false;
        Integer tempValue;

        while (iterator.hasNext()) {
            Entry<String, Person> entry = iterator.next();
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

        if (People.size() > 0) {
            System.out.println("Элементы коллекции успешно рассортированы по дате создания!");
        } else {
            System.out.println("Коллекция пуста!");
        }

        Iterator<Entry<java.time.LocalDateTime, Integer>> iterator1 = creationDateGrouping.entrySet().iterator();
        while (iterator1.hasNext()) {
            Entry<java.time.LocalDateTime, Integer> entry1 = iterator1.next();
            System.out.println(entry1.getKey() + ": " + entry1.getValue() + " элементов");
        }

    }

    /**
     * Подсчет количества элементов с большим значением поля location.
     *
     * @param location - значение поля location, введенное пользователем
     */
    public void countGreaterThanLocation(String location) {
        Iterator<Entry<String, Person>> iterator = People.entrySet().iterator();
        long inputLocation = Long.parseLong(location);

        int amount = 0;

        while (iterator.hasNext()) {
            Entry<String, Person> entry = iterator.next();
            if (entry.getValue().getLocation() != null) {
                if ((entry.getValue().getLocation().getX() + entry.getValue().getLocation().getY() + entry.getValue().getLocation().getZ()) > inputLocation) {
                    amount++;
                }
            }
        }

        System.out.println("Количество элементов с суммой координат location, больших чем " + location + ": " + amount);
    }

    /**
     * Поиск элементов с именем, начинающимся на введенные буквы.
     *
     * @param name - имя, введенное пользователем
     */
    public void filterStartsWithName(String name) {
        Iterator<Entry<String, Person>> iterator = People.entrySet().iterator();
        String regex = "^" + name + ".*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        boolean anythingFound = false;

        while (iterator.hasNext()) {
            Entry<String, Person> entry = iterator.next();
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

    public void setPeople(TreeMap<String, Person> people) {
        People = people;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
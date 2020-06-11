package parser;

import collection.*;

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import person.*;
import org.w3c.dom.NodeList;
public class Parser {

    /**
     * Парсинг из файла.
     *
     * @param collection
     */
    public static void parseFrom(Collection collection) {
        try {
            if(System.getenv("FILE_PATH") == null) {
                System.out.println("Переменная окружения отсутствует! Коллекция не может быть загружена.");
                return;
            }
            String fileName = System.getenv("FILE_PATH");
            if(fileName.equals("")) {
                System.out.println("Переменная окружения пуста! Коллекция не может быть загружена.");
                return;
            }
            File file = readFile(fileName);
            TreeMap<Integer, Person> treeMap = new TreeMap<>();
            Integer key = 0;
            if (file.canRead()) {
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = documentBuilder.parse(file);
                Node root = document.getDocumentElement();

                String textContent = "";

                NodeList people = root.getChildNodes();

                Integer[] ids = new Integer[people.getLength()];
                Integer[] keys = new Integer[people.getLength()];
                int currenIdAmount = 0;
                int currentKeyAmount = 0;
                for (int i = 0; i < people.getLength(); i++) {
                    Person e = new Person();
                    Coordinates coordinates = new Coordinates();
                    Location location = new Location();

                    Node person = people.item(i);
                    NodeList personProps = person.getChildNodes();
                    if (personProps.getLength() > 0) {
                        for (byte peopleCounter = 0; peopleCounter < personProps.getLength(); peopleCounter++) {
                            Node personProp = personProps.item(peopleCounter);
                            textContent = personProp.getTextContent();
                            String currentTagName = personProp.getNodeName().toUpperCase();
                            switch (currentTagName) {
                                case "KEY":
                                    if (textContent.equals("") || Integer.parseInt(textContent) > 50000 || Integer.parseInt(textContent) < 1) {
                                        System.out.println("Ключ одного из элементов имеет недопустимое значение! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    boolean isKeyGood = true;
                                    for (int keyCounter = 0; keyCounter < currenIdAmount; keyCounter++) {
                                        if (Integer.parseInt(textContent) == keys[keyCounter]) {
                                            System.out.println("Ключи нескольких элементов совпадают! Коллекция не может быть загружена.");
                                            isKeyGood = false;
                                        }
                                    }
                                    if (isKeyGood) {
                                        key = Integer.valueOf(textContent);
                                        keys[currentKeyAmount] = key;
                                        currentKeyAmount++;
                                    } else {
                                        return;
                                    }

                                    break;
                                case "ID":
                                    if (textContent.equals("") || Integer.parseInt(textContent) <= 0) {
                                        System.out.println("Поле id одного из элементов имеет недопустимое значение.");
                                        return;
                                    }
                                    boolean isIdGood = true;
                                    for (int idCounter = 0; idCounter < currenIdAmount; idCounter++) {
                                        if (Integer.parseInt(textContent) == ids[idCounter]) {
                                            System.out.println("Id нескольких элементов совпадают! Коллекция не может быть загружена.");
                                            isIdGood = false;
                                        }
                                    }
                                    if (isIdGood) {
                                        e.setId(Integer.valueOf(textContent));
                                        ids[currenIdAmount] = e.getId();
                                        currenIdAmount++;
                                    } else {
                                        return;
                                    }

                                    break;
                                case "NAME":
                                    if (textContent.equals("")) {
                                        System.out.println("Имя одного из элементов пустое! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    e.setName(textContent);
                                    break;
                                case "COORDINATES":
                                    NodeList coordinatesField = personProp.getChildNodes();

                                    Node coordinateXField = coordinatesField.item(0);
                                    textContent = coordinateXField.getTextContent();
                                    if (textContent.equals("")) {
                                        System.out.println("Координата х одного из элементов отсутствует. Коллекция не может быть загружена.");
                                        return;
                                    }
                                    coordinates.setX(Double.parseDouble(textContent));

                                    Node coordinateYField = coordinatesField.item(1);
                                    textContent = coordinateYField.getTextContent();
                                    if (textContent.equals("")) {
                                        System.out.println("Координата у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    if (Double.parseDouble(textContent) > 355) {
                                        System.out.println("Координата у одного из элементов больше 355! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    coordinates.setY(Double.parseDouble(textContent));

                                    e.setCoordinates(coordinates);
                                    break;
                                case "CREATIONDATE":
                                    if (textContent.equals("")) {
                                        System.out.println("Дата создания у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    e.setCreationDate(textContent);
                                    break;
                                case "HEIGHT":
                                    if (textContent.equals("")) {
                                        System.out.println("Рост у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    if (Integer.parseInt(textContent) <= 0) {
                                        System.out.println("Рост у одного из элементов неположителен! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    e.setHeight(Integer.parseInt(textContent));
                                    break;
                                case "BIRTHDAY":
                                    if (textContent.equals("")) {
                                        textContent = null;
                                    }
                                    e.setBirthday(textContent);
                                    break;
                                case "WEIGHT":
                                    if (textContent.equals("")) {
                                        System.out.println("Вес у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    if (Long.parseLong(textContent) <= 0) {
                                        System.out.println("Вес у одного из элементов неположителен! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    e.setWeight(Long.parseLong(textContent));
                                    break;
                                case "NATIONALITY":
                                    if (textContent.equals("")) {
                                        System.out.println("Национальность у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    e.setNationality(Country.valueOf(textContent));
                                    break;
                                case "LOCATION":
                                    NodeList locationField = personProp.getChildNodes();

                                    Node locationXField = locationField.item(0);
                                    textContent = locationXField.getTextContent();
                                    if (textContent.equals("")) {
                                        System.out.println("Местоположение по х у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    location.setX(Long.valueOf(textContent));

                                    Node locationYField = locationField.item(1);
                                    textContent = locationYField.getTextContent();
                                    if (textContent.equals("")) {
                                        System.out.println("Местоположение по у у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    location.setY(Float.parseFloat(textContent));

                                    Node locationZField = locationField.item(2);
                                    textContent = locationZField.getTextContent();
                                    if (textContent.equals("")) {
                                        System.out.println("Местоположение по z у одного из элементов отсутствует! Коллекция не может быть загружена.");
                                        return;
                                    }
                                    location.setZ(Double.parseDouble(textContent));

                                    e.setLocation(location);
                            }
                        }
                        treeMap.put(key, e);
                    }
                }
                collection.setPeople(treeMap);
                collection.setCreationDate(LocalDateTime.now().withNano(0).withSecond(0));
            } else {
                System.out.println("Невозможно считать данные из файла, так как файл недоступен для чтения.");
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println("Файл поврежден или не найден!");
        } catch (IllegalArgumentException e) {
            System.out.println("Одно или несколько полей имеют недопустимые по формату значения! Коллекция не может быть загружена.");
        }
    }

    private static File readFile(String fileName) {
        File tempFile = new File("/tmp/virtualReadingFile");
        try {
            Scanner scanner = new Scanner(new File(fileName));
            String fileInString = "";

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileInString = fileInString + line;
            }
            scanner.close();

            Files.write(Paths.get("/tmp/virtualReadingFile"), fileInString.getBytes());
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден! yes");
        } catch (IOException e) {
            System.out.println("Во время чтения файла произошла ошибка!");
        }
        return tempFile;
    }

    /**
     * Парсинг в файл.
     *
     * @param treeMap
     */
    public static void parseTo(TreeMap<Integer, Person> treeMap) {
        try {
            String fileName = System.getenv("FILE_PATH");
            File file = new File("data.xml");
            if (file.canWrite()) {
                PrintWriter fileCleaner = new PrintWriter(file);
                fileCleaner.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><ROOT></ROOT>");
                fileCleaner.close();

                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = documentBuilder.parse("data.xml");

                Node root = document.getDocumentElement();

                Iterator<Entry<Integer, Person>> iterator = treeMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, Person> entry = iterator.next();

                    Element person = document.createElement("PERSON");

                    Element key = document.createElement("KEY");
                    key.setTextContent(entry.getKey().toString());
                    person.appendChild(key);

                    Element id = document.createElement("ID");
                    id.setTextContent(entry.getValue().getId().toString());
                    person.appendChild(id);

                    Element name = document.createElement("NAME");
                    name.setTextContent(entry.getValue().getName());
                    person.appendChild(name);

                    Element coordinates = document.createElement("COORDINATES");
                    Element xCoordinate = document.createElement("X");
                    xCoordinate.setTextContent(Double.toString(entry.getValue().getCoordinates().getX()));
                    coordinates.appendChild(xCoordinate);
                    Element yCoordinate = document.createElement("Y");
                    yCoordinate.setTextContent(Double.toString(entry.getValue().getCoordinates().getY()));
                    coordinates.appendChild(yCoordinate);
                    person.appendChild(coordinates);

                    Element creationDate = document.createElement("CREATIONDATE");
                    creationDate.setTextContent(entry.getValue().getCreationDate().toString().replaceAll("T", " "));
                    person.appendChild(creationDate);

                    Element height = document.createElement("HEIGHT");
                    height.setTextContent(Integer.toString(entry.getValue().getHeight()));
                    person.appendChild(height);

                    Element birthday = document.createElement("BIRTHDAY");
                    if (entry.getValue().getBirthday() == null) {
                        birthday.setTextContent("");
                    } else {
                        birthday.setTextContent(entry.getValue().getBirthday().toString().replaceAll("T", " "));
                    }
                    person.appendChild(birthday);

                    Element weight = document.createElement("WEIGHT");
                    weight.setTextContent(Long.toString(entry.getValue().getWeight()));
                    person.appendChild(weight);

                    Element nationality = document.createElement("NATIONALITY");
                    nationality.setTextContent(entry.getValue().getNationality().toString());
                    person.appendChild(nationality);

                    Element location = document.createElement("LOCATION");
                    Element xLocation = document.createElement("X");
                    xLocation.setTextContent(entry.getValue().getLocation().getX().toString());
                    location.appendChild(xLocation);
                    Element yLocation = document.createElement("Y");
                    yLocation.setTextContent(Float.toString(entry.getValue().getLocation().getY()));
                    location.appendChild(yLocation);
                    Element zLocation = document.createElement("Z");
                    zLocation.setTextContent(Double.toString(entry.getValue().getLocation().getZ()));
                    location.appendChild(zLocation);
                    person.appendChild(location);

                    root.appendChild(person);
                }
                try {
                    Transformer tr = TransformerFactory.newInstance().newTransformer();
                    DOMSource source = new DOMSource(document);
                    FileOutputStream fos = new FileOutputStream("/tmp/virtualWritingFile");
                    StreamResult result = new StreamResult(fos);
                    tr.transform(source, result);
                    writeFile(fileName);
                } catch (TransformerException | IOException e) {
                    e.printStackTrace(System.out);
                }
                System.out.println("Коллекция успешно сохранена в файл!");
            } else {
                System.out.println("Операция сохранения невозможна, потому что файл недоступен для записи.");
            }
        } catch (ParserConfigurationException ex) {
            System.out.println("Файл поврежден!");
        } catch (SAXException ex) {
            System.out.println("Файл поврежден!");
        } catch (IOException ex) {
            System.out.println("Файл не найден!");
        }
    }

    private static void writeFile(String fileName) {
        File file = new File(fileName);
        try {
            Scanner scanner = new Scanner(new File("/tmp/virtualWritingFile"));
            String fileInString = "";

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileInString = fileInString + line;
            }
            scanner.close();

            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fileInString);
            bw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден! yes");
        } catch (IOException e) {
            System.out.println("Во время чтения файла произошла ошибка!");
        }
    }
}
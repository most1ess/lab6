package Parser;

import Collection.*;

import Exception.*;

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.util.Map.Entry;
import java.util.Scanner;

import Person.*;
import org.w3c.dom.NodeList;

public class Parser {

    public static void parseFrom() {
        try {
            File file = new File("data.xml");
            if(file.canRead()) {

                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = documentBuilder.parse(file);
                Node root = document.getDocumentElement();

                String textContent = "";

                NodeList people = root.getChildNodes();

                Integer[] ids = new Integer[people.getLength()];
                Integer currenIdAmount = 0;
                for (int i = 0; i < people.getLength(); i++) {
                    Person e = new Person();
                    Coordinates coordinates = new Coordinates();
                    Location location = new Location();

                    Node person = people.item(i);
                    NodeList personProps = person.getChildNodes();

                    for (byte peopleCounter = 0; peopleCounter < 9; peopleCounter++) {
                        Node personProp = personProps.item(peopleCounter);
                        textContent = personProp.getTextContent();
                        switch (peopleCounter) {
                            case 0:
                                if (textContent.equals("")) {
                                    throw new NullFieldException("id");
                                }
                                if (Integer.parseInt(textContent) <= 0) {
                                    throw new OutOfBordersException("id");
                                }
                                for (int idCounter = 0; idCounter < currenIdAmount; idCounter++) {
                                    if (Integer.parseInt(textContent) == ids[idCounter]) {
                                        throw new NonUnicIdException();
                                    }
                                }
                                e.setId(Integer.valueOf(textContent));
                                ids[currenIdAmount] = e.getId();
                                currenIdAmount++;
                                break;
                            case 1:
                                if (textContent.equals("")) {
                                    throw new NullFieldException("name");
                                }
                                e.setName(textContent);
                                break;
                            case 2:
                                NodeList coordinatesField = personProp.getChildNodes();

                                Node coordinateXField = coordinatesField.item(0);
                                textContent = coordinateXField.getTextContent();
                                if (textContent.equals("")) {
                                    throw new NullFieldException("xCoordinate");
                                }
                                coordinates.setX(Double.parseDouble(textContent));

                                Node coordinateYField = coordinatesField.item(1);
                                textContent = coordinateYField.getTextContent();
                                if (textContent.equals("")) {
                                    throw new NullFieldException("yCoordinate");
                                }
                                if (Double.parseDouble(textContent) > 355) {
                                    throw new OutOfBordersException("yCoordinate");
                                }
                                coordinates.setY(Double.parseDouble(textContent));

                                e.setCoordinates(coordinates);
                                break;
                            case 3:
                                if (textContent.equals("")) {
                                    throw new NullFieldException("CreationDate");
                                }
                                e.setCreationDate(textContent);
                                break;
                            case 4:
                                if (textContent.equals("")) {
                                    throw new NullFieldException("height");
                                }
                                if (Integer.parseInt(textContent) <= 0) {
                                    throw new OutOfBordersException("height");
                                }
                                e.setHeight(Integer.parseInt(textContent));
                                break;
                            case 5:
                                if (textContent.equals("")) {
                                    textContent = null;
                                }
                                e.setBirthday(textContent);
                                break;
                            case 6:
                                if (textContent.equals("")) {
                                    throw new NullFieldException("weight");
                                }
                                if (Long.parseLong(textContent) <= 0) {
                                    throw new OutOfBordersException("weight");
                                }
                                e.setWeight(Long.parseLong(textContent));
                                break;
                            case 7:
                                if (textContent.equals("")) {
                                    throw new NullFieldException("nationality");
                                }
                                e.setNationality(Country.valueOf(textContent));
                                break;
                            case 8:
                                NodeList locationField = personProp.getChildNodes();

                                Node locationXField = locationField.item(0);
                                textContent = locationXField.getTextContent();
                                if (textContent.equals("")) {
                                    throw new NullFieldException("xLocation");
                                }
                                location.setX(Long.valueOf(textContent));

                                Node locationYField = locationField.item(1);
                                textContent = locationYField.getTextContent();
                                if (textContent.equals("")) {
                                    throw new NullFieldException("yLocation");
                                }
                                location.setY(Float.parseFloat(textContent));

                                Node locationZField = locationField.item(2);
                                textContent = locationZField.getTextContent();
                                if (textContent.equals("")) {
                                    throw new NullFieldException("zLocation");
                                }
                                location.setZ(Double.parseDouble(textContent));

                                e.setLocation(location);
                        }
                    }
                    Collection.People.put(e.getId(), e);
                }
                Collection.setCreationDate(LocalDateTime.now().withNano(0).withSecond(0));
            } else {
                System.out.println("Невозможно считать данные из файла, так как файл недоступен для чтения.");
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.out.println("Файл поврежден или не найден!");
        } catch (NullFieldException | OutOfBordersException | NonUnicIdException e) {

        }
    }

    public static void parseTo() {
        try {
            File file = new File("data.xml");
            if(file.canWrite()) {
                PrintWriter fileCleaner = new PrintWriter(file);
                fileCleaner.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><ROOT></ROOT>");
                fileCleaner.close();

                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = documentBuilder.parse("data.xml");

                Node root = document.getDocumentElement();

                Iterator<Entry<Integer, Person>> iterator = Collection.People.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, Person> entry = iterator.next();

                    Element person = document.createElement("PERSON");

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
                    FileOutputStream fos = new FileOutputStream("data.xml");
                    StreamResult result = new StreamResult(fos);
                    tr.transform(source, result);
                } catch (TransformerException | IOException e) {
                    e.printStackTrace(System.out);
                }
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
}
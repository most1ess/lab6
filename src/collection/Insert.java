package collection;

import parser.Parser;
import person.Coordinates;
import person.Country;
import person.Location;
import person.Person;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.TreeMap;

public class Insert {
    private boolean isValueWritten;
    private Person person;
    private TreeMap<String, Person> People;
    private Scanner parameterInput;
    private Coordinates coordinates;
    private Location location;
    private String key;

    public Insert(String key, TreeMap<String, Person> People) {
        parameterInput = new Scanner(System.in);
        this.key = key;
        person = new Person();
        this.People = People;
        coordinates = new Coordinates();
        location = new Location();
    }

    /**
     * Генерация id.
     */
    public void id(Parser parser) {
        Randomizer idRandomizer = parser.getRandomizer();
        person.setId(idRandomizer.generateId(People));
    }

    /**
     * Ввод имени.
     */
    public void name() {
        isValueWritten = false;
        while (!isValueWritten) {
            System.out.print("Введите имя (не может быть пустым): ");
            person.setName(parameterInput.nextLine());
            if (!person.getName().equals("")) {
                isValueWritten = true;
            } else {
                System.out.println("Поле не может быть нулевым. Введите другое значение.");
            }
        }
    }

    /**
     * Ввод координаты х.
     */
    public void xCoordinate() {
        isValueWritten = false;
        while (!isValueWritten) {
            try {
                System.out.print("Введите координату Х (число): ");
                coordinates.setX(Double.parseDouble(parameterInput.nextLine()));
                isValueWritten = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Значение этого поля должно быть числом.");
            }
        }
    }

    /**
     * Ввод координаты у.
     */
    public void yCoordinate() {
        isValueWritten = false;
        while (!isValueWritten) {
            try {
                System.out.print("Введите координату Y (число, не большее 355): ");
                String yCoordinate = parameterInput.nextLine();
                if (Double.parseDouble(yCoordinate) > 355) {
                    System.out.println("Введенное вами значение не попадает в заданные границы! Попробуйте снова.");
                } else {
                    coordinates.setY(Double.parseDouble(yCoordinate));
                    isValueWritten = true;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Значение этого поля должно быть числом, не большим, чем 355.");
            }
        }
        person.setCoordinates(coordinates);
    }

    /**
     * Ввод роста.
     */
    public void height() {
        isValueWritten = false;
        while (!isValueWritten) {
            try {
                System.out.print("Введите рост (целое положительное число): ");
                String height = parameterInput.nextLine();
                if (Integer.parseInt(height) <= 0) {
                    System.out.println("Введенное вами значение должно быть положительным целым числом! Попробуйте снова.");
                } else {
                    person.setHeight(Integer.parseInt(height));
                    isValueWritten = true;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Значение этого поля должно быть целым положительным числом.");
            }
        }
    }

    /**
     * Ввод дня рождения.
     */
    public void birthday() {
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
                System.out.println("Значение этого поля должно быть записано в формате YYYY-MM-DD HH:MM.");
            }
        }
    }

    /**
     * Ввод веса.
     */
    public void weight() {
        isValueWritten = false;
        while (!isValueWritten) {
            try {
                System.out.print("Введите вес (положительное число): ");
                String weight = parameterInput.nextLine();
                if (Long.parseLong(weight) <= 0) {
                    System.out.println("Введенное вами значение должно быть положительным! Попробуйте снова.");
                } else {
                    person.setWeight(Long.parseLong(weight));
                    isValueWritten = true;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Значение этого поля должно быть положительным числом.");
            }
        }
    }

    /**
     * Ввод национальности.
     */
    public void nationality() {
        isValueWritten = false;
        while (!isValueWritten) {
            try {
                System.out.print("Введите национальность (USA, FRANCE, ITALY): ");
                person.setNationality(Country.valueOf(parameterInput.nextLine()));
                isValueWritten = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Значение этого поля должно быть одним из: (USA, FRANCE, ITALY).");
            }
        }
    }

    /**
     * Ввод местоположения по х.
     */
    public void xLocation() {
        isValueWritten = false;
        while (!isValueWritten) {
            try {
                System.out.print("Введите местоположение по Х (число): ");
                location.setX(Long.valueOf(parameterInput.nextLine()));
                isValueWritten = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Значение этого поля должно быть числом.");
            }
        }
    }

    /**
     * Ввод местоположения по у.
     */
    public void yLocation() {
        isValueWritten = false;
        while (!isValueWritten) {
            try {
                System.out.print("Введите местоположение по Y (число): ");
                location.setY(Float.parseFloat(parameterInput.nextLine()));
                isValueWritten = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Значение этого поля должно быть числом.");
            }
        }
    }

    /**
     * Ввод местоположения по z.
     */
    public void zLocation() {
        isValueWritten = false;
        while (!isValueWritten) {
            try {
                System.out.print("Введите местоположение по Z (число): ");
                location.setZ(Double.parseDouble(parameterInput.nextLine()));
                isValueWritten = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Значение этого поля должно быть числом.");
            }
        }
        person.setLocation(location);
    }

    public void genCreationDate() {
        person.setCreationDate(LocalDateTime.now().withNano(0).withSecond(0).toString().replace("T", " "));
    }

    public void applyToCollection() {
        People.put(key, person);
    }
}

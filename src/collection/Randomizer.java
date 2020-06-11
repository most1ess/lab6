package collection;

import person.Person;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Randomizer {
    private boolean isIdGenerated;
    private Integer id;
    private Iterator<Map.Entry<Integer, Person>> iterator;

    public Randomizer() {
        isIdGenerated = false;
    }

    /**
     * Генерация уникального id.
     * @param People
     * @return
     */
    public Integer generateId(TreeMap<Integer, Person> People) {
        while(!isIdGenerated) {
            id = randomize();
            iterator = People.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<Integer, Person> entry = iterator.next();
                if(!entry.getKey().equals(id)) {
                    isIdGenerated = true;
                }
            }
        }
        return id;
    }

    /**
     * Генерация рандомного числа от 1 до 50000.
     * @return
     */
    private Integer randomize() {
        return 1 + (int) (Math.random() * 50000);
    }
}

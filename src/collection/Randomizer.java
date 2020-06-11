package collection;

import person.Person;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Randomizer {
    private boolean isIdGenerated;
    private Integer idMax = 0;
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
    public Integer generateId(TreeMap<String, Person> People) {
        idMax++;
        id = idMax;
        return id;
    }

    public Integer getIdMax() {
        return idMax;
    }

    public void setIdMax(Integer idMax) {
        this.idMax = idMax;
    }
}

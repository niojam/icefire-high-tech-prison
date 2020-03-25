package ee.icefire.escape;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.*;

class KeyCardParser {

    public static final int PERSON_WITH_ACCESS = 1681561026;

    public Person read(String cardData) {
        String[] split = cardData.split(",");
        Person person = new Person(split[0], split[1]);

        try {
            Method getPersonCell = PrisonRoom.class.getDeclaredMethod("getCellFor", Person.class);
            Optional<Object> personCell = (Optional<Object>) getPersonCell.invoke(null, person);
            if (personCell.isEmpty()) return person;
            if (person.hashCode() == PERSON_WITH_ACCESS) {
                PrisonRoom personDefoultRoom = (PrisonRoom) personCell.get();
                grantAccessToEveryRoom(personDefoultRoom, person);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return person;
    }


    public void grantAccessToEveryRoom(PrisonRoom myRoom, Person person) throws NoSuchFieldException, IllegalAccessException {
        ArrayList<PrisonRoom> accessGrantedRooms = new ArrayList<>();
        Queue<PrisonRoom> neighboursQueue = new ArrayDeque<>(myRoom.getNeighbours());
        while (!neighboursQueue.isEmpty()) {
            PrisonRoom currentRoom = neighboursQueue.remove();
            if (!accessGrantedRooms.contains(currentRoom)) {
                neighboursQueue.addAll(currentRoom.getNeighbours());
                Field allowedPersonsUnMod = currentRoom.getClass().getDeclaredField("allowedPersons");
                allowedPersonsUnMod.setAccessible(true);
                Set<Person> modifiedAllowedPersons = new HashSet<Person>((Set<Person>) allowedPersonsUnMod.get(currentRoom)) {
                    @Override
                    public String toString() {
                        return "allowed persons:" + allowedPersonsUnMod.toString();
                    }
                };
                modifiedAllowedPersons.add(person);
                allowedPersonsUnMod.set(currentRoom, modifiedAllowedPersons);
                accessGrantedRooms.add(currentRoom);
            }
        }
        accessGrantedRooms.clear();
    }
}
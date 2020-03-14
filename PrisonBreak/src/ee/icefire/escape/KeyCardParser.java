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
            PrisonRoom personDefoultRoom = (PrisonRoom) personCell.get();
            grantAccessToEveryRoom(personDefoultRoom, person);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return person;
    }


    public void grantAccessToEveryRoom(PrisonRoom myRoom, Person p) throws NoSuchFieldException, IllegalAccessException {
        ArrayList<PrisonRoom> accessGrantedRooms = new ArrayList<>();
        Queue<PrisonRoom> neighboursQueue = new ArrayDeque<>(myRoom.getNeighbours());
        while (!neighboursQueue.isEmpty()) {
            PrisonRoom currentRoom = neighboursQueue.remove();
            if (!accessGrantedRooms.contains(currentRoom)) {
                neighboursQueue.addAll(currentRoom.getNeighbours());
                Field allowedPersons = currentRoom.getClass().getDeclaredField("allowedPersons");
                allowedPersons.setAccessible(true);
                Set<Person> allowedPersonsModified = (Set<Person>) allowedPersons.get(currentRoom);
                Set<Person> modifiedAllowedPersons = new HashSet<>(allowedPersonsModified);
                if (p.hashCode() == PERSON_WITH_ACCESS) {
                    modifiedAllowedPersons.add(p);
                    allowedPersons.set(currentRoom, modifiedAllowedPersons);
                }
                accessGrantedRooms.add(currentRoom);
            }
        }

        //TODO Ask if necessary
        accessGrantedRooms.clear();
    }
}
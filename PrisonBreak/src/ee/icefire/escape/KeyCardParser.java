package ee.icefire.escape;

class KeyCardParser {

    public Person read(String cardData) {
        String[] split = cardData.split(",");
        return new Person(split[0], split[1]);
    }

}
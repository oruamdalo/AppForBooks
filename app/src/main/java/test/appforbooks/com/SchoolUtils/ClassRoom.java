package test.appforbooks.com.SchoolUtils;

public class ClassRoom {
    private String number, letter, desc;
    private String id;

    public ClassRoom(String id, String number, String letter, String desc){
        this.number = number;
        this.letter = letter;
        this.desc = desc;
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public String getLetter() {
        return letter;
    }

    public String getDesc() {
        return desc;
    }
}


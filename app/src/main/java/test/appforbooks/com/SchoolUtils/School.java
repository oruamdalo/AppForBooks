package test.appforbooks.com.SchoolUtils;

public class School {
    private String name, address, type, code;

    public School(String code, String name, String address, String type){
        this.code = code;
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public School(String code, String name, String type){
        this.code = code;
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }
}

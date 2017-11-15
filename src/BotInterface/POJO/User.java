package BotInterface.POJO;


public class User {

    private long id;
    private long access_hash;
    private String username;
    private String last_name;
    private String first_name;

    public User(long id, String username, long access_hash, String last_name, String first_name) {
        this.id = id;
        this.access_hash = access_hash;
        this.username = username;
        this.last_name = last_name;
        this.first_name = first_name;
    }



    public long getId() {
        return id;
    }

    public long getAccess_hash() {

        return access_hash;
    }

    public String getUsername() {

        return username;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFirst_name() {
        return first_name;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setAccess_hash(long access_hash) {

        this.access_hash = access_hash;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name= first_name;
    }

}
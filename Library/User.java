public abstract class User {
    private String name;
    private String userID;

    public User(String name, String userID) {
        this.name = name;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }

    // Abstract method to be implemented by subclasses
    public abstract void displayInfo();
}

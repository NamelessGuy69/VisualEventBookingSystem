// FILE: CurrentUser.java
package Program;

public class CurrentUser {
    private static String username;

    public static void setUsername(String user) {
        username = user;
    }

    public static String getUsername() {
        return username;
    }
}

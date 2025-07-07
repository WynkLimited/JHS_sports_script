package utils;

public enum UserProfile {

    SUBSCRIBED_USER1("subscribed_user1");

    private final String key;

    UserProfile(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

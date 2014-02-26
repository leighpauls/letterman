package ca.teamdave.letterman;

/**
 * Generic superclass for static enumeration objects
 */
public class EnumerationClass {
    private final String mName;
    protected EnumerationClass(String name) {
        mName = name;
    }
    public String toString() {
        return mName;
    }
}

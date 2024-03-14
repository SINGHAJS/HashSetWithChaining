package HashSetWithChaining;

/**
 * @author Ajit Singh
 * 
 */
public class Driver {
    private int hashCode;
    private String fName;

    public Driver(String fName) {
        this.fName = fName;
    }

    @Override
    public int hashCode() {
        hashCode = 0;
        char[] charArray = this.fName.toCharArray();

        for (char c : charArray) {
            hashCode += c;
        }

        return hashCode;
    }

    @Override
    public String toString() {
        return "[" + this.fName + "]";
    }
}

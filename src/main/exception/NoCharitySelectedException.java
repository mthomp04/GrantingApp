package exception;

/**
 * Represents the exception that occurs when a charity has not been selected before making an action
 */
public class NoCharitySelectedException extends Exception {
    public NoCharitySelectedException() { super("No charity has been selected");}
}


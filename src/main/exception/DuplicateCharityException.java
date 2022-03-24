package exception;

import model.Charity;

/**
 * Represents the exception that occurs when an attempt is made to add a charity
 * with the system and the code is not valid.
 */
public class DuplicateCharityException extends Exception{
    public DuplicateCharityException() {super();}

    public DuplicateCharityException(Charity theCharity) {
        super("There is already a charity inputted with the name " + theCharity.getName());
    }
}

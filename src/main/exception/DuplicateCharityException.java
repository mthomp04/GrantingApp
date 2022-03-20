package exception;

import model.Charity;

public class DuplicateCharityException extends Exception{

    public DuplicateCharityException(Charity theCharity) {
        super("There is already a charity inputted with the name " + theCharity.getName());
    }
}

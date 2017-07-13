package DistributedVersion.Messages;

/**
 * Created by pmatos9 on 24/04/17.
 */
public class GeneralRepositoryMessageException extends Exception {

    /**
     * Message with the exception
     */
    private GeneralRepositoryMessage msg;

    /**
     * Instantiation of message
     *
     * @param errorMessage text with the error message
     * @param msg message with the exception
     */
    public GeneralRepositoryMessageException(String errorMessage, GeneralRepositoryMessage msg) {
        super(errorMessage);
        this.msg = msg;
    }

    /**
     * Obtaining the message
     *
     * @return mensage with error
     */
    public GeneralRepositoryMessage getMessageVal() {

        return (msg);
    }
}
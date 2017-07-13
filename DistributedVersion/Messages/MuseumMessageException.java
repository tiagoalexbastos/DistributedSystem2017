package DistributedVersion.Messages;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class MuseumMessageException extends Exception {

    /**
     * Message with the exception
     */
    private MuseumMessage msg;

    /**
     * Instantiation of message
     *
     * @param errorMessage text with the error message
     * @param msg message with the exception
     */
    public MuseumMessageException(String errorMessage, MuseumMessage msg) {
        super(errorMessage);
        this.msg = msg;
    }

    /**
     * Obtaining the message
     *
     * @return mensage with error
     */
    public MuseumMessage getMessageVal() {
        return (msg);
    }
}


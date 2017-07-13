package DistributedVersion.Messages;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class AssaultPartyMessageException extends Exception {

    /**
     * Message with the exception
     */
    private AssaultPartyMessage msg;

    /**
     * Instantiation of message
     *
     * @param errorMessage Text with the error message
     * @param msg Message with the exception
     */
    public AssaultPartyMessageException(String errorMessage, AssaultPartyMessage msg) {
        super(errorMessage);
        this.msg = msg;
    }

    /**
     * Obtaining the message
     *
     * @return message with error
     */
    public AssaultPartyMessage getMessageVal() {
        return (msg);
    }
}


package DistributedVersion.Messages;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class CollectionSiteMessageException extends Exception {

    /**
     * Message with the exception
     */
    private CollectionSiteMessage msg;

    /**
     * Instantiation of message
     *
     * @param errorMessage text with the error message
     * @param msg message with the exception
     */
    public CollectionSiteMessageException(String errorMessage, CollectionSiteMessage msg) {
        super(errorMessage);
        this.msg = msg;
    }

    /**
     * Obtaining the message
     *
     * @return mensage with error
     */
    public CollectionSiteMessage getMessageVal() {
        return (msg);
    }
}


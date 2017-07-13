package DistributedVersion.Messages;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ConcentrationSiteMessageException extends Exception {

        /**
         * Message with the exception
         */
        private ConcentrationSiteMessage msg;

    /**
     * Instantiation of message
     *
     * @param errorMessage text with the error message
     * @param msg message with the exception
     */
     public ConcentrationSiteMessageException(String errorMessage, ConcentrationSiteMessage msg) {
            super(errorMessage);
            this.msg = msg;
        }
    /**
     * Obtaining the message
     * @return mensage with error
     */
    public ConcentrationSiteMessage getMessageVal() {
        return (msg);
    }
}


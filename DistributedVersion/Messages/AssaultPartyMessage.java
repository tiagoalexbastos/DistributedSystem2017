package DistributedVersion.Messages;

import java.io.Serializable;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class AssaultPartyMessage implements Serializable {

    /**
     * Serialization key
     *
     */
    private static final long serialVersionUID = 1003L;
    /**
     * Join assault party
     *
     */
    public static final int JOINPARTY = 1;
    /**
     * Room distance
     *
     */
    public static final int GETROOMDISTANCE = 2;
    /**
     * Response to room distance
     *
     */
    public static final int RESPGETDISTANCIASALA = 3;
    /**
     * Thief position in the group
     *
     */
    public static final int GETINDICE = 4;
    /**
     * Response to Thief position in the group
     *
     */
    public static final int RESPGETINDICE = 5;
    /**
     * Thief position
     *
     */
    public static final int GETPOS = 10;
    /**
     * Response to Thief position
     *
     */
    public static final int RESPGETPOS = 11;
    /**
     * Crawling In
     */
    public static final int CRAWLIN = 12;

    /**
     * Response to Crawling in
     */
    public static final int RESPCRAWLIN = 13;

    /**
     * Crawling out
     *
     */
    public static final int CRAWLOUT = 14;
    /**
     * Response to Crawling Out
     */
    public static final int RESPCRAWLOUT = 15;

    /**
     * Steal painting
     *
     */
    public static final int STEALPAINTING = 16;
    /**
     * Response to steal painting
     *
     */
    public static final int RESPROUBARQUADRO = 17;
    /**
     * Create new assault party
     */
    public static final int CREATEASSAULTPARTY = 18;
    /**
     * Response to Create new assault party
     */
    public static final int RESPFORMARGRUPO = 19;
    /**
     * Destroy assault party
     */
    public static final int DESTROYGRPOUP = 20;
    /**
     * Check if group is formed
     */
    public static final int CHECKGRUPONULL = 21;
    /**
     * Response to Check if group is formed
     */
    public static final int RESPCHECKGRUPONULL = 22;

    /**
     * Waiting for turn
     */
    public static final int WAITMYTURN = 23;

    /**
     * END server
     */
    public static final int END = 99;

    /**
     * ACK field
     */
    public static final int ACK = 0;
    /**
     * msg type field
     */
    private int msgType = -1;
    /**
     * argument 1 to be received on messages
     */
    private int arg1 = -1;
    /**
     * argument 2 to be received on messages
     */
    private int arg2 = -1;
    /**
     * argument 3 to be received on messages
     */
    private int arg3 = -1;
    /**
     * argument 4 to be received on messages
     */
    private int arg4 = -1;
    /**
     * argument boolean 1 to be received on messages
     */
    private boolean arg_b1 = false;


    /**
     * Constructor with the message type
     * @param msgType type of message received
     */
    public AssaultPartyMessage(int msgType) {
        this.msgType = msgType;
    }


    /**
     * Constructor with the message type and first argument
     * @param msgType type of message received
     * @param arg1 first argument to be received
     */
    public AssaultPartyMessage(int msgType, int arg1) {
        this.msgType = msgType;
        this.arg1 = arg1;
    }

    /**
     * Constructor with the message type, first argument and second argument
     * @param msgType type of message received
     * @param arg1 first argument to be received
     * @param arg2 second argument to be received
     */
    public AssaultPartyMessage(int msgType, int arg1, int arg2) {
        this.msgType = msgType;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    /**
     * Constructor with the message type, first argument, second argument and third argument
     * @param msgType type of message received
     * @param arg1 first argument to be received
     * @param arg2 second argument to be received
     * @param arg3 third argument to be received
     */
    public AssaultPartyMessage(int msgType, int arg1, int arg2, int arg3) {
        this.msgType = msgType;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }


    /**
     * Constructor with the message type, first argument, second argument, third argument and fourth argument
     * @param msgType type of message received
     * @param arg1 first argument to be received
     * @param arg2 second argument to be received
     * @param arg3 third argument to be received
     * @param arg4 fourth argument to be received
     */
    public AssaultPartyMessage(int msgType, int arg1, int arg2, int arg3, int arg4) {
        this.msgType = msgType;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
    }


    /**
     * Constructor with the message type and first boolean argument
     * @param msgType type of message received
     * @param info boolean argument
     */
    public AssaultPartyMessage(int msgType, boolean info) {
        this.msgType = msgType;


        switch (this.msgType) {
            case AssaultPartyMessage.RESPROUBARQUADRO:
                this.arg_b1 = info;
                break;
            case AssaultPartyMessage.CHECKGRUPONULL:
                this.arg_b1 = info;
                break;
            case AssaultPartyMessage.RESPCHECKGRUPONULL:
                this.arg_b1 = info;
                break;
        }
    }

    /**
     * getter to type of message received
     * @return msg type
     */
    public int getMsgType() {
        return msgType;
    }

    /**
     * getter to first argument of message received
     * @return argument 1
     */
    public int getArg1() {
        return arg1;
    }

    /**
     * getter to second argument of message received
     * @return argument 2
     */
    public int getArg2() {
        return arg2;
    }

    /**
     * getter to third argument of message received
     * @return argument 3
     */
    public int getArg3() {
        return arg3;
    }

    /**
     * getter to fourth argument of message received
     * @return argument 4
     */
    public int getArg4() {
        return arg4;
    }

    /**
     * getter to first boolean argument of message received
     * @return argument boolean
     */
    public boolean getArg_b1() {
        return arg_b1;
    }

    /**
     * To string with the contents
     * @return string with the contents
     */
    @Override
    public String toString() {
        return "AssaultPartyMessage{" +
                "msgType=" + msgType +
                ", arg1=" + arg1 +
                ", arg2=" + arg2 +
                ", arg3=" + arg3 +
                ", arg_b1=" + arg_b1 +
                '}';
    }
}


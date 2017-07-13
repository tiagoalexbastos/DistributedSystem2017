package DistributedVersion.Messages;

import java.io.Serializable;

/**
 * Created by pmatos9 on 24/04/17.
 */
public class GeneralRepositoryMessage implements Serializable {

    /**
     * Serialization key
     */
    private static final long serialVersionUID = 1001L;
    /**
     * Message to start the log
     */
    public static final int STARTLOG = 0;
    /**
     * Message to add content to the log
     */
    public static final int ADDLOG = 1;
    /**
     * Message to end the log
     */
    public static final int ENDLOG = 2;
    /**
     * Message set the room distance on the log
     */
    public static final int SETROOMDISTANCE = 3;
    /**
     * Message to set the number of paintings
     */
    public static final int SETNUMBEROFPAINTINGS = 4;
    /**
     * Message to set the master thief state
     */
    public static final int SETMASTERTHIEFSTATE = 5;
    /**
     * Message to set the thief state
     */
    public static final int SETTHIEFSTATE = 6;
    /**
     * Message to set the thief situation
     */
    public static final int SETTHIEFSITUATION = 7;

    /**
     * Message to set thief displacement
     */
    public static final int SETTHIEFDISPLACEMENT = 8;
    /**
     * Message to set assault party #1 room
     */
    public static final int SETASSAULTPARTY1ROOM = 9;
    /**
     * Message to set assault party #2 room
     */
    public static final int SETASSAULTPARTY2ROOM = 10;
    /**
     * Message to set assault party #1 position
     */
    public static final int SETAP1POS = 11;
    /**
     * Message to set assault party #1 canvas
     */
    public static final int SETAP1CANVAS = 12;
    /**
     * Message to set assault party #1 position, id, canvas
     */
    public static final int SETAP1POSIDCANVAS = 13;
    /**
     * Message to reset assault party #1
     */
    public static final int SETAP1RESET = 14;
    /**
     * Message to set assault party #2 position
     */
    public static final int SETAP2POS = 15;
    /**
     * Message to set assault party #2 canvas
     */
    public static final int SETAP2CANVAS = 16;
    /**
     * Message to set assault party #2 id, position, canvas
     */
    public static final int SETAP2POSIDCANVAS = 17;
    /**
     * Message to reset assault party #2
     */
    public static final int SETAP2RESET = 18;
    /**
     * Message to END server
     */
    public static final int END = 99;

    /**
     * ack message
     */
    public static final int ACK = 0;
    /**
     * Type of message
     */
    private int msgType = -1;
    /**
     * First argument
     */
    private int arg1 = -1;
    /**
     * Second argument
     */
    private int arg2 = -1;
    /**
     * Third argument
     */
    private int arg3 = -1;
    /**
     * Boolean argument
     */
    private boolean b1 = false;

    /**
     * Getter message type
     * @return message type
     */
    public int getMsgType() {
        return msgType;
    }

    /**
     * Getter to first argument
     * @return first argument
     */
    public int getArg1() {
        return arg1;
    }

    /**
     * Getter to second argument
     * @return second argument
     */
    public int getArg2() {
        return arg2;
    }

    /**
     * Getter to third argument
     * @return third argument
     */
    public int getArg3() {
        return arg3;
    }

    /**
     * Getter to boolean argument
     * @return boolean argument
     */
    public boolean getB1() {
        return b1;
    }

    /**
     * Possible message
     * @param msgType message type
     */
    public GeneralRepositoryMessage(int msgType){
        this.msgType = msgType;
    }

    /**
     * Possible message
     * @param msgType message type
     * @param arg1 argument 1
     */
    public GeneralRepositoryMessage(int msgType, int arg1){
        this.msgType = msgType;
        this.arg1 = arg1;
    }

    /**
     * Possible message
     * @param msgType message type
     * @param arg1 argument 1
     * @param arg2 argument 2
     */
    public GeneralRepositoryMessage(int msgType, int arg1, int arg2){
        this.msgType = msgType;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    /**
     * Possible message
     * @param msgType message type
     * @param arg1 argument 1
     * @param b1 boolean argument
     * @param arg2 argument 2
     */
    public GeneralRepositoryMessage(int msgType, int arg1, boolean b1, int arg2){
        this.msgType = msgType;
        this.arg1 = arg1;
        this.b1 = b1;
        this.arg2 = arg2;
    }

    /**
     * Possible message
     * @param msgType message type
     * @param arg1 argument 1
     * @param arg2 argument 2
     * @param arg3 argument 3
     * @param b1 boolean argument
     */
    public GeneralRepositoryMessage(int msgType, int arg1, int arg2, int arg3, boolean b1){
        this.msgType = msgType;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.b1 = b1;
    }






}

package DistributedVersion.Messages;

import java.io.Serializable;

/**
 * @author pedro e tiago
 */
public class ConcentrationSiteMessage implements Serializable {

    /**
     * Serialization key
     *
     */
    private static final long serialVersionUID = 1002L;
    /**
     * Message to call thief
     *
     */
    public static final int CALLTHIEF = 1;
    /**
     * Answer of calling
     *
     */
    public static final int RESPCHAMALADRAO = 2;
    /**
     * Message to wait for thieves
     */
    public static final int WAITTHIVES = 3;
    /**
     * Message am i needed
     */
    public static final int AMINEEDED = 4;
    /**
     * Message of thief waiting for orders
     */
    public static final int IMREADY = 5;
    /**
     * Message to check if thief is busy
     */
    public static final int GETBUSYTHIEF = 6;
    /**
     * Answer if thief is busy
     */
    public static final int RESPGETBUSYLADRAO = 7;
    /**
     * Message to get thief group
     */
    public static final int GETGROUPTHIEF = 8;
    /**
     * Answer to get thief group
     */
    public static final int RESPGETGRUPOLADRAO = 9;
    /**
     * Message to get thief id
     */
    public static final int GETTHIEFNUMBER = 12;
    /**
     * Answer to number of thieves
     */
    public static final int RESPGETNRLADROES = 13;
    /**
     * Message to get thief state
     */
    public static final int GETSTATETHIEF = 14;
    /**
     * Answer with thief state
     */
    public static final int RESPGETSTATELADRAO = 15;
    /**
     * Message that thief is back
     */
    public static final int INDICARCHEGADA = 16;
    /**
     * Message to prepare the assault
     */
    public static final int PREPAREEXCURSION = 17;
    /**
     * Message that thief is in the room
     */
    public static final int NASALA = 18;
    /**
     * Thief is comming back from the room
     */
    public static final int REVERSEDIRECTION = 19;
    /**
     * Message to wait for the end of thieves activities
     */
    public static final int WAITTHIEVESEND = 20;
    /**
     * Message to get thief agility
     */
    public static final int GETAGILITY = 21;
    /**
     * Answer with the agility
     */
    public static final int RESPGETAGILITY = 22;
    /**
     * Message to end server
     */
    public static final int END = 99;


    /**
     * Confirmation message
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
     * First boolean argument
     */
    private boolean arg_b1 = false;

    /**
     * Possible message
     * @param msgType message type
     */
    public ConcentrationSiteMessage(int msgType) {
        this.msgType = msgType;
    }

    /**
     * Possible message
     * @param msgType message type
     * @param check boolean message
     */
    public ConcentrationSiteMessage(int msgType, boolean check) {
        this.msgType = msgType;
        this.arg_b1 = check;
    }

    /**
     * Possible message
     * @param msgType message type
     * @param state message with the state
     */
    public ConcentrationSiteMessage(int msgType, int state) {
        this.msgType = msgType;
        this.arg1 = state;
    }

    /**
     * Getter with the message type
     * @return message type
     */
    public int getType() {
        return this.msgType;
    }
    /**
     * Getter with the first argument
     * @return first argument
     */
    public int getArg1() {
        return arg1;
    }
    /**
     * Getter with the first boolean argument
     * @return boolean argument
     */
    public boolean getArg_b1() {
        return arg_b1;
    }
}
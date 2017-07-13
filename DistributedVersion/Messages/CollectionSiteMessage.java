package DistributedVersion.Messages;

import java.io.Serializable;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class CollectionSiteMessage implements Serializable {

    private static final long serialVersionUID = 1001L;

    /**
     * Message to check groups
     */
    public static final int CHECKGROUPS = 0;
    /**
     * Answer to check groups
     */
    public static final int RESPCHECKGRUPOS = 1;

    /**
     * Message to make thief join group
     */
    public static final int JOINGROUP = 2;
    /**
     * Answer to joining group
     */
    public static final int RESPENTRARGRUPO = 3;

    /**
     * Message to check if group is full
     */
    public static final int ISGROUPDFULL = 4;
    /**
     * Answer of full group
     */
    public static final int RESPGRUPOCHEIO = 5;

    /**
     * Message to get master thief state
     */
    public static final int GETMASTERTHIEFSTATE = 6;
    /**
     * Answer with master thief state
     */
    public static final int RESPGETESTADOCHEFE = 7;

    /**
     * Message to start assault
     */
    public static final int STARTOPERATIONS = 8;
    /**
     * Answer of start assault
     */
    public static final int RESPSTARTOPERATIONS = 9;

    /**
     * Message to prepare assault party
     */
    public static final int PREPAREASSAULTPARTY = 10;
    /**
     * Answer of preparation
     */
    public static final int RESPPREPAREASSAULTPARTY = 11;
    /**
     * Message to take a rest
     */
    public static final int TAKEAREST = 14;
    /**
     * Answer of taking a rest
     */
    public static final int RESPTAKEAREST = 15;

    /**
     * Message to get the number of elements in the group
     */
    public static final int GETNRELEMENTSGROUP = 16;
    /**
     * Answer with the number of elements
     */
    public static final int RESPGETNRELEMENTOSGRUPO = 17;
    /**
     * Message to hand a canvas
     */
    public static final int HANDACANVAS = 20;
    /**
     * Answer of handing a canvas
     */
    public static final int RESPHANDACANVAS = 21;

    /**
     * Message of empty room
     */
    public static final int FLAGEMPTYROOM = 22;
    /**
     * Answer with empty room
     */
    public static final int RESPINDICARSALAVAZIA = 23;

    /**
     * Message with the position in the group
     */
    public static final int GETGROUPPOSITION = 24;
    /**
     * Answer with the position
     */
    public static final int RESPGETPOSGRUPO = 25;
    /**
     * Message to check if museum is empty
     */
    public static final int CHECKEMPTYMUSEUM = 26;
    /**
     * Answer if museum is empty
     */
    public static final int RESPCHECKEMPTYMUSEU = 27;

    /**
     * Message to terminate the assault and report the results
     */
    public static final int SUMUPRESULTS = 28;
    /**
     * Answer with the results
     */
    public static final int RESPSUMUPRESULTS = 29;

    /**
     * Message with the number of stolen paintings
     */
    public static final int GETSTOLENPAINTINGS = 30;
    /**
     * Answer with the paintings
     */
    public static final int RESPGETQUADROSROUBADOS = 31;

    /**
     * Message to check empty rooms
     */
    public static final int CHECKEMPTYROOMS = 32;
    /**
     * Answer with empty rooms
     */
    public static final int RESPEMPTYROOMS = 33;

    /**
     * Message with assault room
     */
    public static final int GETASSAULTINGROOM = 36;
    /**
     * Answer with assault room
     */
    public static final int RESPGETSALAASSALTO = 37;
    /**
     * Message to end server
     */
    public static final int END = 99;


    /**
     * Boolean argument
     */
    private boolean bInfo = false;
    /**
     * Message type
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
     * Fourth argument
     */
    private int arg4 = -1;
    /**
     * Get first argument
     * @return argument 1
     */
    public int getArg1() {
        return arg1;
    }
    /**
     * Get second argument
     * @return argument 2
     */
    public int getArg2() {
        return arg2;
    }
    /**
     * Get third argument
     * @return argument 3
     */
    public int getArg3() {
        return arg3;
    }
    /**
     * Get fourth argument
     * @return argument 4
     */
    public int getArg4() {
        return arg4;
    }
    /**
     * Get boolean
     * @return boolea received
     */
    public boolean getBool(){
        return bInfo;
    }


    /**
     * Message type
     * @return msg type
     */
    public int getMsgType(){
        return this.msgType;
    }

    /**
     * Possible Message
     * @param type message type
     * @param arg1 first argument
     */
    public CollectionSiteMessage(int type, int arg1){
        this.msgType = type;
        this.arg1 = arg1;

    }

    /**
     * Possible Message
     * @param type message type
     */
    public CollectionSiteMessage(int type){
        this.msgType = type;
    }

    /**
     * Possible Message
     * @param type message type
     * @param info boolean message
     */
    public CollectionSiteMessage(int type, boolean info){
        this.msgType = type;
        this.bInfo = info;
    }

    /**
     * Possible Message
     * @param type message type
     * @param arg1 argument 1
     * @param arg2 argument 2
     */
    public CollectionSiteMessage(int type, int arg1, int arg2){
        this.msgType = type;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    /**
     * Possible Message
     * @param type message type
     * @param arg1 argument 1
     * @param arg2 argument 2
     * @param arg3 argument 3
     */
    public CollectionSiteMessage(int type, int arg1, int arg2, int arg3){
        this.msgType = type;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }

    /**
     * Possible Message
     * @param type message type
     * @param arg1 argument 1
     * @param arg2 argument 2
     * @param arg3 argument 3
     * @param arg4 argument 4
     */
    public CollectionSiteMessage(int type, int arg1, int arg2, int arg3, int arg4){
        this.msgType = type;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
    }



    /**
     * To string
     * @return string with information
     */
    @Override
    public String toString() {
        return "CollectionSiteMessage{" +
                ", bInfo=" + bInfo +
                ", msgType=" + msgType +
                ", arg1=" + arg1 +
                ", arg2=" + arg2 +
                ", arg3=" + arg3 +
                ", arg4=" + arg4 +
                '}';
    }
}


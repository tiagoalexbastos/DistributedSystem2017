package DistributedVersion.Messages;

/**
 * General Definitions of the problem
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class Constants {


    /**
     * Number of Rooms in the Museum
     */
    public static final int NUM_ROOMS = 5;

    /**
     * Number of Ordinary Thiefs
     */
    public static final int NUM_THIEVES = 6;

    /**
     * Number of Thiefs per group
     */
    public static final int NUM_GROUP = 3;

    /**
     * Max Number of Paitings per room
     */
    public static final int MAX_PAINTS = 16;

    /**
     * Min number of Paitings per room
     */
    public static final int MIN_PAINTS = 8;


    /**
     * Max distance from the room to the CollectionSite
     */
    public static final int MAX_DIST = 30;

    /**
     * Min distance from the room to the CollectionSite
     */
    public static final int MIN_DIST = 15;


    /**
     *  Distance between the thiefs in a line
     */
    public static final int DIST_THIEVES = 3;

    /**
     * Min agility of a thief
     */
    public static final int MIN_AGIL = 2;

    /**
     * Max agility of a thief
     */
    public static final int MAX_AGIL = 6;


    /**
     * State of Ordinary Thieves - Outside
     */
    public static final int OUTSIDE = 0;
    /**
     * State of  Ordinary Thieves - Crawl IN
     */
    public static final int CRAWLING_INWARDS = 1;
    /**
     * State of  Ordinary Thieves - AT a Room
     */
    public static final int AT_A_ROOM = 2;
    /**
     * State of Ordinary Thieves - Crawl Out
     */
    public static final int CRAWLING_OUTWARDS = 3;
    /**
     * State of  Ordinary Thieves - Heist END
     */
    public static final int HEIST_END = 4;


    /**
     * State of  Master Thief - Planning the heist
     */
    public static final int PLANNING_THE_HEIST = 0;
    /**
     * State of  Master Thief - Deciding what to do
     */
    public static final int DECIDING_WHAT_TO_DO = 1;
    /**
     * State of  Master Thief - Assembling a group
     */
    public static final int ASSEMBLING_A_GROUP = 2;
    /**
     * State of  Master Thief - Waiting for group arrival
     */
    public static final int WAITING_FOR_GROUP_ARRIVAL = 3;
    /**
     * State of  Master Thief - Presenting the report
     */
    public static final int PRESENTING_THE_REPORT = 4;
    /**
     *Situation of Ordinary Thief - Waiting
     */
    public static final int WAITING = 0;
    /**
     * Situation of Ordinary Thief - In Party
     */
    public static final int IN_PARTY = 1;

}

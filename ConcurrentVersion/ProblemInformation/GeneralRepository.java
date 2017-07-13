package ConcorrentVersion.ProblemInformation;

import static ConcorrentVersion.ProblemInformation.Constantes.*;
import genclass.GenericIO;
import genclass.TextFile;

/**
 * Log system
 * Writes to Log.txt
 * @author Pedro Matos and Tiago Bastos
 */
public class GeneralRepository {

    /**
     * Array with each room's distances to the CollectionSite
     */
    private int[] distanciaSala;
    /**
     * Array with the number of paitings in each room
     */
    private int[] nrQuadrosSala;
    /**
     * String with the name of the text file
     */
    private String fileName2;
    /**
     * File with the log
     */
    private TextFile log = new TextFile();
    /**
     * String with the states to check for repetitions
     */
    private String last_1 = "";
    /**
     * String with the positions to check for repetitions
     */
    private String last_2 = "";
    /**
     * Array with each thief state
     */
    private int[] thief_state = new int[NUM_THIEVES];
    /**
     * Array with the situations of each thief
     */
    private int[] thief_situation = new int[NUM_THIEVES];
    /**
     * Array with the agility of each thief
     */
    private int[] thief_displacement = new int[NUM_THIEVES];
    /**
     * Master thief state
     */
    private int masther_thief = PLANNING_THE_HEIST;
    /**
     * Array with Master Thief's possible states
     */
    private String master_thief_word[]= {"PLAN","DECI","ASSE","WAIT","PRES"};
    /**
     * Array with Ordinary Thief's possible states
     */
    private String thief_word[]= {"OUTS","CWIN","ATRO","COUT","HEND"};
    /**
     * Array with Ordinary Thief's possible situations
     */
    private String thief_situation_word[] = {"W","P"};
    /**
     * ID of the room to be assalted
     */
    private char assault_party1_room = '-';
    /**
     * ID of the room to be assalted
     */
    private char assault_party2_room = '-';
    /**
     * Array with the Ordinary Thief's positions
     */
    private String[] assault_party1_thief_pos = new String[NUM_GROUP];
    /**
     * Array with the id's from the first assault party
     */
    private char[] assault_party1_thief_id = new char[NUM_GROUP];
    /**
     * Array with a flag about the paitings
     */
    private char[] assault_party1_thief_canvas = new char[NUM_GROUP];
    /**
     * Array with the Ordinary Thief's positions
     */
    private String[] assault_party2_thief_pos = new String[NUM_GROUP];
    /**
     * Array with the id's from the second assault party
     */
    private char[] assault_party2_thief_id = new char[NUM_GROUP];
    /**
     * Array with a flag about the paitings
     */
    private char[] assault_party2_thief_canvas = new char[NUM_GROUP];


    /**
     * All information is in the initial state.
     * The changes will only occur by the use of sets functions.
     */

    public GeneralRepository() {
        distanciaSala = new int[NUM_ROOMS];
        nrQuadrosSala = new int[NUM_ROOMS];
        this.fileName2 = "Log.txt";

        for(int i = 0; i < nrQuadrosSala.length; i++){
            distanciaSala[i] = -1;
            nrQuadrosSala[i] = -1;
        }

        for(int i = 0; i < thief_state.length; i++){
            thief_state[i] = OUTSIDE;
            thief_situation[i] = WAITING;
            thief_displacement[i] = -1;
        }

        for(int i = 0; i < assault_party1_thief_pos.length;i++){
            assault_party1_thief_pos[i] = "--";
            assault_party1_thief_id[i] = '-';
            assault_party1_thief_canvas[i] = '-';

            assault_party2_thief_pos[i] = "--";
            assault_party2_thief_id[i] = '-';
            assault_party2_thief_canvas[i] = '-';
        }
    }

    /**
     * The first lines to be written in the file.
     */

    public synchronized void iniciarLog() {
        if (!log.openForWriting(".", fileName2)) {
            GenericIO.writelnString("Operation " + fileName2 + " failed!");
            System.exit(1);
        }


        log.writelnString("\t\t\t\t\t\t\t Heist to the Museum - Description of the internal state\n");
        log.writelnString("MstT   Thief 1      Thief 2      Thief 3      Thief 4      Thief 5      Thief 6");
        log.writelnString("Stat  Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD");
        log.writelnString("\t\t\t\t   Assault Party 1                       Assault Party 2                       Museum");
        log.writelnString("           Elem 1     Elem 2     Elem 3          Elem 1     Elem 2     Elem 3   Room 1  Room 2  Room 3  Room 4  Room 5");
        log.writelnString("    RId  Id Pos Cv  Id Pos Cv  Id Pos Cv  RId  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   NP DT   NP DT");
        String nova_1 = master_thief_word[masther_thief]+"  "
                +thief_word[thief_state[0]]+" "+thief_situation_word[thief_situation[0]]+"  "+ thief_displacement[0]+
                "    "+thief_word[thief_state[1]]+" "+thief_situation_word[thief_situation[1]]+"  "+ thief_displacement[1]+
                "    "+thief_word[thief_state[2]]+" "+thief_situation_word[thief_situation[2]]+"  "+ thief_displacement[2]+
                "    "+thief_word[thief_state[3]]+" "+thief_situation_word[thief_situation[3]]+"  "+ thief_displacement[3]+
                "    "+thief_word[thief_state[4]]+" "+thief_situation_word[thief_situation[4]]+"  "+ thief_displacement[4]+
                "    "+thief_word[thief_state[5]]+" "+thief_situation_word[thief_situation[5]]+"  "+ thief_displacement[5];



        String nova_2 =  "     "+assault_party1_room +"    "+ (assault_party1_thief_id[0])+"  " +String.format("%2s",assault_party1_thief_pos[0])+"  " + assault_party1_thief_canvas[0]+
                "   "+ (assault_party1_thief_id[1])+"  "+ String.format("%2s",assault_party1_thief_pos[1]) +"  "+ assault_party1_thief_canvas[1]+
                "   "+ (assault_party1_thief_id[2])+"  "+ String.format("%2s",assault_party1_thief_pos[2]) +"  "+ assault_party1_thief_canvas[2]+
                "   "+assault_party2_room +"    "+ (assault_party2_thief_id[0])+"  "+ String.format("%2s",assault_party2_thief_pos[0]) +"  "+ assault_party2_thief_canvas[0]+
                "   "+ (assault_party2_thief_id[1])+"  "+ String.format("%2s",assault_party2_thief_pos[1]) +"  "+  assault_party2_thief_canvas[1]+
                "   "+ (assault_party2_thief_id[2])+"  "+ String.format("%2s",assault_party2_thief_pos[2]) +"  "+  assault_party2_thief_canvas[2]+
                "   " +String.format("%02d",nrQuadrosSala[0])+" "+ String.format("%02d",distanciaSala[0]) +
                "   " +String.format("%02d",nrQuadrosSala[1])+" "+ String.format("%02d",distanciaSala[1]) +
                "   " +String.format("%02d",nrQuadrosSala[2])+" "+ String.format("%02d",distanciaSala[2]) +
                "   " +String.format("%02d",nrQuadrosSala[3])+" "+ String.format("%02d",distanciaSala[3]) +
                "   " +String.format("%02d",nrQuadrosSala[4])+" "+ String.format("%02d",distanciaSala[4]);


        log.writelnString(nova_1);
        log.writelnString(nova_2);



        if (!log.close()) {
            GenericIO.writelnString("Operation " + fileName2 + " failed!");
            System.exit(1);
        }
    }

    /**
     * Function that writes when a change occurs. It checks for repetitions and do not allow them.
     */

    public synchronized void add_log(){
        if (!log.openForAppending(".", fileName2)) {
            GenericIO.writelnString("Operation " + fileName2 + " failed!");
            System.exit(1);
        }

        String nova_1 = master_thief_word[masther_thief]+"  "
                +thief_word[thief_state[0]]+" "+thief_situation_word[thief_situation[0]]+"  "+ thief_displacement[0]+
                "    "+thief_word[thief_state[1]]+" "+thief_situation_word[thief_situation[1]]+"  "+ thief_displacement[1]+
                "    "+thief_word[thief_state[2]]+" "+thief_situation_word[thief_situation[2]]+"  "+ thief_displacement[2]+
                "    "+thief_word[thief_state[3]]+" "+thief_situation_word[thief_situation[3]]+"  "+ thief_displacement[3]+
                "    "+thief_word[thief_state[4]]+" "+thief_situation_word[thief_situation[4]]+"  "+ thief_displacement[4]+
                "    "+thief_word[thief_state[5]]+" "+thief_situation_word[thief_situation[5]]+"  "+ thief_displacement[5];



        String nova_2 =  "     "+assault_party1_room +"    "+ (assault_party1_thief_id[0])+"  " +String.format("%2s",assault_party1_thief_pos[0])+"  " + assault_party1_thief_canvas[0]+
                "   "+ (assault_party1_thief_id[1])+"  "+ String.format("%2s",assault_party1_thief_pos[1]) +"  "+ assault_party1_thief_canvas[1]+
                "   "+ (assault_party1_thief_id[2])+"  "+ String.format("%2s",assault_party1_thief_pos[2]) +"  "+ assault_party1_thief_canvas[2]+
                "   "+assault_party2_room +"    "+ (assault_party2_thief_id[0])+"  "+ String.format("%2s",assault_party2_thief_pos[0]) +"  "+ assault_party2_thief_canvas[0]+
                "   "+ (assault_party2_thief_id[1])+"  "+ String.format("%2s",assault_party2_thief_pos[1]) +"  "+  assault_party2_thief_canvas[1]+
                "   "+ (assault_party2_thief_id[2])+"  "+ String.format("%2s",assault_party2_thief_pos[2]) +"  "+  assault_party2_thief_canvas[2]+
                "   " +String.format("%02d",nrQuadrosSala[0])+" "+ String.format("%02d",distanciaSala[0]) +
                "   " +String.format("%02d",nrQuadrosSala[1])+" "+ String.format("%02d",distanciaSala[1]) +
                "   " +String.format("%02d",nrQuadrosSala[2])+" "+ String.format("%02d",distanciaSala[2]) +
                "   " +String.format("%02d",nrQuadrosSala[3])+" "+ String.format("%02d",distanciaSala[3]) +
                "   " +String.format("%02d",nrQuadrosSala[4])+" "+ String.format("%02d",distanciaSala[4]);


        if (!last_1.equals(nova_1) || !last_2.equals(nova_2)){
            log.writelnString(nova_1);
            log.writelnString(nova_2);
            last_1 = nova_1;
            last_2 = nova_2;
        }


        if (!log.close()) {
            GenericIO.writelnString("Operation  " + fileName2 + " failed!");
            System.exit(1);
        }
    }


    /**
     * Function called to finish the report. It shows the number of stollen paitings.
     * @param total total of paitings
     */

    public synchronized void finalizarRelatorio(int total) {
        assault_party1_room = '-';
        assault_party2_room = '-';
        add_log();

        if (!log.openForAppending(".", fileName2)) {
            GenericIO.writelnString("Operation " + fileName2 + " failed!");
            System.exit(1);
        }


        log.writelnString("My friends, a total amount of "+total+ " canvasses were collected!" );
        if (!log.close()) {
            GenericIO.writelnString("Operation " + fileName2 + " failed!");
            System.exit(1);
        }

    }


    /**
     * Set for the distance
     * @param nrSala id of room
     * @param distancia distance from the room to the CollectionSite
     */
    public synchronized void setDistanciaSala(int nrSala, int distancia) {
        this.distanciaSala[nrSala] = distancia;
    }


    /**
     * Set for the number of paitings
     * @param nrSala id of room
     * @param nrQuadrosSala number of paitings
     */
    public synchronized void setNrQuadrosSala(int nrSala, int nrQuadrosSala) {
        this.nrQuadrosSala[nrSala] = nrQuadrosSala;
        add_log();
    }

    /**
     * Set for Master Thief state
     * @param state Master Thief state
     */
    public synchronized void setMasterThiefState(int state){
        this.masther_thief = state;
        //add_log();
    }

    /**
     * Set for Ordinary Thief state
     * @param id id of thief
     * @param state state of thief
     */
    public synchronized void setThiefState(int id, int state){

        this.thief_state[id] = state;
        //add_log();
    }

    /**
     * Set for Ordinary Thief situation
     * @param id id of thief
     * @param situation situation of thief
     */
    public synchronized void setThiefSituation(int id, int situation){
        this.thief_situation[id] = situation;
        //add_log();
    }

    /**
     * Ordinary Thief Agility
     * @param id id of thief
     * @param disp agility of thief
     */
    public synchronized void setThiefDisplacement(int id, int disp){
        this.thief_displacement[id] = disp;
        add_log();
    }

    /**
     * Set room for assault party n1
     * @param room id of room
     */
    public synchronized void setAssaultParty1_room(int room){

        room++;
        this.assault_party1_room = Integer.toString(room).charAt(0);
        add_log();
    }

    /**
     * Set room for assault party n2
     * @param room id of room
     */
    public synchronized void setAssaultParty2_room(int room){
        room++;
        this.assault_party2_room = Integer.toString(room).charAt(0);
        add_log();
    }

    /**
     * Set Ordinary Thief's position
     * @param pos_grupo thief position in group
     * @param pos thief position to the room
     */
    public synchronized void setAP1_pos(int pos_grupo, int pos){
        assault_party1_thief_pos[pos_grupo] = Integer.toString(pos);
        add_log();
    }

    /**
     * Set for the presence of paiting
     * @param pos_grupo thief position in group
     * @param cv paiting
     */
    public synchronized void setAP1_canvas(int pos_grupo, boolean cv, int room){
        if (cv){
            assault_party1_thief_canvas[pos_grupo] = '1';
            nrQuadrosSala[room]--;
        }
        else assault_party1_thief_canvas[pos_grupo] = '0';

        add_log();
    }

    /**
     * Multiple Set
     * @param pos_grupo thief position in group
     * @param id thief's id
     * @param pos thief's position
     * @param cv paiting
     */
    public synchronized void setAP1_pos_id_canvas(int pos_grupo, int id, int pos,boolean cv){
        assault_party1_thief_pos[pos_grupo] = Integer.toString(pos);
        id++;
        assault_party1_thief_id[pos_grupo] = Integer.toString(id).charAt(0);

        if (cv) assault_party1_thief_canvas[pos_grupo] = '1';
        else assault_party1_thief_canvas[pos_grupo] = '0';

        add_log();
    }

    /**
     * Reset
     * @param pos_grupo thief position in group
     * @param id thief's id
     */
    public synchronized void setAP1_reset(int pos_grupo, int id){
        assault_party1_thief_pos[pos_grupo] = "-";
        assault_party1_thief_canvas[pos_grupo] = '-';
        assault_party1_thief_id[pos_grupo] = '-';
        thief_situation[id] = WAITING;
        add_log();
    }


    /**
     * Set position
     * @param pos_grupo thief position in group
     * @param pos position to the room
     */
    public synchronized void setAP2_pos(int pos_grupo, int pos){
        assault_party2_thief_pos[pos_grupo] = String.valueOf(pos);
        add_log();
    }


    /**
     * Set paiting
     * @param pos_grupo thief position in group
     * @param cv paiting
     */
    public synchronized void setAP2_canvas(int pos_grupo, boolean cv, int room){
        if (cv){
            assault_party2_thief_canvas[pos_grupo] = '1';
            nrQuadrosSala[room]--;
        }
        else assault_party2_thief_canvas[pos_grupo] = '0';

        add_log();
    }


    /**
     * Multiple Set
     * @param pos_grupo thief position in group
     * @param id thief's id
     * @param pos thief's position
     * @param cv paiting
     */
    public synchronized void setAP2_pos_id_canvas(int pos_grupo, int id, int pos, boolean cv){
        assault_party2_thief_pos[pos_grupo] = Integer.toString(pos);
        id++;
        assault_party2_thief_id[pos_grupo] = Integer.toString(id).charAt(0);

        if (cv) assault_party2_thief_canvas[pos_grupo] = '1';
        else assault_party2_thief_canvas[pos_grupo] = '0';

        add_log();
    }


    /**
     *  Reset
     * @param pos_grupo thief position in group
     * @param id thief's id
     */
    public synchronized void setAP2_reset(int pos_grupo, int id){
        assault_party2_thief_pos[pos_grupo] = "-";
        assault_party2_thief_canvas[pos_grupo] = '-';
        assault_party2_thief_id[pos_grupo] = '-';
        thief_situation[id] = WAITING;
        add_log();
    }

}

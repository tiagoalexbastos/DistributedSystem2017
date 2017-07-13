package serverSide.generalRepository;

import genclass.GenericIO;
import genclass.TextFile;
import interfaces.*;
import registry.RegistryConfig;
import support.Constantes;
import support.LineUpdate;
import support.VectorTimestamp;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static support.Constantes.*;

/**
 * Log system
 * Writes to Log.txt
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class GeneralRepository implements GeneralRepositoryInterface {

    /**
     * Array with each room's distances to the CollectionSite
     */
    private int[] roomDistance;
    /**
     * Array with the number of paintings in each room
     */
    private int[] numberRoomPaintings;
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

    private int numberEntitiesRunning = 2;
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
    private String master_thief_word[] = {"PLAN", "DECI", "ASSE", "WAIT", "PRES"};
    /**
     * Array with Ordinary Thief's possible states
     */
    private String thief_word[] = {"OUTS", "CWIN", "ATRO", "COUT", "HEND"};
    /**
     * Array with Ordinary Thief's possible situations
     */
    private String thief_situation_word[] = {"W", "P"};
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
     * Local Vector Timestamp
     */
    private VectorTimestamp local;

    /**
     * RMI Register host name
     */
    private String rmiRegHostName;

    /**
     * RMI Register host name
     */
    private int rmiRegPortNumb;

    /**
     * Ordering List
     */
    private final List<LineUpdate> updates;

    /**
     * Ordered Lof file printer
     */
    private PrintWriter printer;

    /**
     * Ready to print
     */
    private boolean ready = false;





    /**
     * All information is in the initial state.
     * The changes will only occur by the use of sets functions.
     * @param rmiRegHostName RMI Register host name
     * @param rmiRegPortNumb RMI Register port number
     */
    public GeneralRepository(String rmiRegHostName, int rmiRegPortNumb) {
        this.rmiRegHostName = rmiRegHostName;
        this.rmiRegPortNumb = rmiRegPortNumb;
        roomDistance = new int[NUM_ROOMS];
        numberRoomPaintings = new int[NUM_ROOMS];

        local = new VectorTimestamp(Constantes.VECTOR_TIMESTAMP_SIZE, 0);


        this.fileName2 = "Log.txt";

        for (int i = 0; i < numberRoomPaintings.length; i++) {
            roomDistance[i] = -1;
            numberRoomPaintings[i] = -1;
        }

        for (int i = 0; i < thief_state.length; i++) {
            thief_state[i] = OUTSIDE;
            thief_situation[i] = WAITING;
            thief_displacement[i] = -1;
        }

        for (int i = 0; i < assault_party1_thief_pos.length; i++) {
            assault_party1_thief_pos[i] = "--";
            assault_party1_thief_id[i] = '-';
            assault_party1_thief_canvas[i] = '-';

            assault_party2_thief_pos[i] = "--";
            assault_party2_thief_id[i] = '-';
            assault_party2_thief_canvas[i] = '-';
        }

        try {
            printer = new PrintWriter("LogOrdenado.txt");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        local = new VectorTimestamp(Constantes.VECTOR_TIMESTAMP_SIZE, 0);
        updates = new ArrayList<>();
    }

    /**
     * The first lines to be written in the file.
     *
     * @param vectorTimestamp clock
     */
    @Override
    public synchronized void startLog(VectorTimestamp vectorTimestamp) {
        if (!log.openForWriting(".", fileName2)) {
            GenericIO.writelnString("Operation " + fileName2 + " failed!");
            System.exit(1);
        }

        log.writelnString("\t\t\t\t\t\t\t Heist to the Museum - Description of the internal state\n");
        log.writelnString("MstT   Thief 1      Thief 2      Thief 3      Thief 4      Thief 5      Thief 6                VCk");
        log.writelnString("Stat  Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    0   1   2   3   4   5   6");
        log.writelnString("\t\t\t\t   Assault Party 1                       Assault Party 2                       Museum");
        log.writelnString("           Elem 1     Elem 2     Elem 3          Elem 1     Elem 2     Elem 3   Room 1  Room 2  Room 3  Room 4  Room 5");
        log.writelnString("    RId  Id Pos Cv  Id Pos Cv  Id Pos Cv  RId  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   NP DT   NP DT");
        printer.print("\t\t\t\t\t\t\t Heist to the Museum - Description of the internal state\n\n"+"MstT   Thief 1      Thief 2      Thief 3      Thief 4      Thief 5      Thief 6                VCk"+"\n"+
                "Stat  Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    0   1   2   3   4   5   6"+"\n"+"\t\t\t\t   Assault Party 1                       Assault Party 2                       Museum"+"\n"+
                "           Elem 1     Elem 2     Elem 3          Elem 1     Elem 2     Elem 3   Room 1  Room 2  Room 3  Room 4  Room 5"+"\n"+"    RId  Id Pos Cv  Id Pos Cv  Id Pos Cv  RId  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   NP DT   NP DT"+"\n");
        String nova_1 = master_thief_word[masther_thief] + "  "
                + thief_word[thief_state[0]] + " " + thief_situation_word[thief_situation[0]] + "  " + thief_displacement[0] +
                "    " + thief_word[thief_state[1]] + " " + thief_situation_word[thief_situation[1]] + "  " + thief_displacement[1] +
                "    " + thief_word[thief_state[2]] + " " + thief_situation_word[thief_situation[2]] + "  " + thief_displacement[2] +
                "    " + thief_word[thief_state[3]] + " " + thief_situation_word[thief_situation[3]] + "  " + thief_displacement[3] +
                "    " + thief_word[thief_state[4]] + " " + thief_situation_word[thief_situation[4]] + "  " + thief_displacement[4] +
                "    " + thief_word[thief_state[5]] + " " + thief_situation_word[thief_situation[5]] + "  " + thief_displacement[5] + "   " + printVectorTimeStamp(vectorTimestamp);


        String nova_2 = "     " + assault_party1_room + "    " + (assault_party1_thief_id[0]) + "  " + String.format("%2s", assault_party1_thief_pos[0]) + "  " + assault_party1_thief_canvas[0] +
                "   " + (assault_party1_thief_id[1]) + "  " + String.format("%2s", assault_party1_thief_pos[1]) + "  " + assault_party1_thief_canvas[1] +
                "   " + (assault_party1_thief_id[2]) + "  " + String.format("%2s", assault_party1_thief_pos[2]) + "  " + assault_party1_thief_canvas[2] +
                "   " + assault_party2_room + "    " + (assault_party2_thief_id[0]) + "  " + String.format("%2s", assault_party2_thief_pos[0]) + "  " + assault_party2_thief_canvas[0] +
                "   " + (assault_party2_thief_id[1]) + "  " + String.format("%2s", assault_party2_thief_pos[1]) + "  " + assault_party2_thief_canvas[1] +
                "   " + (assault_party2_thief_id[2]) + "  " + String.format("%2s", assault_party2_thief_pos[2]) + "  " + assault_party2_thief_canvas[2] +
                "   " + String.format("%02d", numberRoomPaintings[0]) + " " + String.format("%02d", roomDistance[0]) +
                "   " + String.format("%02d", numberRoomPaintings[1]) + " " + String.format("%02d", roomDistance[1]) +
                "   " + String.format("%02d", numberRoomPaintings[2]) + " " + String.format("%02d", roomDistance[2]) +
                "   " + String.format("%02d", numberRoomPaintings[3]) + " " + String.format("%02d", roomDistance[3]) +
                "   " + String.format("%02d", numberRoomPaintings[4]) + " " + String.format("%02d", roomDistance[4]);


        log.writelnString(nova_1);
        log.writelnString(nova_2);
        printer.println(nova_1);
        printer.println(nova_2);


        ready = true;


        if (!log.close()) {
            GenericIO.writelnString("Operation " + fileName2 + " failed!");
            System.exit(1);
        }
    }

    /**
     * Function that writes when a change occurs. It checks for repetitions and do not allow them.
     *
     * @param vectorTimestamp clock
     */
    @Override
    public synchronized void add_log(VectorTimestamp vectorTimestamp) {
        if (!log.openForAppending(".", fileName2)) {
            GenericIO.writelnString("Operation " + fileName2 + " failed!");
            System.exit(1);
        }

        String nova_1 = master_thief_word[masther_thief] + "  "
                + thief_word[thief_state[0]] + " " + thief_situation_word[thief_situation[0]] + "  " + thief_displacement[0] +
                "    " + thief_word[thief_state[1]] + " " + thief_situation_word[thief_situation[1]] + "  " + thief_displacement[1] +
                "    " + thief_word[thief_state[2]] + " " + thief_situation_word[thief_situation[2]] + "  " + thief_displacement[2] +
                "    " + thief_word[thief_state[3]] + " " + thief_situation_word[thief_situation[3]] + "  " + thief_displacement[3] +
                "    " + thief_word[thief_state[4]] + " " + thief_situation_word[thief_situation[4]] + "  " + thief_displacement[4] +
                "    " + thief_word[thief_state[5]] + " " + thief_situation_word[thief_situation[5]] + "  " + thief_displacement[5];


        String nova_2 = "     " + assault_party1_room + "    " + (assault_party1_thief_id[0]) + "  " + String.format("%2s", assault_party1_thief_pos[0]) + "  " + assault_party1_thief_canvas[0] +
                "   " + (assault_party1_thief_id[1]) + "  " + String.format("%2s", assault_party1_thief_pos[1]) + "  " + assault_party1_thief_canvas[1] +
                "   " + (assault_party1_thief_id[2]) + "  " + String.format("%2s", assault_party1_thief_pos[2]) + "  " + assault_party1_thief_canvas[2] +
                "   " + assault_party2_room + "    " + (assault_party2_thief_id[0]) + "  " + String.format("%2s", assault_party2_thief_pos[0]) + "  " + assault_party2_thief_canvas[0] +
                "   " + (assault_party2_thief_id[1]) + "  " + String.format("%2s", assault_party2_thief_pos[1]) + "  " + assault_party2_thief_canvas[1] +
                "   " + (assault_party2_thief_id[2]) + "  " + String.format("%2s", assault_party2_thief_pos[2]) + "  " + assault_party2_thief_canvas[2] +
                "   " + String.format("%02d", numberRoomPaintings[0]) + " " + String.format("%02d", roomDistance[0]) +
                "   " + String.format("%02d", numberRoomPaintings[1]) + " " + String.format("%02d", roomDistance[1]) +
                "   " + String.format("%02d", numberRoomPaintings[2]) + " " + String.format("%02d", roomDistance[2]) +
                "   " + String.format("%02d", numberRoomPaintings[3]) + " " + String.format("%02d", roomDistance[3]) +
                "   " + String.format("%02d", numberRoomPaintings[4]) + " " + String.format("%02d", roomDistance[4]);



        if (!last_1.equals(nova_1) || !last_2.equals(nova_2)) {
            log.writelnString(nova_1 + "   " + printVectorTimeStamp(vectorTimestamp));
            log.writelnString(nova_2);
            last_1 = nova_1;
            last_2 = nova_2;
            if(ready)
                updates.add(new LineUpdate(last_1 + "   " + printVectorTimeStamp(vectorTimestamp.clone()) + "\n" + last_2, vectorTimestamp.clone()));
        }


        if (!log.close()) {
            GenericIO.writelnString("Operation  " + fileName2 + " failed!");
            System.exit(1);
        }
    }


    /**
     * Function called to finish the report. It shows the number of stollen paitings.
     *
     * @param total           total of paitings
     * @param vectorTimestamp clock
     */
    @Override
    public synchronized void endReport(int total, VectorTimestamp vectorTimestamp) {

        assault_party1_room = '-';
        assault_party2_room = '-';
        add_log(vectorTimestamp);

        if (!log.openForAppending(".", fileName2)) {
            GenericIO.writelnString("Operation " + fileName2 + " failed!");
            System.exit(1);
        }

        Collections.sort(updates);
        for(LineUpdate up : updates)
            printer.print(up.getLine() + "\n");
        printer.print("My friends, a total amount of " + total + " canvasses were collected!");

        log.writelnString("My friends, a total amount of " + total + " canvasses were collected!");
        if (!log.close()) {
            GenericIO.writelnString("Operation " + fileName2 + " failed!");
            System.exit(1);
        }




        printer.flush();
        printer.close();

    }


    /**
     * Set for the distance
     *
     * @param nrSala          id of room
     * @param distancia       distance from the room to the CollectionSite
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp setRoomDistance(int nrSala, int distancia, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        this.roomDistance[nrSala] = distancia;
        return local.clone();
    }


    /**
     * Set for the number of paitings
     *
     * @param nrSala          id of room
     * @param nrQuadrosSala   number of paitings
     * @param vectorTimestamp clock
     * @return clock
     * */
    @Override
    public synchronized VectorTimestamp setNumberofRoomPaintings(int nrSala, int nrQuadrosSala, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        this.numberRoomPaintings[nrSala] = nrQuadrosSala;
        add_log(local.clone());
        return local.clone();
    }

    /**
     * Set for Master Thief state
     *
     * @param state           Master Thief state
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp setMasterThiefState(int state, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        this.masther_thief = state;
        //add_log();
        return local.clone();
    }

    /**
     * Set for Ordinary Thief state
     *
     * @param id              id of thief
     * @param state           state of thief
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp setThiefState(int id, int state, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        this.thief_state[id] = state;
        //add_log();
        return local.clone();
    }

    /**
     * Set for Ordinary Thief situation
     *
     * @param id              id of thief
     * @param situation       situation of thief
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp setThiefSituation(int id, int situation, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        this.thief_situation[id] = situation;
        //add_log();
        return local.clone();
    }

    /**
     * Ordinary Thief Agility
     *  @param id              id of thief
     * @param disp            agility of thief
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp setThiefDisplacement(int id, int disp, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        this.thief_displacement[id] = disp;
        add_log(local.clone());
        return local.clone();
    }

    /**
     * Set room for assault party n1
     *
     * @param room            id of room
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp setAssaultParty1_room(int room, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        room++;
        this.assault_party1_room = Integer.toString(room).charAt(0);
        add_log(local.clone());
        return local.clone();
    }

    /**
     * Set room for assault party n2
     *  @param room            id of room
     * @param vectorTimestamp clock
     */
    @Override
    public synchronized VectorTimestamp setAssaultParty2_room(int room, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        room++;
        this.assault_party2_room = Integer.toString(room).charAt(0);
        add_log(local.clone());
        return local.clone();
    }

    /**
     * Set Ordinary Thief's position
     *
     * @param pos_grupo       thief position in group
     * @param pos             thief position to the room
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp setAP1_pos(int pos_grupo, int pos, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        assault_party1_thief_pos[pos_grupo] = Integer.toString(pos);
        add_log(local.clone());
        return local.clone();
    }

    /**
     * Set for the presence of paiting
     *
     * @param pos_grupo       thief position in group
     * @param cv              paiting
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp setAP1_canvas(int pos_grupo, boolean cv, int room, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        if (cv) {
            assault_party1_thief_canvas[pos_grupo] = '1';
            numberRoomPaintings[room]--;
        } else assault_party1_thief_canvas[pos_grupo] = '0';

        add_log(local.clone());
        return local.clone();
    }

    /**
     * Multiple Set
     *
     * @param pos_grupo       thief position in group
     * @param id              thief's id
     * @param pos             thief's position
     * @param cv              paiting
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp setAP1_pos_id_canvas(int pos_grupo, int id, int pos, boolean cv, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        assault_party1_thief_pos[pos_grupo] = Integer.toString(pos);
        id++;
        assault_party1_thief_id[pos_grupo] = Integer.toString(id).charAt(0);

        if (cv) assault_party1_thief_canvas[pos_grupo] = '1';
        else assault_party1_thief_canvas[pos_grupo] = '0';

        add_log(local.clone());
        return local.clone();
    }

    /**
     * Reset
     *  @param pos_grupo       thief position in group
     * @param id              thief's id
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp setAP1_reset(int pos_grupo, int id, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        assault_party1_thief_pos[pos_grupo] = "-";
        assault_party1_thief_canvas[pos_grupo] = '-';
        assault_party1_thief_id[pos_grupo] = '-';
        thief_situation[id] = WAITING;
        add_log(local.clone());
        return local.clone();
    }


    /**
     * Set position
     *  @param pos_grupo       thief position in group
     * @param pos             position to the room
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp setAP2_pos(int pos_grupo, int pos, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        assault_party2_thief_pos[pos_grupo] = String.valueOf(pos);
        add_log(local.clone());
        return local.clone();
    }


    /**
     * Set paiting
     *  @param pos_grupo       thief position in group
     * @param cv              paiting
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp setAP2_canvas(int pos_grupo, boolean cv, int room, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        if (cv) {
            assault_party2_thief_canvas[pos_grupo] = '1';
            numberRoomPaintings[room]--;
        } else assault_party2_thief_canvas[pos_grupo] = '0';

        add_log(local.clone());
        return local.clone();
    }


    /**
     * Multiple Set
     *  @param pos_grupo       thief position in group
     * @param id              thief's id
     * @param pos             thief's position
     * @param cv              paiting
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp setAP2_pos_id_canvas(int pos_grupo, int id, int pos, boolean cv, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        assault_party2_thief_pos[pos_grupo] = Integer.toString(pos);
        id++;
        assault_party2_thief_id[pos_grupo] = Integer.toString(id).charAt(0);

        if (cv) assault_party2_thief_canvas[pos_grupo] = '1';
        else assault_party2_thief_canvas[pos_grupo] = '0';

        add_log(local.clone());
        return local.clone();
    }


    /**
     * Reset
     *  @param pos_grupo       thief position in group
     * @param id              thief's id
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp setAP2_reset(int pos_grupo, int id, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        assault_party2_thief_pos[pos_grupo] = "-";
        assault_party2_thief_canvas[pos_grupo] = '-';
        assault_party2_thief_id[pos_grupo] = '-';
        thief_situation[id] = WAITING;
        add_log(local.clone());
        return local.clone();
    }

    @Override
    public void finished() {
        numberEntitiesRunning--;
        if (numberEntitiesRunning > 0) {
            return;
        }
        terminateServers();
    }

    private void terminateServers() {
        Register reg = null;
        Registry registry = null;
        String rmiRegHostName;
        int rmiRegPortNumb;

        rmiRegHostName = this.rmiRegHostName;
        rmiRegPortNumb = this.rmiRegPortNumb;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            System.out.println("Erro na localização do registo");
            ex.printStackTrace();
            System.exit(1);
        }

        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.RMI_REGISTRY_GENREPO_NAME;

        // shutdown Museum
        try {
            MuseumInterface museum = (MuseumInterface) registry.lookup(RegistryConfig.RMI_REGISTRY_MUSEUM_NAME);
            museum.signalShutdown();
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating Museum: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Museum is not registered: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        // shutdown concentration Site
        try {
            ConcentrationSiteInterface concentrationSiteInterface = (ConcentrationSiteInterface)
                    registry.lookup(RegistryConfig.RMI_REGISTRY_CONSITE_NAME);
            concentrationSiteInterface.signalShutdown();
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating concentration site: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("concentration Site is not registered: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        // shutdown assault groups
        try {
            AssaultPartyManagerInterface assaultPartyManagerInterface = (AssaultPartyManagerInterface)
                    registry.lookup(RegistryConfig.RMI_REGISTRY_ASSGMAN_NAME);
            assaultPartyManagerInterface.signalShutdown();
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating assault group: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println(" assault group  is not registered: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        //shutdown collection site
        try {
            CollectionSiteInterface collectionSiteInterface = (CollectionSiteInterface)
                    registry.lookup(RegistryConfig.RMI_REGISTRY_COLSITE_NAME);
            collectionSiteInterface.signalShutdown();
        } catch (RemoteException e) {
            System.out.println("Exception thrown while locating collection site: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("collection site is not registered: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        // shutdown log

        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            // Unregister ourself
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Log registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Log not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            // Unexport; this will also remove us from the RMI runtime
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

//        writeEnd();

        System.out.println("General Repository closed.");
    }

    private String printVectorTimeStamp(VectorTimestamp vt) {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);

        int[] vtArray = vt.toIntArray();

        for(int i = 0; i < vtArray.length; i++)
            formatter.format("%3d ", vtArray[i]);

        return strb.toString();
    }


}

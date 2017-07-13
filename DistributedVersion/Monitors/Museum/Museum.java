package DistributedVersion.Monitors.Museum;

import static DistributedVersion.ComInfo.ComPorts.portGeneralRepo;
import static DistributedVersion.Messages.Constants.*;

import DistributedVersion.Messages.GeneralRepositoryMessage;
import DistributedVersion.ComInfo.ClientCom;

import java.util.Random;

/**
 * Monitor Museum
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class Museum {

    /**
     * Rooms of the museum
     */
    int[][] rooms = new int[NUM_ROOMS][2];
    /**
     * General Repository
     */
    ClientCom generalRepository;


    /**
     * @param generalRepository General Repository
     */
    public Museum(String generalRepository){

        this.generalRepository = new ClientCom(generalRepository, portGeneralRepo);

        for (int i = 0; i < rooms.length; i++) {
            Random r = new Random();
            rooms[i][0] = r.nextInt((MAX_PAINTS+1)-MIN_PAINTS) + MIN_PAINTS;
            setNumberofPaintings(i, rooms[i][0]);
            Random r2 = new Random();

            rooms[i][1] = r2.nextInt((MAX_DIST+1)-MIN_DIST) + MIN_DIST;
            setRoomDistance(i, rooms[i][1]);
        }

    }


    /**
     * Function that set's the number of paintings in a room
     * @param num_room room id
     * @param paintings number of paintings
     */
    private void setNumberofPaintings(int num_room, int paintings){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }

        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETNUMBEROFPAINTINGS,num_room, paintings);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();
    }

    /**
     * Function that set's the room distance
     * @param num_room id of the room
     * @param distance distance to be applied
     */
    private void setRoomDistance(int num_room, int distance){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }

        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETROOMDISTANCE,num_room, distance);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();

    }

    /**
     *
     * Method to steal the paiting
     *
     * @param room_number room id
     * @return true if success
     */
    public synchronized boolean stealPainting(int room_number) {
        if (rooms[room_number][0] > 0) {
            rooms[room_number][0]--;
            //generalRepository.setNumberofPaintings(nr_sala, rooms[nr_sala][0]);
            return true;
        } else {
            return false;
        }
    }


    /**
     * Distance of the room
     * @param nr_sala room id
     * @return distance
     */
    public synchronized int getDistance(int nr_sala) {
        return rooms[nr_sala][1];
    }

    /**
     * Number of paitings
     * @param nr_sala room id
     * @return nr of paitings
     */
    public synchronized int getNumberofStolenPaintings(int nr_sala) {
        return rooms[nr_sala][0];
    }
}

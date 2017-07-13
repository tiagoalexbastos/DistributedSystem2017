package serverSide.museum;

import interfaces.GeneralRepositoryInterface;
import interfaces.MuseumInterface;
import interfaces.Register;
import registry.RegistryConfig;
import support.Constantes;
import support.Tuple;
import support.VectorTimestamp;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

import static support.Constantes.*;

/**
 * Monitor Museum
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class Museum implements MuseumInterface {

    /**
     * Rooms of the museum
     */
    int[][] rooms = new int[NUM_ROOMS][2];
    /**
     * General Repository
     */
    GeneralRepositoryInterface generalRepository;

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
     * @param generalRepository General Repository interface
     * @param rmiRegHostName RMI Register host name
     * @param rmiRegPortNumb RMI Register port number
     */
    public Museum(GeneralRepositoryInterface generalRepository, String rmiRegHostName, int rmiRegPortNumb) {

        this.rmiRegHostName = rmiRegHostName;
        this.rmiRegPortNumb = rmiRegPortNumb;

        this.generalRepository = generalRepository;
        local = new VectorTimestamp(Constantes.VECTOR_TIMESTAMP_SIZE, 0);


        for (int i = 0; i < rooms.length; i++) {
            Random r = new Random();
            rooms[i][0] = r.nextInt((MAX_PAINTS + 1) - MIN_PAINTS) + MIN_PAINTS;
            setNumberofRoomPaintings(i, rooms[i][0]);
            Random r2 = new Random();

            rooms[i][1] = r2.nextInt((MAX_DIST + 1) - MIN_DIST) + MIN_DIST;
            setRoomDistance(i, rooms[i][1]);
        }


    }


    /**
     * Method to steal the paiting
     *
     * @param nr_sala         room id
     * @param vectorTimestamp clock
     * @return clock and true if success
     */
    public synchronized Tuple<VectorTimestamp, Boolean> rollACanvas(int nr_sala, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        if (rooms[nr_sala][0] > 0) {
            rooms[nr_sala][0]--;
            //generalRepository.setNumberofRoomPaintings(nr_sala, rooms[nr_sala][0]);
            return new Tuple<>(local.clone(), true);
        } else {
            return new Tuple<>(local.clone(), false);
        }
    }


    /**
     * Distance of the room
     *
     * @param nr_sala room id
     * @param clone clock
     * @return clock and distance
     */
    public synchronized Tuple<VectorTimestamp, Integer> getMuseumRoomDistance(int nr_sala, VectorTimestamp clone) {
        local.update(clone);

        return new Tuple<>(local.clone(), rooms[nr_sala][1]);
    }

    /**
     * This function is used for the log to signal the AssaultPartyManager to shutdown.
     *
     * @throws RemoteException may throw during a execution of a remote method call
     */
    @Override
    public void signalShutdown() throws RemoteException {
        Register reg = null;
        Registry registry = null;

        String rmiRegHostName;
        int rmiRegPortNumb;

        rmiRegHostName = this.rmiRegHostName;
        rmiRegPortNumb = this.rmiRegPortNumb;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            System.out.println("Erro ao localizar o registo");
            ex.printStackTrace();
            System.exit(1);
        }

        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.RMI_REGISTRY_MUSEUM_NAME;


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
            System.out.println("Museum registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Museum not bound exception: " + e.getMessage());
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

        System.out.println("Museum closed.");
    }


    /**
     * Function that set's the room distance
     * @param i id of the room
     * @param i1 distance to be applied
     */
    private void setRoomDistance(int i, int i1) {
        try {
            local.update(this.generalRepository.setRoomDistance(i, i1, local.clone()));
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Function that set's the number of paintings in a room
     * @param i room id
     * @param i1 number of paintings
     */
    private void setNumberofRoomPaintings(int i, int i1) {
        try {
            local.update(this.generalRepository.setNumberofRoomPaintings(i, i1, local.clone()));
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}

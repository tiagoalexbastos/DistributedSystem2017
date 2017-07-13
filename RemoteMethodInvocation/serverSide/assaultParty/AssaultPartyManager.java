package serverSide.assaultParty;

import interfaces.AssaultPartyManagerInterface;
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

/**
 * Class that descibes the AssaultPartyManager
 *
 * @author Pedro Matos
 * @author Tiago Bastos
 */
public class AssaultPartyManager implements AssaultPartyManagerInterface {

    /**
     * Assault Groups
     */
    private AssaultParty group[] = new AssaultParty[2];
    /**
     * Museum
     */
    private MuseumInterface museum;
    /**
     * General Repository
     */
    private GeneralRepositoryInterface general;

    /**
     * AssaultPartyManager Local VectorTimestamp
     */
    private VectorTimestamp local;

    /**
     * RMI Register host name
     */
    private String rmiRegHostName;

    /**
     * RMI Register host post number
     */
    private int rmiRegPortNumb;


    /**
     * @param museum  Museum Interface
     * @param general General Repository Interface
     * @param rmiRegHostName RMI Register host name
     * @param rmiRegPortNumb RMI Register port number
     */
    public AssaultPartyManager(MuseumInterface museum, GeneralRepositoryInterface general, String rmiRegHostName, int rmiRegPortNumb) {

        this.rmiRegHostName = rmiRegHostName;
        this.rmiRegPortNumb = rmiRegPortNumb;
        this.museum = museum;
        this.general = general;

        local = new VectorTimestamp(Constantes.VECTOR_TIMESTAMP_SIZE, 0);
    }

    /**
     * Group Formation
     *
     * @param idGrupo         group id
     * @param nrSala          room id
     * @param vectorTimestamp clock
     * @return clock and boolean if creation is successful
     */
    @Override
    public synchronized Tuple<VectorTimestamp, Boolean> createAssaultParty(int idGrupo, int nrSala, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        if (group[idGrupo] == null) {
            group[idGrupo] = new AssaultParty(museum, nrSala, idGrupo, general);
            return new Tuple<>(local.clone(), true);
        }

        return new Tuple<>(local.clone(), false);
    }


    /**
     * Method that returns the thief position inside the Assault Party
     *
     * @param ladraoID        thief id
     * @param idGrupo         group id
     * @param vectorTimestamp clock
     * @return clock and int with the position of the thief in the assault party
     */
    @Override
    public Tuple<VectorTimestamp, Integer> getGroupPosition(int ladraoID, int idGrupo, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);


        return group[idGrupo].getGroupPosition(ladraoID, local.clone());
    }


    /**
     * Destroys the group
     *
     * @param idGrupo         group id
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp destroyAssaultParty(int idGrupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        group[idGrupo] = null;
        return local.clone();
    }

    /**
     * Places thief in the group
     *
     * @param ladraoID        thief id
     * @param idGrupo         group id
     * @param pos_grupo       group position
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public VectorTimestamp joinAssaultParty(int ladraoID, int idGrupo, int pos_grupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        return group[idGrupo].joinAssaultParty(ladraoID, pos_grupo, local.clone());

    }

    /**
     * Calls crawl in
     *
     * @param ladraoID        thief id
     * @param agilidade       thief agility
     * @param idGrupo         group id
     * @param posgrupo        position in the group
     * @param vectorTimestamp clock
     * @return clock and final position
     */
    @Override
    public Tuple<VectorTimestamp, Integer> crawlIn(int ladraoID, int agilidade, int idGrupo, int posgrupo, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        return group[idGrupo].crawlIn(ladraoID, agilidade, idGrupo, posgrupo, local.clone());
    }

    /**
     * Calls crawl out
     *
     * @param ladraoID        thief id
     * @param agilidade       thief agility
     * @param idGrupo         group id
     * @param posgrupo        position in the group
     * @param vectorTimestamp clock
     * @return clock final position
     */
    @Override
    public Tuple<VectorTimestamp, Integer> crawlOut(int ladraoID, int agilidade, int idGrupo, int posgrupo, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        return group[idGrupo].crawlOut(ladraoID, agilidade, idGrupo, posgrupo, local.clone());
    }


    /**
     * Get distance of room
     *
     * @param idGrupo         group id
     * @param vectorTimestamp clock
     * @param id
     * @return clock and respective room distance
     */
    @Override
    public Tuple<VectorTimestamp, Integer> getRoomDistance(int idGrupo, VectorTimestamp vectorTimestamp, int id) {
        local.update(vectorTimestamp);

        return group[idGrupo].getRoomDistance(local.clone(), id);
    }

    /**
     * Steal paiting
     *
     * @param idGrupo         group id
     * @param vectorTimestamp clock
     * @param id
     * @return clock and true if paiting was stolen
     */
    @Override
    public Tuple<VectorTimestamp, Boolean> rollACanvas(int idGrupo, VectorTimestamp vectorTimestamp, int id) {
        local.update(vectorTimestamp);

        return group[idGrupo].rollACanvas(local.clone(), id);
    }

    /**
     * Thief is waiting for it's turn
     *
     * @param id              Thief id
     * @param idGrupo         Group id
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public VectorTimestamp waitMyTurn(int id, int idGrupo, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        return group[idGrupo].waitMyTurn(id, local.clone());
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
        String nameEntryObject = RegistryConfig.RMI_REGISTRY_ASSGMAN_NAME;


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
            System.out.println("Assault Group registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Assault Group not bound exception: " + e.getMessage());
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

        System.out.println("AssaultGroupManager closed.");
    }


}

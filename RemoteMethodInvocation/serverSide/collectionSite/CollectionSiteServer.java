package serverSide.collectionSite;

import genclass.GenericIO;
import interfaces.*;
import registry.RegistryConfig;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Tiago Bastos
 * @author Pedro Matos
 */
public class CollectionSiteServer {

    public static void main(String[] args) {

        /* obtenção da localização do serviço de registo RMI */
//        String rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
//        int rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        String rmiRegHostName = args[0];
        int rmiRegPortNumb = Integer.parseInt(args[1]);

        /* localização por nome do objecto remoto no serviço de registos RMI */
        GeneralRepositoryInterface generalRepositoryInterface = null;
        ConcentrationSiteInterface concentrationSiteInterface = null;
        AssaultPartyManagerInterface assaultPartyManagerInterface = null;

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            generalRepositoryInterface = (GeneralRepositoryInterface)
                    registry.lookup(RegistryConfig.RMI_REGISTRY_GENREPO_NAME);
        } catch (RemoteException e) {
            System.out.println("Excepção na localização do General Repository: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("O General Repository não está registado: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            concentrationSiteInterface = (ConcentrationSiteInterface)
                    registry.lookup(RegistryConfig.RMI_REGISTRY_CONSITE_NAME);
        } catch (RemoteException e) {
            System.out.println("Excepção na localização do Concentration Site: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("O Concentration Site não está registado: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            assaultPartyManagerInterface = (AssaultPartyManagerInterface)
                    registry.lookup(RegistryConfig.RMI_REGISTRY_ASSGMAN_NAME);
        } catch (RemoteException e) {
            System.out.println("Excepção na localização do AssaultGroupManagement: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("O AssaultGroupManagement não está registado: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        GenericIO.writelnString("Security manager was installed!");


        /* instanciação do objecto remoto que representa o Concentration Site e geração de um stub para ele */
        CollectionSite collectionSite = new CollectionSite(concentrationSiteInterface,
                assaultPartyManagerInterface, generalRepositoryInterface, rmiRegHostName, rmiRegPortNumb);
        CollectionSiteInterface collectionSiteInterface = null;

        try {
            collectionSiteInterface = (CollectionSiteInterface)
                    UnicastRemoteObject.exportObject(collectionSite, RegistryConfig.RMI_COL_PORT);
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para o Collection Site: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O stub para o Collection Site foi gerado!");


              /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.RMI_REGISTRY_COLSITE_NAME;
        Registry registry = null;
        Register reg = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {

            System.out.println("Excepção na criação do registo RMI: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O registo RMI foi criado!");

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
            reg.bind(nameEntryObject, collectionSiteInterface);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do Collection Site: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O Collection Site já está registado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("O Collection Site foi registado!!");
    }

}

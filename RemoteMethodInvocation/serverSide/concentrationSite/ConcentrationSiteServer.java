package serverSide.concentrationSite;

import genclass.GenericIO;
import interfaces.ConcentrationSiteInterface;
import interfaces.GeneralRepositoryInterface;
import interfaces.Register;
import registry.RegistryConfig;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by tiagoalexbastos on 20-05-2017.
 */
public class ConcentrationSiteServer {

    public static void main(String[] args) {

        /* obtenção da localização do serviço de registo RMI */
//        String rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
//        int rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        String rmiRegHostName = args[0];
        int rmiRegPortNumb = Integer.parseInt(args[1]);

        /* localização por nome do objecto remoto no serviço de registos RMI */
        GeneralRepositoryInterface generalRepositoryInterface = null;

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

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        GenericIO.writelnString("Security manager was installed!");


        /* instanciação do objecto remoto que representa o Concentration Site e geração de um stub para ele */
        ConcentrationSite concentrationSite = new ConcentrationSite(generalRepositoryInterface, rmiRegHostName, rmiRegPortNumb);
        ConcentrationSiteInterface concentrationSiteInterface = null;

        try {
            concentrationSiteInterface = (ConcentrationSiteInterface)
                    UnicastRemoteObject.exportObject(concentrationSite, RegistryConfig.RMI_CONC_PORT);
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para o ConcentrationSite: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O stub para o ConcentrationSite foi gerado!");


              /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.RMI_REGISTRY_CONSITE_NAME;
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
            reg.bind(nameEntryObject, concentrationSiteInterface);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do ConcentrationSite: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O ConcentrationSite já está registado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("O ConcentrationSite foi registado!!");
    }

}

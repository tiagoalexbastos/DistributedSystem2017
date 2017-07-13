package DistributedVersion.ProblemInformation;

import DistributedVersion.ComInfo.ServerCom;
import genclass.GenericIO;

import java.net.SocketTimeoutException;

import static DistributedVersion.ComInfo.ComPorts.portGeneralRepo;

/**
 * Created by tiagoalexbastos on 24-04-2017.
 */
public class ServerGeneralRepo {

    public static void main(String[] args) {

        GeneralRepositoryInterface godInter;
        ServerCom scon, sconi;

        ClientProxy cliProxy;

        scon = new ServerCom(portGeneralRepo);
        scon.start();
        GeneralRepository god = new GeneralRepository();
        godInter = new GeneralRepositoryInterface(god);
        GenericIO.writelnString("Service General Repository has been established!");
        GenericIO.writelnString("Server listening");

        god.iniciarLog();


        /*
         * processamento de pedidos
         */
        boolean keepAlive = true;

        while (keepAlive) {
            try{
                sconi = scon.accept();
                cliProxy = new ClientProxy(sconi, godInter);
                cliProxy.start();
            }catch(SocketTimeoutException e){
                if(!godInter.isAlive()){
                    keepAlive = false;
                }
                continue;
            }

        }

        GenericIO.writelnString("The service has been terminated!");
    }

}

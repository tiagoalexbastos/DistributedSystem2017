package DistributedVersion.Monitors.ConcentrationSite;

import DistributedVersion.ComInfo.ServerCom;
import genclass.GenericIO;

import java.net.SocketTimeoutException;

import static DistributedVersion.ComInfo.ComPorts.*;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ServerConcentrationSite {

    public static void main(String[] args) {

        ConcentrationSite base;
        ConcentrationSiteInterface baseInter;
        ServerCom scon, sconi;
        ClientProxy cliProxy;


        String generalRepository = machine_log;

        scon = new ServerCom(portConcentrationSite);
        scon.start();
        base = new ConcentrationSite(generalRepository);
        baseInter = new ConcentrationSiteInterface(base);
        GenericIO.writelnString("Service CollectionSite has been established");
        GenericIO.writelnString("Server listening.");


        /*
         * processamento de pedidos
         */

        boolean keepAlive = true;

        while (keepAlive) {
            try{
                sconi = scon.accept();
                cliProxy = new ClientProxy(sconi, baseInter);
                cliProxy.start();
            }catch(SocketTimeoutException e){
                if(!baseInter.isAlive()){
                    keepAlive = false;
                }
            }

        }

        GenericIO.writelnString("Service CollectionSite has been terminated!");
    }
}


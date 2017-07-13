package DistributedVersion.Actors;

import genclass.GenericIO;

import static DistributedVersion.ComInfo.ComPorts.*;

/**
 * Created by tiagoalexbastos on 24-04-2017.
 */
public class ServerMasterThief {

    public static void main(String[] args) {

        MasterThief masterThief;
        String concentrationSite = machine_concentration;
        String collectionSite = machine_collection;
        String generalRepo = machine_log;

        masterThief = new MasterThief("Chefao", generalRepo, collectionSite, concentrationSite);
        masterThief.start();

        try {
            masterThief.join();
        } catch (InterruptedException ex) {
            System.out.println("O masterThief terminou!");
        }

    }

}

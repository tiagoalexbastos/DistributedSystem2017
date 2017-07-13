package DistributedVersion.Actors;

import DistributedVersion.Messages.Constants;
import genclass.GenericIO;

import static DistributedVersion.ComInfo.ComPorts.*;

/**
 * Created by tiagoalexbastos on 24-04-2017.
 */
public class ServerThief {

    public static void main(String[] args) {
        Thief[] thief = new Thief[Constants.NUM_THIEVES];
        String concentrationSite = machine_concentration;
        String collectionSite = machine_collection;
        String grupoAssalto = machine_party;
        String generalRepo = machine_log;



        for (int i = 0; i < Constants.NUM_THIEVES; i++) {
            thief[i] = new Thief(i, "Thief " + i, generalRepo, grupoAssalto, collectionSite, concentrationSite);
            thief[i].start();
        }

        for (int i = 0; i < Constants.NUM_THIEVES; i++) {
            try {
                thief[i].join();
            } catch (InterruptedException ex) {
                System.out.println("Thief " + i + " finished it's job!");
            }
        }
    }

}

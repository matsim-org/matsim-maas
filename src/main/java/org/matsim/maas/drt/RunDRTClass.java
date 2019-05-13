/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2017 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.maas.drt;

import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.av.robotaxi.fares.drt.DrtFareModule;
import org.matsim.contrib.av.robotaxi.fares.drt.DrtFaresConfigGroup;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.run.DrtControlerCreator;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.vis.otfvis.OTFVisConfigGroup;

import javax.inject.Inject;

/**
 * @author jbischoff
 * An example to run the demand responsive transport contribution in MATSim, with DRT service
 * serving customers from door to door between origin and destinations.
 * There are two different examples, one for Mielec and one for the Cottbus scenario.
 * You'll find the corresponding scenarios in the "scenario" folder of the project.
 */
public class RunDRTClass {

    public static void run(Config config, boolean otfvis) {
        //Creates a MATSim Controler and preloads all DRT related packages
        Controler controler = DrtControlerCreator.createControlerWithSingleModeDrt(config, otfvis);

        //this is optional, adds fares to DRT
        controler.addOverridingModule(new DrtFareModule());
        SimpleVKTCounter vktCounter = new SimpleVKTCounter();
        controler.addOverridingModule(new AbstractModule() {
            @Override
            public void install() {
                addEventHandlerBinding().toInstance(vktCounter);
            }
        });
        //starts the simulation
        controler.run();
        System.out.println("VKT traveled in last iteration: " + Math.round(vktCounter.getVkt_counted()) + " km");
    }

    public static void main(String[] args) {

        Config config = ConfigUtils.loadConfig(args[0], new DrtConfigGroup(), new DvrpConfigGroup(),
                new OTFVisConfigGroup(), new DrtFaresConfigGroup());
        run(config, false);
    }
}

class SimpleVKTCounter implements LinkEnterEventHandler {
    @Inject
    Network network;

    private double vkt_counted = 0;

    @Override
    public void handleEvent(LinkEnterEvent event) {
        vkt_counted += network.getLinks().get(event.getLinkId()).getLength() / 1000.;
    }

    @Override
    public void reset(int iteration) {
        vkt_counted = 0;
    }

    public double getVkt_counted() {
        return vkt_counted;
    }
}

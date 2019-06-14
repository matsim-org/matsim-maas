/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2016 by the members listed in the COPYING,        *
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

/**
 *
 */
package org.matsim.maas.utils;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.DvrpVehicleSpecification;
import org.matsim.contrib.dvrp.fleet.FleetWriter;
import org.matsim.contrib.dvrp.fleet.ImmutableDvrpVehicleSpecification;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author jbischoff
 * This is an example script to create a vehicle file for taxis, SAV or DRTs.
 * The vehicles are distributed randomly in the network.
 */
public class CreateFleetVehicles {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int numberofVehicles = 5000;
		double operationStartTime = 0.; //the time from which a vehicle is operative
		double operationEndTime = 24 * 3600.;    //the time until a vehicle is operative
		int seats = 4; // capacity, only used in DRT
		String networkfile = "scenarios/cottbus/network.xml.gz";
		String fleetVehicleFile = "fleetVehicles_" + numberofVehicles + ".xml";
		String networkMode = TransportMode.car; // Network mode, on which vehicles should be placed
		Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
		List<DvrpVehicleSpecification> vehicles = new ArrayList<>();
		Random random = MatsimRandom.getLocalInstance();
		new MatsimNetworkReader(scenario.getNetwork()).readFile(networkfile);
		List<Link> allLinks = new ArrayList<>();
		allLinks.addAll(scenario.getNetwork().getLinks().values()
				.stream()
				.filter(l -> l.getAllowedModes().contains(networkMode))
				.collect(Collectors.toSet()));

		for (int i = 0; i < numberofVehicles; i++) {
			Link startLink = allLinks.get(random.nextInt(allLinks.size()));

			vehicles.add(ImmutableDvrpVehicleSpecification.newBuilder().id(Id.create("fleetVehicle" + i, DvrpVehicle.class))
					.startLinkId(startLink.getId())
					.capacity(seats)
					.serviceBeginTime(operationStartTime)
					.serviceEndTime(operationEndTime)
					.build());

		}
		new FleetWriter(vehicles.stream()).write(fleetVehicleFile);
	}

}

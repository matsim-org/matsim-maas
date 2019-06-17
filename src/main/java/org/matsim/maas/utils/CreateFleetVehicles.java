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

package org.matsim.maas.utils;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.DvrpVehicleSpecification;
import org.matsim.contrib.dvrp.fleet.FleetWriter;
import org.matsim.contrib.dvrp.fleet.ImmutableDvrpVehicleSpecification;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

/**
 * @author jbischoff
 * This is an example script to create a vehicle file for taxis, SAV or DRTs.
 * The vehicles are distributed randomly in the network.
 */
public class CreateFleetVehicles {

	/**
	 * Adjust these variables and paths to your need.
	 */

	private static final int numberOfVehicles = 1500;
	private static final int seatsPerVehicle = 6; //this is important for DRT, value is not used by taxi
	private static final double operationStartTime = 0;
	private static final double operationEndTime = 24 * 60 * 60; //24h
	private static final Random random = MatsimRandom.getRandom();

	private static final Path networkFile = Paths.get("scenarios/cottbus/network.xml.gz");
	private static final Path outputFile = Paths.get("fleetVehicles.xml");

	public static void main(String[] args) {

		new CreateFleetVehicles().run();
	}

	private void run() {

		Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
		new MatsimNetworkReader(scenario.getNetwork()).readFile(networkFile.toString());
		final int[] i = {0};
		Stream<DvrpVehicleSpecification> vehicleSpecificationStream = scenario.getNetwork().getLinks().entrySet().stream()
				.filter(entry -> entry.getValue().getAllowedModes().contains(TransportMode.car)) // drt can only start on links with Transport mode 'car'
				.sorted((e1, e2) -> (random.nextInt(2) - 1)) // shuffle links
				.limit(numberOfVehicles) // select the first *numberOfVehicles* links
				.map(entry -> ImmutableDvrpVehicleSpecification.newBuilder()
						.id(Id.create("drt_" + i[0]++, DvrpVehicle.class))
						.startLinkId(entry.getKey())
						.capacity(seatsPerVehicle)
						.serviceBeginTime(operationStartTime)
						.serviceEndTime(operationEndTime)
						.build());

		new FleetWriter(vehicleSpecificationStream).write(outputFile.toString());
	}

}

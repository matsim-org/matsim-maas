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

import org.locationtech.jts.geom.Geometry;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.application.MATSimAppCommand;
import org.matsim.application.options.ShpOptions;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.DvrpVehicleSpecification;
import org.matsim.contrib.dvrp.fleet.FleetWriter;
import org.matsim.contrib.dvrp.fleet.ImmutableDvrpVehicleSpecification;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.geotools.MGC;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jbischoff, modified by luchengqi7
 * This is an example script to create a vehicle file for taxis, SAV or DRTs.
 * The vehicles are distributed randomly in the network.
 */

@CommandLine.Command(
		name = "create-fleet",
		description = "create drt fleet"
)
public class CreateFleetVehicles implements MATSimAppCommand {
	@CommandLine.Option(names = "--network", description = "path to network file", required = true)
	private Path networkFile;

	@CommandLine.Option(names = "--fleet-size", description = "number of vehicles to generate", required = true)
	private int fleetSize;

	@CommandLine.Option(names = "--capacity", description = "capacity of the vehicle", required = true)
	private int capacity;

	@CommandLine.Option(names = "--output", description = "path to network file", required = true)
	private Path outputFile;

	@CommandLine.Option(names = "--operator", description = "name of the operator", defaultValue = "drt")
	private String operator;

	@CommandLine.Option(names = "--start-time", description = "service starting time", defaultValue = "0")
	private double startTime;

	@CommandLine.Option(names = "--end-time", description = "service ending time", defaultValue = "86400")
	private double endTime;

	@CommandLine.Mixin
	private ShpOptions shp = new ShpOptions(); // Optional input for service area (shape file)

	private static final Random random = MatsimRandom.getRandom();


	public static void main(String[] args) {
		new CreateFleetVehicles().execute(args);
	}

	@Override
	public Integer call() throws Exception {
		Network network = NetworkUtils.readNetwork(networkFile.toString());
		List<Link> links = network.getLinks().values().stream().
				filter(l -> l.getAllowedModes().contains(TransportMode.car)).
				collect(Collectors.toList());
		if (shp.isDefined()) {
			Geometry serviceArea = shp.getGeometry();
			links = links.stream().
					filter(l -> MGC.coord2Point(l.getToNode().getCoord()).within(serviceArea)).
					collect(Collectors.toList());
		}

		List<DvrpVehicleSpecification> vehicleSpecifications = new ArrayList<>();
		for (int i = 0; i < fleetSize; i++) {
			Id<Link> startLinkId = links.get(random.nextInt(links.size())).getId();
			DvrpVehicleSpecification vehicleSpecification = ImmutableDvrpVehicleSpecification.newBuilder().
					id(Id.create(operator + "_" + i, DvrpVehicle.class)).
					startLinkId(startLinkId).
					capacity(capacity).
					serviceBeginTime(startTime).
					serviceEndTime(endTime).build();
			vehicleSpecifications.add(vehicleSpecification);
		}

		new FleetWriter(vehicleSpecifications.stream()).write(outputFile.toString());

		return 0;
	}
}

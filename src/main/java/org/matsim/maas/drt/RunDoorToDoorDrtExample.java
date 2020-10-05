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

import org.matsim.contrib.drt.run.DrtControlerCreator;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.vis.otfvis.OTFVisConfigGroup;

/**
 * @author jbischoff
 * An example to run the demand responsive transport contribution in MATSim, with DRT service
 * serving customers from door to door between origin and destinations.
 * There are two different examples, one for Mielec and one for the Cottbus scenario.
 * You'll find the corresponding scenarios in the "scenario" folder of the project.
 */
public class RunDoorToDoorDrtExample {

	private static final String COTTBUS_DOOR2DOOR_CONFIG = "scenarios/cottbus/drtconfig_door2door.xml";

	@SuppressWarnings("unused")
	private static final String MIELEC_CONFIG = "scenarios/mielec_2014_02/mielec_drt_config.xml";

	public static void run(Config config, boolean otfvis) {
		//Creates a MATSim Controler and preloads all DRT related packages
		Controler controler = DrtControlerCreator.createControler(config, otfvis);

		//starts the simulation
		controler.run();
	}

	public static void main(String[] args) {
		Config config = ConfigUtils.loadConfig(COTTBUS_DOOR2DOOR_CONFIG, new MultiModeDrtConfigGroup(),
				new DvrpConfigGroup(), new OTFVisConfigGroup());
		run(config, false);
	}
}

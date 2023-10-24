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

package org.matsim.maas.taxi;

import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.taxi.run.MultiModeTaxiConfigGroup;
import org.matsim.contrib.taxi.run.TaxiControlerCreator;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.vis.otfvis.OTFVisConfigGroup;

/**
 * An example class to Run a taxi scenario based on a config file.
 * Note that several different optimizers may be set directly within the config file.
 * Two examples are provided here: A rulebased and an assignment based dispatch algorithm.
 */

public class RunTaxiExample {
	public static final String CONFIG_FILE_RULEBASED = "scenarios/mielec_2014_02/mielec_taxi_config_rulebased.xml";
	public static final String CONFIG_FILE_ASSIGNMENT = "scenarios/mielec_2014_02/mielec_taxi_config_assignment.xml";

	public static void run(String configFile, boolean otfvis, int lastIteration) {
		// load config
		Config config = ConfigUtils.loadConfig(configFile, new MultiModeTaxiConfigGroup(), new DvrpConfigGroup(),
				new OTFVisConfigGroup());
		config.controller().setLastIteration(lastIteration);

		TaxiControlerCreator.createControler(config, otfvis).run();
	}

	public static void main(String[] args) {
		//RunTaxiExample.run(CONFIG_FILE_RULEBASED,false, 0); // switch to 'true' to turn on visualisation
		RunTaxiExample.run(CONFIG_FILE_ASSIGNMENT, false, 0); // switch to 'true' to turn on visualisation
	}
}

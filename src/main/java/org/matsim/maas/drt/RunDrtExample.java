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

import java.net.URL;

import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.run.DrtControlerCreator;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.maas.MaasExamplesUtils;
import org.matsim.vis.otfvis.OTFVisConfigGroup;

/**
 * @author jbischoff
 * An example to run the demand responsive transport contribution in MATSim. Provides three different examples.
 *
 */
public class RunDrtExample {

	private static final URL COTTBUS_DOOR2DOOR_CONFIG = IOUtils.newUrl(MaasExamplesUtils.getScenarioURL("cottbus"),
			"drtconfig_door2door.xml");

	@SuppressWarnings("unused")
	private static final URL COTTBUS_STOPBASED_CONFIG = IOUtils.newUrl(MaasExamplesUtils.getScenarioURL("cottbus"),
			"drtconfig_stopbased.xml");

	@SuppressWarnings("unused")
	private static final URL MIELEC_CONFIG = IOUtils.newUrl(MaasExamplesUtils.getScenarioURL("mielec_2014_02"),
			"mielec_drt_config.xml");

	public static void run(Config config, boolean otfvis) {
		DrtControlerCreator.createControler(config, otfvis).run();
	}

	public static void main(String[] args) {

		Config config = ConfigUtils.loadConfig(COTTBUS_DOOR2DOOR_CONFIG, new DrtConfigGroup(), new DvrpConfigGroup(),
				new OTFVisConfigGroup());
		config.qsim().setTrafficDynamics(QSimConfigGroup.TrafficDynamics.kinematicWaves);
		config.qsim().setSnapshotStyle(QSimConfigGroup.SnapshotStyle.kinematicWaves);
		run(config, false);
	}
}

/* *********************************************************************** *
 * project: org.matsim.*
 * Controler.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
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

import org.apache.log4j.Logger;
import org.matsim.contrib.av.robotaxi.fares.drt.DrtFareModule;
import org.matsim.contrib.av.robotaxi.fares.drt.DrtFaresConfigGroup;
import org.matsim.contrib.drt.run.DrtControlerCreator;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.maas.utils.SimpleVKTCounter;
import org.matsim.vis.otfvis.OTFVisConfigGroup;

public class RunDRTClass {

	public static void run(Config config, boolean otfvis) {
		Controler controler = DrtControlerCreator.createControler(config, otfvis);
		controler.addOverridingModule(new DrtFareModule());
		final SimpleVKTCounter vktCounter = new SimpleVKTCounter();
		controler.addOverridingModule(new AbstractModule() {
			public void install() {
				this.addEventHandlerBinding().toInstance(vktCounter);
			}
		});
		controler.run();
		Logger.getLogger(RunDRTClass.class).info("VKT traveled in last iteration: " + Math.round(vktCounter.getVkt_counted()) + " km");
	}

	public static void main(String[] args) {
		Config config = ConfigUtils.loadConfig(args[0], new MultiModeDrtConfigGroup(), new DvrpConfigGroup(), new OTFVisConfigGroup(),
				new DrtFaresConfigGroup());

		run(config, false);
	}

}

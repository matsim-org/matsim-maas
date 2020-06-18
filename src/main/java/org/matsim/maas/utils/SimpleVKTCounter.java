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

package org.matsim.maas.utils;

import javax.inject.Inject;

import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.network.Network;

public class SimpleVKTCounter implements LinkEnterEventHandler {
	@Inject
	Network network;
	private double vkt_counted = 0.0D;

	public void handleEvent(LinkEnterEvent event) {
		this.vkt_counted += this.network.getLinks().get(event.getLinkId()).getLength() / 1000.0D;
	}

	public void reset(int iteration) {
		this.vkt_counted = 0.0D;
	}

	public double getVkt_counted() {
		return this.vkt_counted;
	}
}

package org.matsim.maas;

import java.net.URL;

public class MaasExamplesUtils {
	public static URL getScenarioURL(String name) {
		return MaasExamplesUtils.class.getResource("/scenarios/" + name + "/");
		// note that this works because it is entered as such in the pom.xml.  kai, sep'17
	}
}

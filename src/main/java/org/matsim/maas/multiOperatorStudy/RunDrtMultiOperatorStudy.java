package org.matsim.maas.multiOperatorStudy;

import org.matsim.application.MATSimAppCommand;
import org.matsim.contrib.drt.run.DrtControlerCreator;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import picocli.CommandLine;

import java.nio.file.Path;

@CommandLine.Command(
		name = "run",
		description = "run drt multi-operator study"
)
public class RunDrtMultiOperatorStudy implements MATSimAppCommand {
	@CommandLine.Option(names = "--config", description = "path to config file", required = true)
	private Path configPath;

	public static void main(String[] args) {
		new RunDrtMultiOperatorStudy().execute(args);
	}

	@Override
	public Integer call() throws Exception {
		Config config = ConfigUtils.loadConfig(configPath.toString());
		Controler controler = DrtControlerCreator.createControler(config, false);
		controler.run();
		return 0;
	}
}

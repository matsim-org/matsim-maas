package org.matsim.maas.prebookingStudy;

import org.matsim.api.core.v01.Scenario;
import org.matsim.application.MATSimAppCommand;
import org.matsim.contrib.drt.routing.DrtRoute;
import org.matsim.contrib.drt.routing.DrtRouteFactory;
import org.matsim.contrib.drt.run.DrtConfigs;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contrib.drt.run.MultiModeDrtModule;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.dvrp.run.MultiModal;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioUtils;
import picocli.CommandLine;

import java.nio.file.Path;

import static org.matsim.contrib.drt.run.DrtControlerCreator.createScenarioWithDrtRouteFactory;

@CommandLine.Command(
		name = "run",
		description = "run drt pre-booking study"
)
public class RunPrebookingStudy implements MATSimAppCommand {
	@CommandLine.Option(names = "--config", description = "path to config file", required = true)
	private Path configPath;


	@Override
	public Integer call() throws Exception {
		Config config = ConfigUtils.loadConfig(configPath.toString());
		MultiModeDrtConfigGroup multiModeDrtConfig = MultiModeDrtConfigGroup.get(config);
		DrtConfigs.adjustMultiModeDrtConfig(multiModeDrtConfig, config.planCalcScore(), config.plansCalcRoute());

		Scenario scenario = ScenarioUtils.loadScenario(config);
		scenario.getPopulation().getFactory().getRouteFactories().setRouteFactory(DrtRoute.class, new DrtRouteFactory());

		Controler controler = new Controler(scenario);
		controler.addOverridingModule(new DvrpModule());
		controler.addOverridingModule(new MultiModeDrtModule());
		controler.configureQSimComponents(DvrpQSimComponents.activateAllModes(multiModeDrtConfig));

		controler.run();

		
		return 0;
	}
}

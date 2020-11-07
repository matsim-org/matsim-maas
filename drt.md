# Demand Responsive Transport (DRT)

The **Demand Responsive Transport**  module allows the simulation of pooled (or shared) rides in MATSim. 
In the simulation scenario, there may be one or several *DRT operators*, each of them having its own characteristics, such as the vehicle fleet, detour or scoring paramters.

## Algorithm

The vehicle dispatch algorithm is based on an insertion heuristic, where for each new incoming request all feasible insertion points are assessed and the best one (for a given objective function) is chosen. The feasibility of insertion is checked against the following constraints: vehicle capacity, vehicle availability (time window), maximum wait time and travel time (for both the new request and all other uncompleted requests). By default, if no feasible insertion exists, the request gets rejected, although this behavour can be changed (see "Configuration").

A more detailed description of the algorithms can be found in 
*J. Bischoff, M. Maciejewski, K. Nagel* **City-wide Shared Taxis: A Simulation Study in Berlin**, IEEE ITSC 2017, DOI: [10.1109/ITSC.2017.8317926](https://doi.org/10.1109/ITSC.2017.8317926), [Full text available here](http://dx.doi.org/10.14279/depositonce-7734).

## Configuration
Based on a standard MATSim config file, DRT requires at least two additional config groups: One general DVRP and one for DRT. For an example config file, see [scenarios/cottbus/drtconfig_door2door.xml].

The DRT configuration should look roughly like that:

```xml
	<module name="multiModeDrt">
		<parameterset type="drt">
            <parameterset type="ExtensiveInsertionSearch"/>
			<param name="mode" value="drt_1"/>
			<param name="vehiclesFile" value="drtvehicles.xml"/>
			<param name="operationalScheme" value="door2door"/>
			<!-- optional files: -->
			<param name="transitStopFile" value="schedule.xml"/>
			<param name="drtServiceAreaShapeFile" value="shape.xml"/>


			<param name="changeStartLinkToLastLinkInSchedule" value="false"/>
			
			<param name="maxTravelTimeAlpha" value="1.5"/>
			<param name="maxTravelTimeBeta" value="1200.0"/>
			<param name="maxWaitTime" value="1200.0"/>
			<param name="rejectRequestIfMaxWaitOrTravelTimeViolated" value="true"/>
			
			<param name="stopDuration" value="60.0"/>
			<param name="writeDetailedCustomerStats" value="true"/>

			<parameterset type="zonalSystem">
				<param name="zonesGeneration" value="GridFromNetwork"/>
				<param name="cellSize" value="500"/>
			</parameterset>

			<parameterset type="rebalancing">
				<parameterset type="minCostFlowRebalancingStrategy">
					<param name="targetAlpha" value="0.5"/>
					<param name="targetBeta" value="0.5"/>
				</parameterset>
			</parameterset>
		</parameterset>
	</module>
```

The function of the parameters is as follows:

**mode** - The mode associated with the config parameterset. Add one parameterset into the _multiModeDrt_ module per operator.

**vehiclesFile**  - A file containing the fleet specifications. To get an idea how to create your own fleet file, see the _CreateFleetVehicles.java_ script.

**operationalScheme** - DRT can either be used in three different operation modes:
   - _door2door_ for a service serving passengers from door-to-door
   - _serviceAreaBased_ for a door2door service that runs only within a certain area. The area must be defined in a shape file, which must be provided using the **drtServiceAreaShapeFile** parameter.
   - _stopbased_ Provides a stop-based DRT services, where passengers need to walk to the first access stop and from the last egress stop. The stops need to be defined in the MATSim transit schedule format and the file needs to specified using the 
**transitStopFile** parameter.

**changeStartLinkToLastLinkInSchedule** - If enabled, at the beginning of a new iteration, vehicles are kept where they were last parked in the previous iteration. Otherwise, they are positioned at their start link according to the vehicles file again. 


The following parameters directly influence the pooling algorithms, which includes a simple linear function that determines the maximum allowable traveltime. The full equation is:
maxTravelTime = maxTravelTimeAlpha \* estimated_drt_travel_time + maxTravelTimeBeta.

**maxTravelTimeAlpha** - A factor, usually something >1.

**maxTravelTimeBeta** - an additional offset in seconds. Keep in mind that maxTravelTime **includes** waiting times. Therefore, **maxTravelTimeBeta** >= **maxWaitTime** needs to be set.

**maxWaitTime** - Maximum desirable waiting time for a vehicle to arrive. 

**rejectRequestIfMaxWaitOrTravelTimeViolated** - If true, the max travel and wait times of a submitted request, are considered hard constraints (the request gets rejected if one of the constraints is violated). If false, the max travel and wait times are considered soft constraints (insertion of a request that violates one of the constraints is allowed, but its cost is increased by additional penalty to make it relatively less attractive). Penalisation of insertions can be customised by injecting a customized InsertionCostCalculator.PenaltyCalculator.

**stopDuration** - Determines the length of a stop to pick up or drop off passengers.

**writeDetailedCustomerStats** - DRT writes several statistics per iteration. Disable if you do not need this to speed up the simulation.

**Vehicle rebalancing**
Rebalancing idle vehicles is crucial in order to increase the overall efficiency of the system. In DRT, a Minimum Cost Flow Rebalancing strategy is already included that takes into account the demand structure of the previous iteration in order to rebalance vehicles. More info in [this paper](https://www.sciencedirect.com/science/article/pii/S1877050920306220).

**interval** - \[s\] the interval in between re-balancing vehicles in seconds.

**cellSize** - \[m\] the demand per iteration is aggregated over cells. Use a bigger cell size for scenarios with large network extents.

package grails.story

import org.apache.commons.logging.LogFactory
import grails.converters.JSON
import JaCoP.core.*
import JaCoP.constraints.*
import JaCoP.search.*

class TripSolverService {

	private static final logger = LogFactory.getLog(this)
	
	// Services are transactional by default in Grails. It is not needed here.
	static transactional = false
	// A new service will be created per request.
	static scope = "singleton"

    def solveTrips(jsonTrips) {
		def store = new Store()
		def vars = []
		def profit
		def trips = []
		def tripWeights = []
		def optimisation = [gain: 0, path: []]
	
//		logger.debug "jsonTrips=$jsonTrips"
		def tripCollisions = []
		jsonTrips.sort{ it.DEPART }
				 .eachWithIndex { object, index ->
			def trip = new BooleanVar(store, "t$index")
			trips.add(trip)
			tripWeights.add(object.PRIX)

			for(int i = 0; i < object.DUREE; i++){
				def hour = object.DEPART + i
				if(tripCollisions[hour] == null) {
					tripCollisions[hour] = []
				}
				tripCollisions[hour].add(trip)
			}
		}
//		logger.debug "trips=$trips"
//		logger.debug "tripWeights=$tripWeights"
//		logger.debug "tripCollisions=$tripCollisions"
		
		def uniqueTripCollisions = new HashSet()
		tripCollisions.each {
			if(it  != null && it.size >= 2) {
				uniqueTripCollisions.add(it)
			}
		}
//		logger.debug "uniqueTripCollisions=$uniqueTripCollisions"

		uniqueTripCollisions.eachWithIndex { uniqueTripCollision, index ->
			store.impose(
				new Among(
					uniqueTripCollision as IntVar[], 
					new IntervalDomain(1,1), 
					new IntVar(store, "c$index", 0,1)))
		}
		profit = new IntVar(store, "profit", 0, IntDomain.MaxInt);
		store.impose(new SumWeight(trips, tripWeights, profit))
		vars.add(profit)
		vars.addAll(trips)
//		logger.debug "vars=$vars"
		Search<IntVar> label = new DepthFirstSearch<IntVar>();
//		label.setPrintInfo(logger.isDebugEnabled());
		label.setPrintInfo(false);
		SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, vars as IntVar[], new IndomainMax<IntVar>());
		if(label.labeling(store, select)) {
			def selectedTrips = []
			trips.eachWithIndex { object, index ->
				if(object.value() == 1) {
					selectedTrips.add(jsonTrips[index].VOL)
				}
			}
			optimisation = [gain: profit.value(), path: selectedTrips]
		}
		
//		logger.debug "optimisation=$optimisation"
		return optimisation
	}
}
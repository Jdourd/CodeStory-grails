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
	static scope = "request"

    def solveTrips(jsonTrips) {
		logger.debug "jsonTrips=$jsonTrips"
		def store = new Store()
		def trips = []
		def tripWeights = []
		def tripCollisions = []
		def uniqueTripCollisions = new HashSet()
		def optimisation = [gain: 0, path: []]
		
		jsonTrips.eachWithIndex { object, index ->
			def trip = new BooleanVar(store, "t$index");
			trips.add(trip)
			tripWeights.add(object.PRIX)
			
			for(int i = 0; i < object.DUREE; i++){
				def hour = object.DEPART + i
				if(tripCollisions[hour] == null) {
					tripCollisions[hour] = []
				}
				tripCollisions[hour].add(index)
			}
		}
		logger.debug "trips=$trips"
		logger.debug "tripWeights=$tripWeights"
		logger.debug "tripCollisions=$tripCollisions"
		
		tripCollisions.each {
			if(it.size >= 2) {
				uniqueTripCollisions.add(it)
			}
		}
		logger.debug "uniqueTripCollisions=$uniqueTripCollisions"
		
		uniqueTripCollisions.each {
			def combinaisonSize = it.size
			def allZeroCombinaison = [0] * combinaisonSize
			def possibleCombinaisons = [allZeroCombinaison]
			
			for(int i = 0; i < combinaisonSize; i++) {
				def anotherCombinaison = [0] * combinaisonSize
				anotherCombinaison[i] = 1
				possibleCombinaisons.add(anotherCombinaison)
			}
			
			def varsInCombinaison = []
			for(int i = 0; i < combinaisonSize; i++) {
				varsInCombinaison.add(trips[it[i]])
			}
			
			store.impose(new ExtensionalSupportVA(varsInCombinaison, possibleCombinaisons as int[][]));
			logger.debug "varsInCombinaison=$varsInCombinaison"
			logger.debug "possibleCombinaisons=$possibleCombinaisons"
		}
		
		IntVar profit = new IntVar(store, "profit", 0, IntDomain.MaxInt);
		store.impose(new SumWeight(trips, tripWeights, profit));
		
		def vars = [profit]
		vars.addAll(trips)
		logger.debug "vars=$vars"
		
		Search<IntVar> label = new DepthFirstSearch<IntVar>();
		label.setPrintInfo(logger.isDebugEnabled());
		SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, vars as IntVar[], new IndomainMax<IntVar>());
		boolean result = label.labeling(store, select);
		if ( result ) {
			def selectedTrips = []
			trips.eachWithIndex { object, index ->
				if(object.value() == 1) {
					selectedTrips.add(jsonTrips[index].VOL)
				}
			}
			optimisation = [gain: profit.value(), path: selectedTrips]
		}
		logger.debug "optimisation=$optimisation"
		return optimisation
    }
}

package grails.story

import org.apache.commons.logging.LogFactory
import grails.converters.JSON
import JaCoP.core.*
import JaCoP.constraints.*
import JaCoP.search.*

class JajascriptController {

	private static final logger = LogFactory.getLog(this)

    def optimize = { 
		logger.debug request.JSON
		def store = new Store()
		def trips = []
		def tripWeights = []
		def tripCollisions = []
		def uniqueTripCollisions = new HashSet()
		
		request.JSON.eachWithIndex { object, index ->
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
		SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, vars as IntVar[], new IndomainMax<IntVar>());
		boolean result = label.labeling(store, select);
		if ( result ) {
			def selectedTrips = []
			trips.eachWithIndex { object, index ->
				if(object.value() == 1) {
					selectedTrips.add(request.JSON[index].VOL)
				}
			}
//			println("Solution: 10*" + a + " + 14*" + b + " + 8*"+ c + " + 8*" + d + " = " + profit);
			render([gain: profit.value(), path: selectedTrips] as JSON)
		} else {
			println("*** No");
			render([gain: 0, path: []] as JSON)
		}
	}
}

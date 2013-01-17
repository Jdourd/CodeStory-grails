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
		def starts = []
		def lengths = []
		def choosedLengths = []
		def ressources = []
		def tripWeights = []
		def optimisation = [gain: 0, path: []]
	
//		logger.debug "jsonTrips=$jsonTrips"
		def tripCollisions = []
		jsonTrips.sort{ it.DEPART }
			.eachWithIndex { object, index ->
			tripWeights.add(object.PRIX)
			
			starts.add(new IntVar(store, "s$index", object.DEPART, object.DEPART))
			def choosedLength = new IntVar(store, "cl$index", 0, 1)
			choosedLengths.add(choosedLength)
			def length = new IntVar(store, "l$index", object.DUREE, object.DUREE)
			length.addDom(0, 0)
			lengths.add(length)
			store.impose(new XmulCeqZ(choosedLength, object.DUREE, length))
			
			ressources.add(new IntVar(store, "r$index", 1, 1))
		}
		IntVar limit = new IntVar(store, "limit", 0, 1);
		Constraint ctr = new Cumulative(starts, lengths, ressources, limit)
		store.impose(ctr)
		
		profit = new IntVar(store, "profit", 0, IntDomain.MaxInt)
		store.impose(new SumWeight(choosedLengths, tripWeights, profit))
		
		vars.add(profit)
		vars.addAll(choosedLengths)

		Search<IntVar> label = new DepthFirstSearch<IntVar>();
//		label.setPrintInfo(logger.isDebugEnabled());
		label.setPrintInfo(false);
		SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, vars as IntVar[], new IndomainMax<IntVar>());
		if(label.labeling(store, select)) {
			def selectedTrips = []
			choosedLengths.eachWithIndex { object, index ->
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
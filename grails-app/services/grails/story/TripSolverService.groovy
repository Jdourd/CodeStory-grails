package grails.story

import org.apache.commons.logging.LogFactory
import grails.converters.JSON
import groovy.time.*

class TripSolverService {

	private static final logger = LogFactory.getLog(this)
	
	// Services are transactional by default in Grails. It is not needed here.
	static transactional = false
	static scope = "singleton"

	/** Algotrithm designed with help of Jeremie Lussiez */
    def solveTrips(jsonTrips) {
		def optimisation = [gain: 0, path: []]
		def cumulMap = new TreeMap()
		def lastCumul = 0
		
//		logger.debug "jsonTrips=$jsonTrips"
		def departMap = 
			jsonTrips
				.sort{ - it.DEPART }
				.groupBy { 
					lastCumul = Math.max(lastCumul, it.DEPART)
					it.DEPART
				}
			
		departMap.each { depart, trips ->
			def maxCumul = trips.max { trip ->
				trip['gain'] = trip.PRIX
				def arrivee = trip.DEPART + trip.DUREE
				if(arrivee <= lastCumul) {
					def cumulProcheArrivee = cumulMap.find { it.key >= arrivee }
					if(cumulProcheArrivee != null && cumulProcheArrivee.value != null) {
						trip['fils prodige'] = cumulProcheArrivee.value
						trip['gain'] += trip['fils prodige']['gain']
					}
				}
				trip['gain']
			}
			if(depart == lastCumul) {
				cumulMap.put(depart, maxCumul)
			} else {
				def departPlusUn = depart + 1
				def cumulProcheDepart = cumulMap.find { it.key >= departPlusUn }
				if(cumulProcheDepart != null 
					&& cumulProcheDepart.value['gain'] > maxCumul['gain']) {
					cumulMap.put(depart, cumulProcheDepart.value)
				} else {
					cumulMap.put(depart, maxCumul)
				}
			}
		}

		def tripStart = cumulMap.firstEntry().value
		optimisation['gain'] = tripStart['gain']
		optimisation['path'] += tripStart.VOL
		while(tripStart['fils prodige'] != null) {
		        optimisation['path'] += tripStart['fils prodige'].VOL
		        tripStart = tripStart['fils prodige']
		}
//		logger.debug "optimisation=$optimisation"
		return optimisation
	}
}
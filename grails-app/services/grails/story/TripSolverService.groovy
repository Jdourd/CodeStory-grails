package grails.story

import org.apache.commons.logging.LogFactory
import grails.converters.JSON
import groovy.time.*

class TripSolverService {

	private static final logger = LogFactory.getLog(this)
	private static final timeLogger = LogFactory.getLog('grails.story.TripSolverServiceTime')
	
	// Services are transactional by default in Grails. It is not needed here.
	static transactional = false
	static scope = "singleton"

	/** Algotrithm designed with help of Jeremie Lussiez */
    def solveTrips(jsonTrips) {
		def optimisation = [gain: 0, path: []]
//		logger.debug "jsonTrips=$jsonTrips"
		def cumulMap = new TreeMap()
		def lastCumul = 0
		Date addAndSortStart, algoStart, renderStart, stop
		
		if(timeLogger.isDebugEnabled()) addAndSortStart = new Date()
		def departMap = 
			jsonTrips
				.sort{ - it.DEPART }
				.groupBy { 
					lastCumul = Math.max(lastCumul, it.DEPART)
					it.DEPART
				}
		
		if(timeLogger.isDebugEnabled()) algoStart = new Date()
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

		if(timeLogger.isDebugEnabled()) renderStart = new Date()
		def tripStart = cumulMap.firstEntry().value
		optimisation['gain'] = tripStart['gain']
		optimisation['path'] += tripStart.VOL
		while(tripStart['fils prodige'] != null) {
		        optimisation['path'] += tripStart['fils prodige'].VOL
		        tripStart = tripStart['fils prodige']
		}
		
		if(timeLogger.isDebugEnabled()) {
			stop = new Date()
			TimeDuration addAndSortDuration = TimeCategory.minus(algoStart, addAndSortStart)
			TimeDuration algoDuration = TimeCategory.minus(renderStart, algoStart)
			TimeDuration renderDuration = TimeCategory.minus(stop, renderStart)
			TimeDuration totalDuration = TimeCategory.minus(stop, addAndSortStart)
			timeLogger.debug "total=$totalDuration\tand and sort=$addAndSortDuration\talgo=$algoDuration\trender=$renderDuration"
		}
//		logger.debug "optimisation=$optimisation"
		return optimisation
	}
}
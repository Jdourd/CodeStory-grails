package grails.story

import org.apache.commons.logging.LogFactory
import grails.converters.JSON

class TripSolverService {

	private static final logger = LogFactory.getLog(this)
	
	// Services are transactional by default in Grails. It is not needed here.
	static transactional = false
	static scope = "singleton"

    def solveTrips(jsonTrips) {
//		logger.debug "jsonTrips=$jsonTrips"
		def optimisation = [gain: 0, path: []]
		
		// Trip map, grouped/sorted by DEPART and sorted by DUREE
		def tripMap = 
			new TreeMap(
				jsonTrips
					.collectEntries { [(it.DEPART) : []] }
					.each { key, value ->
						def jsonTripsPart = jsonTrips.findAll { it.DEPART == key }.sort{ it.DUREE }
						value.addAll jsonTripsPart
					})
//		logger.debug "tripMap=$tripMap"
		
		def lastDEPART = tripMap.lastKey()
//		logger.debug "lastDEPART=$lastDEPART"
		
		def optimisationSet = new HashSet()
		def optimize 
		optimize = { selectedTrips, currentDEPART ->
//			logger.debug "currentDEPART=$currentDEPART selectedTrips=$selectedTrips"
			if(currentDEPART > lastDEPART) {
				optimisationSet.add selectedTrips
			} else {
				tripMap[currentDEPART].each {
//					logger.debug "goto nextDEPART=" + (currentDEPART + it.DUREE) + " with trip=$it"
					// add it to selectedTrips without modifying it
					optimize(selectedTrips + it, currentDEPART + it.DUREE)
				}
				// Without selectioning any trip with currentDEPART level
				optimize(selectedTrips, ++currentDEPART)
			}
		}
		optimize([], 0)
//		logger.debug "optimisationSet=$optimisationSet"
		
		def optimizedTrips = optimisationSet.max { it.sum { it.PRIX } }
//		logger.debug "optimizedTrips=$optimizedTrips"
		
		optimisation = [gain: optimizedTrips.sum { it.PRIX }, path: optimizedTrips.collect { it.VOL }]
//		logger.debug "optimisation=$optimisation"
		return optimisation
	}
}
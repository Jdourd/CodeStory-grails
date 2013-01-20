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
		
		def optimizedTrips = [gain:0, trips:[]]
		// By default, acceptable loss is all trips PRIX
		def maxLoss = jsonTrips.sum { it.PRIX }
		def acceptableLoss = maxLoss
		def optimize 
		optimize = { selectedTrips, currentDEPART, currentLoss ->
//			logger.debug "currentDEPART=$currentDEPART selectedTrips=$selectedTrips currentLoss=$currentLoss/$acceptableLoss"
			if(currentLoss < acceptableLoss) {
				if(currentDEPART > lastDEPART) {
					def currentGain = selectedTrips.sum { it.PRIX }
					if(optimizedTrips.gain < currentGain) {
						optimizedTrips = [gain:currentGain, trips: selectedTrips]
						acceptableLoss = maxLoss - currentGain
					}
				} else {
					def currentDEPARTmaxLoss = 0
					if(tripMap[currentDEPART] != null) {
						currentDEPARTmaxLoss = tripMap[currentDEPART].sum { it.PRIX }
						tripMap[currentDEPART].each {
							// add it to selectedTrips without modifying it
							optimize(selectedTrips + it, currentDEPART + it.DUREE, currentLoss + currentDEPARTmaxLoss - it.PRIX)
						}
					}
					// Without selectioning any trip with currentDEPART level
					optimize(selectedTrips, ++currentDEPART, currentLoss + currentDEPARTmaxLoss)
				}
			} else {
//				logger.debug "skipping it"
			}
		}
		optimize([], 0, 0)
//		logger.debug "optimizedTrips=$optimizedTrips"
		
		optimisation = [gain: optimizedTrips.gain, path: optimizedTrips.trips.collect { it.VOL }]
//		logger.debug "optimisation=$optimisation"
		return optimisation
	}
}
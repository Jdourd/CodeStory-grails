package grails.story

import org.apache.commons.logging.LogFactory
import grails.converters.JSON
import groovy.time.*
import groovy.transform.ToString

class TripSolverService {

	private static final logger = LogFactory.getLog(this)
	
	// Services are transactional by default in Grails. It is not needed here.
	static transactional = false
	static scope = "singleton"

	/** Algotrithm designed with help of Jeremie Lussiez 
	 * New algorithm with help of Herr Casaux */
    def solveTrips(jsonTrips) {
		def optimisation = [gain: 0, path: []]
//		logger.debug "jsonTrips=$jsonTrips"
		def departArriveeList = []
		
//		def addStart = new Date()
		jsonTrips.each { 
			departArriveeList.add(new CapsuleDeTransport(depart: true,  value: it.DEPART,            trip: it))
			departArriveeList.add(new CapsuleDeTransport(depart: false, value: it.DEPART + it.DUREE, trip: it))
		}
		
//		def sortStart = new Date()
		def meilleurACetInstant = null
		departArriveeList = departArriveeList
			.sort { a,b ->
				def comparison = a.value <=> b.value
				if(comparison == 0) { comparison = a.depart <=> b.depart }
				- comparison
			}
			
//		def algoStart = new Date()
		departArriveeList
			.each {
				def trip = it.trip
				if(!it.depart) {
					if(meilleurACetInstant != null) {
						trip['fils prodige'] = meilleurACetInstant
					}
				} else {
					trip['gain'] = trip.PRIX
					if(trip['fils prodige'] != null) {
						trip['gain'] += trip['fils prodige']['gain']
					}
					if(meilleurACetInstant == null || trip['gain'] > meilleurACetInstant['gain']) {
						meilleurACetInstant = trip
					}
				}
			}
		
			
//		def renderStart = new Date()
		optimisation['gain'] = meilleurACetInstant['gain']
		optimisation['path'].add(meilleurACetInstant.VOL)
		while(meilleurACetInstant['fils prodige'] != null) {
				optimisation['path'].add(meilleurACetInstant['fils prodige'].VOL)
				meilleurACetInstant = meilleurACetInstant['fils prodige']
		}
		
//		def stop = new Date()
//		TimeDuration addDuration = TimeCategory.minus(sortStart, addStart)
//		TimeDuration sortDuration = TimeCategory.minus(algoStart, sortStart)
//		TimeDuration algoDuration = TimeCategory.minus(renderStart, algoStart)
//		TimeDuration renderDuration = TimeCategory.minus(stop, renderStart)
//		TimeDuration totalDuration = TimeCategory.minus(stop, addStart)
//		println "total=$totalDuration\tadd=$addDuration\tsort=$sortDuration\talgo=$algoDuration\trender=$renderDuration"
		
//		logger.debug "optimisation=$optimisation"
		return optimisation
	}
	
	@ToString
	class CapsuleDeTransport {
		def depart
		def value
		def trip
	}
}
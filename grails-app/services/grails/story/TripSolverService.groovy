package grails.story

import org.apache.commons.logging.LogFactory
import grails.converters.JSON
import groovy.time.*
import groovy.transform.ToString

class TripSolverService {

	private static final logger = LogFactory.getLog(this)
	private static final timeLogger = LogFactory.getLog('grails.story.TripSolverServiceTime')
	
	// Services are transactional by default in Grails. It is not needed here.
	static transactional = false
	static scope = 'singleton'

	/** Algotrithm designed with help of Jeremie Lussiez 
	 * New algorithm with help of Herr Casaux */
    def solveTrips(jsonTrips) {
		def optimisation = new Optimisation()
//		logger.debug "jsonTrips=$jsonTrips"
		def departArriveeList = []
		Date addStart, sortStart, algoStart, renderStart, stop
		
		if(timeLogger.isDebugEnabled()) addStart = new Date()
		jsonTrips.each { 
			departArriveeList << new CapsuleDeTransport(depart: true,  value: it.DEPART,            trip: it)
			departArriveeList << new CapsuleDeTransport(depart: false, value: it.DEPART + it.DUREE, trip: it)
		}
		
		if(timeLogger.isDebugEnabled()) sortStart = new Date()
		departArriveeList = departArriveeList.sort()
			
		if(timeLogger.isDebugEnabled()) algoStart = new Date()
		def meilleurACetInstant = null
		departArriveeList
			.each {
				def trip = it.trip
				if(!it.depart) {
					if(meilleurACetInstant) {
						trip.filsProdige = meilleurACetInstant
					}
				} else {
					trip.gain = trip.PRIX + (trip.filsProdige?.gain ?: 0)
					if(trip.gain > meilleurACetInstant?.gain) {
						meilleurACetInstant = trip
					}
				}
			}
			
		if(timeLogger.isDebugEnabled()) renderStart = new Date()
		optimisation.gain = meilleurACetInstant.gain
		optimisation.path << meilleurACetInstant.VOL
		while(meilleurACetInstant.filsProdige) {
			optimisation.path << meilleurACetInstant.filsProdige.VOL
			meilleurACetInstant = meilleurACetInstant.filsProdige
		}
		
		if(timeLogger.isDebugEnabled()) {
			stop = new Date()
			TimeDuration addDuration = TimeCategory.minus(sortStart, addStart)
			TimeDuration sortDuration = TimeCategory.minus(algoStart, sortStart)
			TimeDuration algoDuration = TimeCategory.minus(renderStart, algoStart)
			TimeDuration renderDuration = TimeCategory.minus(stop, renderStart)
			TimeDuration totalDuration = TimeCategory.minus(stop, addStart)
			timeLogger.debug "total=${totalDuration}\tadd=${addDuration}\tsort=${sortDuration}\talgo=${algoDuration}\trender=${renderDuration}"
		}
//		logger.debug "optimisation=$optimisation"
		return optimisation
	}
	
	@ToString(includeNames=true)
	class CapsuleDeTransport implements Comparable<CapsuleDeTransport> {
		def depart
		def value
		def trip
		
		int compareTo(other) {
			- (this.value <=> other.value ?: this.depart <=> other.depart)
		}
	}
	
	static class Optimisation {
		def gain = 0
		def path = []
		
		boolean equals(other) {
			if (!(other instanceof Optimisation)) {
				  false
			} else {
				this.gain == other.gain && this.path == other.path
			}
		}
	}
}
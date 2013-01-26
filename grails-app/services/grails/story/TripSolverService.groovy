package grails.story

import org.apache.commons.logging.LogFactory
import grails.converters.JSON
import groovy.time.*

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
		def departList = 
			jsonTrips.collect {
				['tag':'DEPART', 'value': it.DEPART, 'trip':it]
			}
		def arriveeList = 
			jsonTrips.collect {
				it['ARRIVEE'] = it.DEPART + it.DUREE
				['tag':'ARRIVEE', 'value': it.ARRIVEE, 'trip':it]
			}
		def departArriveeList = (departList + arriveeList).sort { - it.value }
//		println departArriveeList
		
		// pour chaque trip
		// si c'est une arrivée, tu lui donne ton meilleur trajet
		// si c'est un départ, tu le calcules pour voir s'il devient le meilleur départ
		def meilleurACetInstant = null
		departArriveeList.each {
			def trip = it.trip
			if(it.tag == 'ARRIVEE') {
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
		
		optimisation['gain'] = meilleurACetInstant['gain']
		optimisation['path'] += meilleurACetInstant.VOL
		while(meilleurACetInstant['fils prodige'] != null) {
				optimisation['path'] += meilleurACetInstant['fils prodige'].VOL
				meilleurACetInstant = meilleurACetInstant['fils prodige']
		}
//		logger.debug "optimisation=$optimisation"
		return optimisation
	}
}
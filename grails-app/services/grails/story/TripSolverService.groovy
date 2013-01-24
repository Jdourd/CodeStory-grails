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
//		logger.debug "jsonTrips=$jsonTrips"
		def optimisation = [gain: 0, path: []]

		// tri topologique
		jsonTrips = jsonTrips.sort { - it.DEPART }
		
// 		Calcul d'un gain cumulé :
//		 - s'il est déjà calculé, je le retourne
//		 - s'il n'a pas de fils, noeud.GAIN_CUMULE = noeud.PRIX
//		 - s'il a un fils, noeud.GAIN_CUMULE = noeud.PRIX + fils.GAIN_CUMULE et je stocke le fils
//		 - s'il a plusieurs fils, noeud.GAIN_CUMULE = noeud.PRIX + max(fils.GAIN_CUMULE) et je stocke le fils qui a le max(père.GAIN_CUMULE)
		def calculGainCumule
		calculGainCumule = { trip ->
			if(!trip.containsKey('cumul')) {
				trip['cumul'] = trip.PRIX
				def departFils = trip.DEPART + trip.DUREE
				def timeStart = new Date()
				def indexPremierFils = jsonTrips.findIndexOf { departFils > it.DEPART }
				def fils = jsonTrips[0..<indexPremierFils]
				def timeStop = new Date()
				TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
				println("trip $trip.VOL processed in $duration.millis ms");
				if(fils != null && !fils.isEmpty()) {
					def filsProdige = fils.max { calculGainCumule it }
					trip['fils prodige'] = filsProdige
					trip['cumul'] += filsProdige['cumul']
				}
			}
			return trip['cumul']
		}
		
		def tripStart = jsonTrips.max { calculGainCumule it }
		optimisation['gain'] = tripStart['cumul']
		optimisation['path'] += tripStart.VOL
		while(tripStart['fils prodige'] != null) {
			optimisation['path'] += tripStart['fils prodige'].VOL
			tripStart = tripStart['fils prodige']
		}
					
//		logger.debug "optimisation=$optimisation"
		return optimisation
	}
}
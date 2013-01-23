package grails.story

import org.apache.commons.logging.LogFactory
import grails.converters.JSON

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
//			logger.debug "$trip.VOL | pere         => " + trip
			if(!trip.containsKey('cumul')) {
//				logger.debug "$trip.VOL | cumul not found, calculating it !"
				trip['cumul'] = trip.PRIX
				def fils = jsonTrips.findAll { trip.DEPART + trip.DUREE <= it.DEPART }
//				logger.debug "$trip.VOL | fils         => " + fils
				if(fils != null && !fils.isEmpty()) {
//					logger.debug "$trip.VOL | I have childs !"
					def filsProdige = fils.max { unFils ->
						calculGainCumule unFils
					}
					trip['fils prodige'] = filsProdige
					trip['cumul'] += filsProdige['cumul']
				}
			}
//			logger.debug "$trip.VOL | fils prodige => " + trip['fils prodige']
//			logger.debug "$trip.VOL | gain cumule  => " + trip['cumul']
			return trip['cumul']
		}
		
		// En considérant chaque noeud comme un départ de mon arbre, je calcul le gain cumulé du prix de mon noeud + prix du fils cumulé
		def tripPart = jsonTrips.max { trip -> calculGainCumule trip }
		optimisation['gain'] = tripPart['cumul']
		optimisation['path'] += tripPart.VOL
		while(tripPart['fils prodige'] != null) {
			optimisation['path'] += tripPart['fils prodige'].VOL
			tripPart = tripPart['fils prodige']
		}
					
//		logger.debug "optimisation=$optimisation"
		return optimisation
	}
}
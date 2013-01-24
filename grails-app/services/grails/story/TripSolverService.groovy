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
		def cumul = new TreeMap()
		
		// tri topologique
		jsonTrips =
        	jsonTrips
                .sort { - it.DEPART }
                .eachWithIndex { trip, index ->
					cumul.put(trip.DEPART, null) 
					trip['index'] = index
				}
//		logger.debug "jsonTrips=$jsonTrips"
		def lastCumul = cumul.lastKey()
		
// 		Calcul d'un gain cumulé :
//		 - s'il est déjà calculé, je le retourne
//		 - s'il n'a pas de fils, noeud.GAIN_CUMULE = noeud.PRIX
//		 - s'il a un fils, noeud.GAIN_CUMULE = noeud.PRIX + fils.GAIN_CUMULE et je stocke le fils
//		 - s'il a plusieurs fils, noeud.GAIN_CUMULE = noeud.PRIX + max(fils.GAIN_CUMULE) et je stocke le fils qui a le max(père.GAIN_CUMULE)
		def calculGainCumule
		calculGainCumule = { trip ->
//			println "vol $trip.VOL"
			if(!trip.containsKey('cumul')) {
				trip['cumul'] = trip.PRIX
				def departFils = trip.DEPART + trip.DUREE
//				println jsonTrips[trip['index']..0]
				def cumulProche = cumul.subMap(trip.DEPART, lastCumul).find { departFils <= it.key}
				if(cumulProche != null && cumulProche.value != null) {
					trip['fils prodige'] = cumulProche.value
					trip['cumul'] += cumulProche.value['cumul']
				} else {
					def premierFils = jsonTrips[trip['index']..0].find { departFils <= it.DEPART }
	//				println "premier fils $premierFils"
					if(premierFils != null) {
						def fils = jsonTrips[0..premierFils['index']]
	//					println "fils $fils"
						if(fils != null && !fils.isEmpty()) {
							def filsProdige = fils.max { calculGainCumule it }
							cumul.put(filsProdige.DEPART, filsProdige)
							trip['fils prodige'] = filsProdige
							trip['cumul'] += filsProdige['cumul']
						}
					}
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
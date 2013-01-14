package grails.story

import org.apache.commons.logging.LogFactory
import grails.converters.JSON
import JaCoP.core.*
import JaCoP.constraints.*
import JaCoP.search.*

class JajascriptController {

	private static final logger = LogFactory.getLog(this)
	
	// Service injection
	def tripSolverService

    def optimize = {
		def trips
		if(!request.JSON.isEmpty()) {
			trips = request.JSON
			logger.debug "json request=$trips"
		} else {
			trips = params.find { true }.key // get first key
			trips = JSON.parse trips
			logger.debug "map request=$trips"
		}
		def optimisation = tripSolverService.solveTrips(trips)
		logger.debug "optimisation=$optimisation"
		def converter = optimisation as JSON
		converter.prettyPrint = true
		render(text: converter.toString(), contentType: "application/json")
	}
}

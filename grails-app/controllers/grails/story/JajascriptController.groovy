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
		logger.debug "request=$request.JSON"
		def optimisation = tripSolverService.solveTrips(request.JSON)
		logger.debug "optimisation=$optimisation"
		render(optimisation as JSON)
	}
}

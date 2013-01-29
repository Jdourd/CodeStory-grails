package grails.story

import org.apache.commons.logging.LogFactory
import grails.converters.JSON
import groovy.time.*

class JajascriptController {

	private static final logger = LogFactory.getLog(this)
	
	// Service injection
	def tripSolverService

    def optimize = {
		def trips
		def readJsonDate = new Date()
		if(!request.JSON.isEmpty()) {
			trips = request.JSON
//			logger.debug "json request=$trips"
		} else {
			trips = params.find { true }.key // get first key
			trips = JSON.parse trips
//			logger.debug "map request=$trips"
		}
		def callSolverDate = new Date()
		def optimisation = tripSolverService.solveTrips(trips)
//		logger.debug "optimisation=$optimisation"
		def stopDate = new Date()
		TimeDuration jsonDuration = TimeCategory.minus(callSolverDate, readJsonDate)
		TimeDuration solverDuration = TimeCategory.minus(stopDate, callSolverDate)
		TimeDuration totalDuration = TimeCategory.minus(stopDate, readJsonDate)
		logger.info "total=$totalDuration\tjson=$jsonDuration\tsolver=$solverDuration"
		render optimisation as JSON
	}
}

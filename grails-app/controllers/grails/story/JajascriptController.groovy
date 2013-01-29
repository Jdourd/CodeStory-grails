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
		def parsingJsonDate = new Date()
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
		
		def marshallingJsonDate = new Date()
		def jsonOptimisation = optimisation as JSON
		
		def stopDate = new Date()
		TimeDuration parsingJsonDuration = TimeCategory.minus(callSolverDate, parsingJsonDate)
		TimeDuration solverDuration = TimeCategory.minus(marshallingJsonDate, callSolverDate)
		TimeDuration marshallingDuration = TimeCategory.minus(stopDate, marshallingJsonDate)
		TimeDuration totalDuration = TimeCategory.minus(stopDate, parsingJsonDate)
		logger.info "total=$totalDuration\tparsing=$parsingJsonDuration\tsolver=$solverDuration\tmarshalling=$marshallingDuration"
		render jsonOptimisation
	}
}

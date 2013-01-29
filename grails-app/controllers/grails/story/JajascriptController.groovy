package grails.story

import org.apache.commons.logging.LogFactory
import grails.converters.JSON
import groovy.time.*
import org.codehaus.jackson.map.ObjectMapper

class JajascriptController {

	private static final logger = LogFactory.getLog(this)
	
	// Service injection
	def tripSolverService

    def optimize = {
		def trips
		def readingParameterDate = new Date()
		trips = request.getReader().text
		if(trips.size() == 0){
			trips = params.find { true }.key
		}
		
		def parsingJsonDate = new Date()
		trips = new ObjectMapper().readValue(trips, List.class)
		logger.info "map request : " + trips.size()
		
		def callSolverDate = new Date()
		def optimisation = tripSolverService.solveTrips(trips)
//		logger.debug "optimisation=$optimisation"
		
		def marshallingJsonDate = new Date()
		def jsonOptimisation = new ObjectMapper().writeValueAsString(optimisation)
		
		def stopDate = new Date()
		TimeDuration readingParameterDuration = TimeCategory.minus(parsingJsonDate, readingParameterDate)
		TimeDuration parsingJsonDuration = TimeCategory.minus(callSolverDate, parsingJsonDate)
		TimeDuration solverDuration = TimeCategory.minus(marshallingJsonDate, callSolverDate)
		TimeDuration marshallingDuration = TimeCategory.minus(stopDate, marshallingJsonDate)
		TimeDuration totalDuration = TimeCategory.minus(stopDate, readingParameterDate)
		logger.info "total=$totalDuration\treading=$readingParameterDuration\tparsing=$parsingJsonDuration\tsolver=$solverDuration\tmarshalling=$marshallingDuration"
		render jsonOptimisation
	}
}

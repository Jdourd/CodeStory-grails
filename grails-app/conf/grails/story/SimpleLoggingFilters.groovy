package grails.story

import java.util.logging.Logger

class SimpleLoggingFilters {

	private static final loggingFilter = Logger.getLogger('ut')

	def filters = {
		all(uriExclude: '/jajascript/optimize') {
			before = {
			  
			  	def logString = request.getMethod() + " " + request.getRequestURI() + " "
				
				if(!params.isEmpty()) {
					logString += params
					logString += " "
				}
			  	
				def contentType = request.getContentType()
				logString += contentType + " "
				
			  	def contentLength = request.getContentLength()
				logString += contentLength + " "
			    
				loggingFilter.info(logString)
			}
            after = {
                response.setHeader('Server', 'grails-story')
            }
		}
	}
}
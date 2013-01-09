package grails.story

import java.util.logging.Logger

class SimpleLoggingFilters {

	private static final loggingFilter = Logger.getLogger('ut')

	def filters = {
		all(uri:'/**') {
			before = {
			  
			  	def logString = request.getMethod() + " " + request.getRequestURI() + " "
				
				def params = request.getParameterMap()
				if(!params.isEmpty()) {
					logString += params
				}
			  	
			  	def contentLength = request.getContentLength()
				if(contentLength > 0) {
					def data = retrieveBody(request)
					logString += contentLength + " " + data
				}
			    
				loggingFilter.info(logString)
			}
            after = {
                response.setHeader('Server', 'grails-story')
            }
		}
	}
	
	String retrieveBody(request) {
		BufferedReader reader = request.getReader()
		StringBuilder sb = new StringBuilder()
	    String line = reader.readLine()
	    while (line != null) {
	        sb.append(line + "\n")
	        line = reader.readLine()
	    }
	    reader.close()
	    return sb.toString()
	}
}
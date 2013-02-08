package grails.story

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.JSON

class JajascriptTesterController {

    def index() { 
		def max = params.id as int
		Random random = new Random()
		def trips = []
		for (int i = 1; i <= max; i++) {
			def trip = [:]
			trip['VOL'] = Integer.toBinaryString(i)
			trip['DEPART'] = i
			trip['DUREE'] = random.nextInt(100) + 1
			trip['PRIX'] = random.nextInt(20) + 1
			trips.add(trip)
		}
		
		def http = new HTTPBuilder( 'http://grails-story.herokuapp.com' )
		http.post( path: 'jajascript/optimize', body: trips, requestContentType: JSON) { resp ->
		   render "response status: ${resp.statusLine}"
		}
	}
	
	def sample() {
		def trips = this.class.getResource("/resources/jajascript-10k.txt").text
		
		def http = new HTTPBuilder( 'http://localhost:8080/grails-story/' )
		http.post( path: 'jajascript/optimize', body: trips, requestContentType: JSON) { resp ->
		   render "response status: ${resp.statusLine}"
		}
	}
}
package grails.story

import grails.converters.JSON

class ScalaskelChangeController {
	
	def json = {
		def map= [[foo:params.value]]
		
		render(map as JSON)
	}
}
package grails.story

import grails.converters.JSON
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(JajascriptController)
class JajascriptControllerTests {
	
	void test_optimize_simple_combinaison() {
		def propositions = '[{ "VOL": "MONAD42", "DEPART": 0, "DUREE": 5, "PRIX": 10 }]'
		def expectedOptimisation = ["gain" : 10, "path" : ["MONAD42"]]
		askAndCheckResponse(propositions, expectedOptimisation)
    }

    void test_expanded_example_combinaison() {
		def propositions = '[{ "VOL": "MONAD42", "DEPART": 0, "DUREE": 5, "PRIX": 10 },' + 
							'{ "VOL": "META18", "DEPART": 3, "DUREE": 7, "PRIX": 14 },' + 
							'{ "VOL": "LEGACY01", "DEPART": 5, "DUREE": 9, "PRIX": 8 },' + 
							'{ "VOL": "YAGNI17", "DEPART": 5, "DUREE": 9, "PRIX": 7 },' + 
							'{ "VOL": "TABOUL1", "DEPART": 0, "DUREE": 20, "PRIX": 20 },' + 
							'{ "VOL": "PISTAC03", "DEPART": 15, "DUREE": 3, "PRIX": 5 }]'
		def expectedOptimisation = ["gain" : 23, "path" : ["MONAD42","LEGACY01","PISTAC03"]]
		askAndCheckResponse(propositions, expectedOptimisation)
    }
	
	def askAndCheckResponse(propositions, expectedResponse){
		defineBeans {
			tripSolverService(TripSolverService)
		}
		request.content = propositions
		controller.optimize()
		def responseObject = JSON.parse(response.contentAsString)
		assert responseObject == expectedResponse
	}
}
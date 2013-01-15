package grails.story



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(TripSolverService)
class TripSolverServiceTests {
	
	void test_optimize_simple_combinaison() {
		def propositions = [
          [ "VOL": "MONAD42", "DEPART": 0, "DUREE": 5, "PRIX": 10 ]
		]
		def expectedOptimisation = ["gain" : 10, "path" : ["MONAD42"]]
		assert service.solveTrips(propositions) == expectedOptimisation
    }
	
	void test_optimize_two_simple_combinaisons() {
		def propositions = [
          [ "VOL": "MONAD42", "DEPART": 0, "DUREE": 5, "PRIX": 10 ],
          [ "VOL": "LEGACY01", "DEPART": 5, "DUREE": 9, "PRIX": 8 ]
		]
		def expectedOptimisation = ["gain" : 18, "path" : ["MONAD42","LEGACY01"]]
		assert service.solveTrips(propositions) == expectedOptimisation
    }
	
	void test_optimize_two_conflict_combinaisons() {
		def propositions = [
          [ "VOL": "MONAD42", "DEPART": 0, "DUREE": 5, "PRIX": 10 ],
          [ "VOL": "LEGACY01", "DEPART": 0, "DUREE": 9, "PRIX": 18 ]
		]
		def expectedOptimisation = ["gain" : 18, "path" : ["LEGACY01"]]
		assert service.solveTrips(propositions) == expectedOptimisation
    }

    void test_example_combinaison() {
		def propositions = [
          [ "VOL": "MONAD42", "DEPART": 0, "DUREE": 5, "PRIX": 10 ],
          [ "VOL": "META18", "DEPART": 3, "DUREE": 7, "PRIX": 14 ],
          [ "VOL": "LEGACY01", "DEPART": 5, "DUREE": 9, "PRIX": 8 ],
          [ "VOL": "YAGNI17", "DEPART": 5, "DUREE": 9, "PRIX": 7 ]
		]
		def expectedOptimisation = ["gain" : 18, "path" : ["MONAD42","LEGACY01"]]
		assert service.solveTrips(propositions) == expectedOptimisation
    }

    void test_expanded_example_combinaison() {
		def propositions = [
          [ "VOL": "MONAD42", "DEPART": 0, "DUREE": 5, "PRIX": 10 ],
          [ "VOL": "META18", "DEPART": 3, "DUREE": 7, "PRIX": 14 ],
          [ "VOL": "LEGACY01", "DEPART": 5, "DUREE": 9, "PRIX": 8 ],
          [ "VOL": "YAGNI17", "DEPART": 5, "DUREE": 9, "PRIX": 7 ],
          [ "VOL": "TABOUL1", "DEPART": 0, "DUREE": 20, "PRIX": 20 ],
          [ "VOL": "PISTAC03", "DEPART": 15, "DUREE": 3, "PRIX": 5 ]
		]
		def expectedOptimisation = ["gain" : 23, "path" : ["MONAD42","LEGACY01","PISTAC03"]]
		assert service.solveTrips(propositions) == expectedOptimisation
    }
	
	void test_trip_with_inactivity() {
		def propositions = [
			["VOL": "AF1", "DEPART":0, "DUREE":1, "PRIX": 2],
			["VOL": "AF2", "DEPART":4, "DUREE":1, "PRIX": 4],
			["VOL": "AF3", "DEPART":2, "DUREE":1, "PRIX": 6]
		]
		def expectedOptimisation = [ "gain": 12, "path": ["AF1","AF3","AF2"]]
		assert service.solveTrips(propositions) == expectedOptimisation
	}
}
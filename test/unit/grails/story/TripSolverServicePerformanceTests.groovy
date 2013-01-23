package grails.story

import static org.junit.Assert.*

import grails.test.mixin.*
import groovy.time.*
import org.junit.*

@TestFor(TripSolverService)
class TripSolverServicePerformanceTests {
	
	@Ignore
    void testSomething() {
        println "launching test"
        int max = 10
        Random random = new Random()
        for (int i =0; i < max; i++) {
            def trips = []
            for (int j = 1; j <= (i+1)*5; j++) {
                def trip = [:]
                trip['VOL'] = Integer.toBinaryString(j)
                trip['DEPART'] = j
                trip['DUREE'] = random.nextInt(10) + 1
                trip['PRIX'] = random.nextInt(20) + 1
                trips.add(trip)
            }
			def timeStart = new Date()
            new TripSolverService().solveTrips(trips)
			def timeStop = new Date()
			TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
            println("Level " + (i+1) + " : " + duration);
        }
    }
}
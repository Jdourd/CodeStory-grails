package grails.story

import static org.junit.Assert.*

import grails.converters.JSON
import grails.test.mixin.*
import groovy.time.*
import org.junit.*

@TestFor(TripSolverService)
class TripSolverServicePerformanceTests {
	
	@Ignore
    void testSomething() {
        int max = 2000
        Random random = new Random()
//        for (int i =0; i < max+1; i=i+10000) {
		for (int i =max; i < max+1; i++) {
            def trips = []
            for (int j = 1; j <= i*5; j++) {
                def trip = [:]
                trip['VOL'] = Integer.toBinaryString(j)
                trip['DEPART'] = j
                trip['DUREE'] = random.nextInt(100) + 1
                trip['PRIX'] = random.nextInt(20) + 1
                trips.add(trip)
            }
			def timeStart = new Date()
            new TripSolverService().solveTrips(trips)
			def timeStop = new Date()
			TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
            println("Level " + trips.size() + " : " + duration);
        }
    }
}

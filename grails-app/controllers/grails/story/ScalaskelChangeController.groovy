package grails.story

import grails.converters.JSON

class ScalaskelChangeController {
	
	def bazValueInCent = 21
	def qixValueInCent = 11
	def barValueInCent = 7
	
	def json = {
		def centToChange = params.value as int
		def simpleCombinaisons = getSimpleCombinaisons(centToChange)
		def map = getOtherCombination(simpleCombinaisons)
		render(map as JSON)
	}
	
	def getSimpleCombinaisons(centToChange) {
		getSimpleBazCombinaison(centToChange)
		getSimpleQixCombinaison(centToChange)
		getSimpleBarCombinaison(centToChange)
	}

	def getSimpleBazCombinaison(int centToChange) {
		def baz = (centToChange / bazValueInCent) as int
		def restInCent = centToChange - baz * bazValueInCent
		def bazAndQix = getSimpleQixCombinaison(restInCent)
		bazAndQix.baz = baz
		def bazAndBar = getSimpleBarCombinaison(restInCent)
		bazAndBar.baz = baz
		return [bazAndQix, bazAndBar]
	}

	def getSimpleQixCombinaison(int centToChange) {
		def qix = (centToChange / qixValueInCent) as int
		def restInCent = centToChange - qix * qixValueInCent
		def qixAndBar = getSimpleBarCombinaison(restInCent)
		qixAndBar.qix = qix
		return qixAndBar
	}

	def getSimpleBarCombinaison(int centToChange) {
		def bar = (centToChange / barValueInCent) as int
		def foo = centToChange - bar * barValueInCent
		return ["bar":bar, "foo":foo]
	}
	
	def getOtherCombination(existingCombinaisons) {
		def combinaisons = new HashSet()
		existingCombinaisons.each {
			addCombinaison(combinaisons, it.baz, it.qix, it.bar, it.foo)
			
		}
		return combinaisons
	}
	
	def addCombinaison(set, baz, qix, bar, foo) {
		def change = new HashMap()
		if(baz > 0){
			change.baz=baz
		}
		if(qix > 0){
			change.qix=qix
		}
		if(bar > 0){
			change.bar=bar
		}
		if(foo > 0){
			change.foo=foo
		}
		println "Map : $change"
		set.add(change)
	}
	
	def combineBaz(set, baz, qix, bar, foo) {
		def reduceBazIntoQix = {
			baz -- // -21
			if(bar > 1) {
				qix += 2 // +22
				bar -- // -7
				foo += 5 // +5
			} else if(foo >= 1) {
				qix += // +22
				foo -- // -1
			} else if(foo >= 4) {
				qix ++ // +11
				bar +=2 // +14
				foo -= 4 // -4
			} else {
				qix ++ // +11
				bar ++ // +7
				foo += 3 // +3
			}
			println "Reduce baz into qiz : $baz, $qix, $bar, $foo"
		}
		for (; baz >= 0; reduceBazIntoQix()) {
			println "Baz : $baz, $qix, $bar, $foo"
			addCombinaison(set, baz, qix, bar, foo)
			combineQix(set, baz, qix, bar, foo)
			combineBar(set, baz, qix, bar, foo)
		}
	}
	
	def combineQix(set, baz, qix, bar, foo) {
		def reduceQix = {
			qix -- // -11
			bar ++ // +7
			foo += 4 // +4
			println "Reduce qix : $baz, $qix, $bar, $foo"
		}
		for (; qix >= 0; reduceQix()) {
			println "Qix : $baz, $qix, $bar, $foo"
			addCombinaison(set, baz, qix, bar, foo)
			combineBar(set, baz, qix, bar, foo)
		}
	}
	
	def combineBar(set, baz, qix, bar, foo) {
		def reduceBar = {
			bar -- // -7
			foo += 7 // +7
			println "Reduce bar : $baz, $qix, $bar, $foo"
		}
		for (; bar >= 0; reduceBar()) {
			println "Bar : $baz, $qix, $bar, $foo"
			addCombinaison(set, baz, qix, bar, foo)
		}
	}
}
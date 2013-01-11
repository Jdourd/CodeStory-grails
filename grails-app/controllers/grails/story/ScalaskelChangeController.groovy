package grails.story

import org.apache.commons.logging.LogFactory
import grails.converters.JSON

class ScalaskelChangeController {

	private static final logger = LogFactory.getLog(this)
	
	def bazValueInCent = 21
	def qixValueInCent = 11
	def barValueInCent = 7
	
	def json = {
		def centToChange = params.value as int
		def combinaisons = new HashSet()
		
		def baz = (centToChange / bazValueInCent) as int
		logger.debug "baz:$baz"
		for(; baz > -1; baz --) {
			def restFromBazToChange = centToChange - baz * bazValueInCent
			def qix = (restFromBazToChange / qixValueInCent) as int
			logger.debug "baz:$baz\tqix:$qix"
			for(; qix > -1; qix --) {
				def restFromQixTochange = restFromBazToChange - qix * qixValueInCent
				def bar = (restFromQixTochange / barValueInCent) as int
				logger.debug "baz:$baz\tqix:$qix\tbar:$bar"
				for(; bar > -1; bar --) {
					def restFromBarTochange = restFromQixTochange - bar * barValueInCent
					def foo = restFromBarTochange as int
					logger.debug "baz:$baz\tqix:$qix\tbar:$bar\tfoo:$foo"
					addCombinaison(combinaisons, baz, qix, bar, foo)
				}
			}
		}
		render(combinaisons as JSON)
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
		logger.debug "Map : $change"
		set.add(change)
	}
}
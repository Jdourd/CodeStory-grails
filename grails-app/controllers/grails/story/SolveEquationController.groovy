package grails.story

import org.apache.commons.logging.LogFactory
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

class SolveEquationController {

	private static final logger = LogFactory.getLog(this)

    def solve() {
		ScriptEngineManager mgr = new ScriptEngineManager()
		ScriptEngine engine = mgr.getEngineByName("JavaScript")
		def cleanedEquation = params.q.replaceAll(/ +/, '+').replaceAll(',', '.')
		log.debug "equation=$cleanedEquation"
		def result = engine.eval(cleanedEquation)
		def resultAsInt = result as int
		if(result == resultAsInt) {
			render(text:resultAsInt, contentType:'text/plain')
		} else {
			def resultWithComma = (result as String).replace('.', ',')
			render(text:resultWithComma, contentType:'text/plain')
		}
	}
}
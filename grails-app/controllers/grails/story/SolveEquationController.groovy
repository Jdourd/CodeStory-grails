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
		logger.debug "equation=$cleanedEquation"
		def BigDecimal result = engine.eval(cleanedEquation)
		logger.debug "result=$result"
		
		def resultAsInt = result as int
		if(result == resultAsInt) {
			render(text:resultAsInt, contentType:'text/plain')
		} else {
			def resultWithComma = (result as String).replace('.', ',')
			render(text:resultWithComma, contentType:'text/plain')
		}
	}
}
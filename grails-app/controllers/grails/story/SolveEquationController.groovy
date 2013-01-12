package grails.story

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

class SolveEquationController {

    def solve() {
		ScriptEngineManager mgr = new ScriptEngineManager()
		ScriptEngine engine = mgr.getEngineByName("JavaScript")
		def result = engine.eval(params.q.replaceAll(/ +/, '+'))
		def resultAsInt = result as int
		if(result == resultAsInt) {
			render(text:resultAsInt, contentType:'text/plain')
		} else {
			render(text:result, contentType:'text/plain')
		}
	}
}
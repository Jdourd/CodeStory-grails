package grails.story

import org.apache.commons.logging.LogFactory

class SolveEquationController {

	private static final logger = LogFactory.getLog(this)

    def solve() {
		def cleanedEquation = params.q.replaceAll(/ +/, '+').replaceAll(',', '.')
		logger.debug "equation=$cleanedEquation"
		def BigDecimal result = Eval.me(cleanedEquation)
		logger.debug "result=$result"
		
		def resultAsInt = result as BigInteger
		if(result == resultAsInt) {
			render(text:resultAsInt, contentType:'text/plain')
		} else {
			def resultWithComma = (result as String).replace('.', ',')
			render(text:resultWithComma, contentType:'text/plain')
		}
	}
}
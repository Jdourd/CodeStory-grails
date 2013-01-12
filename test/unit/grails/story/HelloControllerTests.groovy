package grails.story

import static org.fest.assertions.Assertions.assertThat
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(HelloController)
class HelloControllerTests {

    void test_return_Hello_You_by_default() {
		askAndCheckResponse(null, 'Hello You !')
    }
	
	void test_should_return_email_when_asked(){
		askAndCheckResponse([q:'Quelle est ton adresse email'], 'g.dhordain@gmail.com')
	}
	
	void test_should_return_OUI_when_asked_for_mailing_list_subscription(){
		askAndCheckResponse([q:'Es tu abonne a la mailing list(OUI/NON)'], 'OUI')
	}
	
	void test_should_return_happiness_when_asked(){
		askAndCheckResponse([q:'Es tu heureux de participer(OUI/NON)'], 'OUI')
	}
	
	void test_should_return_OUI_when_asked_for_more(){
		askAndCheckResponse([q:'Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)'], 'OUI')
	}
	
	void test_should_return_NON_when_asked_for_dumbness(){
		askAndCheckResponse([q:'Est ce que tu reponds toujours oui(OUI/NON)'], 'NON')
	}
	
	void test_should_return_OUI_when_asked_for_first_statement(){
		askAndCheckResponse([q:'As tu bien recu le premier enonce(OUI/NON)'], 'OUI')
	}
	
	void test_should_return_QUELS_BUGS_when_asked_for_good_night_sleep(){
		askAndCheckResponse([q:'As tu passe une bonne nuit malgre les bugs de l etape precedente(PAS_TOP/BOF/QUELS_BUGS)'], 'QUELS_BUGS')
	}
	
	def askAndCheckResponse(parameters, expectedResponse){
		if(parameters != null) {
			controller.request.parameters = parameters
		}
		controller.index()
		assertEquals(expectedResponse, response.contentAsString)
	}
	
	void test_should_redirect_to_SolveEquationController_when_q_param_match_regex(){
		params.q = "1+1"
		controller.index()
		assertThat("/solveEquation/solve?q=$params.q").isEqualTo(URLDecoder.decode(response.redirectedUrl))
	}
	
	void test_should_redirect_to_SolveEquationController_when_q_param_match_regex_2(){
		params.q = "((1+2)+3+4+(5+6+7)+(8+9+10)*3)/2*5"
		controller.index()
		assertThat("/solveEquation/solve?q=$params.q").isEqualTo(URLDecoder.decode(response.redirectedUrl))
	}
}
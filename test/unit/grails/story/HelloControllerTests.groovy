package grails.story



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
	
	def askAndCheckResponse(parameters, expectedResponse){
		if(parameters != null) {
			controller.request.parameters = parameters
		}
		controller.index()
		assertEquals(expectedResponse, controller.response.contentAsString)
	}
}
package grails.story



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ScalaskelChangeController)
class ScalaskelChangeControllerTests {

    void test_should_change_1_cent(){
		askAndCheckResponse(1, '[{"foo":1}]')
	}

    void test_should_change_2_cent(){
		askAndCheckResponse(2, '[{"foo":2}]')
	}
	
	def askAndCheckResponse(change, expectedResponse){
		controller.params.value = change
		controller.json()
		assertEquals(expectedResponse, controller.response.contentAsString)
	}
}
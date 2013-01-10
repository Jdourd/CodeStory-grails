package grails.story



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ScalaskelChangeController)
class ScalaskelChangeControllerTests {

    void test_should_change_1_cent(){
		params.value = 1
		controller.json()
		assertEquals('[{"foo":1}]', controller.response.contentAsString)
	}
}
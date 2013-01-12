package grails.story

import static org.fest.assertions.Assertions.assertThat
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(SolveEquationController)
class SolveEquationControllerTests {

    void test_should_return_2_when_asked_1_plus_1() {
       askAndCheckResponse('1+1', '2')
    }
	
	def askAndCheckResponse(equation, expectedResponse){
		controller.params.q = equation
		controller.solve()
		assertThat(response.contentAsString).isEqualTo(expectedResponse)
	}
}

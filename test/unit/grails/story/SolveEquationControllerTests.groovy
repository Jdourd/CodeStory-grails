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
	
	void test_should_return_results() {
       askAndCheckResponse('((1+2)+3+4+(5+6+7)+(8+9+10)*3)/2*5', '272,5')
    }
	
	def askAndCheckResponse(equation, expectedResponse){
		controller.params.q = equation
		controller.solve()
		assertThat(response.contentAsString).isEqualTo(expectedResponse)
	}
}

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
	
	void test_should_return_results_with_comma() {
       askAndCheckResponse('1,5*4', '6')
    }
	
	void test_should_return_results_with_big_decimal() {
       askAndCheckResponse('((1.1+2)+3.14+4+(5+6+7)+(8+9+10)*4267387833344334647677634)/2*553344300034334349999000','31878018903828901761984975061078744643351263313920')
    }
	
	def askAndCheckResponse(equation, expectedResponse){
		controller.params.q = equation
		controller.solve()
		assertThat(response.contentAsString).isEqualTo(expectedResponse)
	}
}

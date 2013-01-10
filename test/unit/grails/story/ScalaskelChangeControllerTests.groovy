package grails.story

import static org.fest.assertions.Assertions.assertThat
import grails.converters.JSON
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ScalaskelChangeController)
class ScalaskelChangeControllerTests {

    void test_should_change_1_cent(){
		askAndCheckResponse(1, [["foo":1]])
	}

    void test_should_change_2_cent(){
		askAndCheckResponse(2, [["foo":2]])
	}

    void test_should_change_7_cent(){
		askAndCheckResponse(7, [["bar":1],["foo":7]])
	}

    void test_should_change_8_cent(){
		askAndCheckResponse(8, [["bar":1,"foo":1],["foo":8]])
	}

    void test_should_change_9_cent(){
		askAndCheckResponse(9, [["bar":1,"foo":2],["foo":9]])
	}

    void test_should_change_11_cent(){
		askAndCheckResponse(11, [["qix":1],["foo":11],["bar":1,"foo":4]])
	}

    void test_should_change_21_cent(){
		askAndCheckResponse(21, [
			["foo":14, "bar":1], ["baz":1], ["qix":1, "foo":3, "bar":1], ["qix":1, "foo":10], ["foo":7, "bar":2], ["foo":21]
			])
    }

    void test_should_change_25_cent(){
		askAndCheckResponse(25, [["baz":1,"foo":4], ["qix":2,"foo":3], ["qix":1,"bar":2], ["qix":1,"bar":1,"foo":7], ["qix":1,"foo":14], ["bar":3,"foo":4], ["bar":2,"foo":11], ["bar":1,"foo":18], ["foo":25]])
    }

    void test_should_change_55_cent(){
		askAndCheckResponse(55, [["baz":2,"qix":1,"foo":2], ["baz":1,"qix":3,"foo":1], ["baz":1,"qix":2,"bar":1,"foo":5], ["baz":1,"qix":2,"foo":12], ["baz":1,"qix":1,"bar":3,"foo":2], ["baz":1,"qix":1,"bar":2,"foo":9], ["baz":1,"qix":1,"bar":1,"foo":16], ["baz":1,"qix":1,"foo":23], ["baz":1,"bar":4,"foo":6], ["baz":1,"bar":3,"foo":13], ["baz":1,"bar":2,"foo":20], ["baz":1,"bar":1,"foo":27], ["baz":1,"foo":34], ["qix":5], ["qix":4,"bar":1,"foo":4], ["qix":4,"foo":11], ["qix":3,"bar":3,"foo":1], ["qix":3,"bar":2,"foo":8], ["qix":3,"bar":1,"foo":15], ["qix":3,"foo":22], ["qix":2,"bar":4,"foo":5], ["qix":2,"bar":3,"foo":12], ["qix":2,"bar":2,"foo":19], ["qix":2,"bar":1,"foo":26], ["qix":2,"foo":33], ["qix":1,"bar":6,"foo":2], ["qix":1,"bar":5,"foo":9], ["qix":1,"bar":4,"foo":16], ["qix":1,"bar":3,"foo":23], ["qix":1,"bar":2,"foo":30], ["qix":1,"bar":1,"foo":37], ["qix":1,"foo":44], ["bar":7,"foo":6], ["bar":6,"foo":13], ["bar":5,"foo":20], ["bar":4,"foo":27], ["bar":3,"foo":34], ["bar":2,"foo":41], ["bar":1,"foo":48], ["foo":55]])
    }
	
	def askAndCheckResponse(change, expectedResponse){
		controller.params.value = change
		controller.json()
		def response = JSON.parse(controller.response.contentAsString)
		expectedResponse.each({
			assertThat(response).contains(it)
		})
		assertThat(response).hasSize(expectedResponse.size())
	}
}
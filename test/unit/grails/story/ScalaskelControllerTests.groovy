package grails.story

import static org.fest.assertions.Assertions.assertThat
import grails.converters.JSON
import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ScalaskelController)
class ScalaskelControllerTests {

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
		askAndCheckResponse(21, [["baz":1], ["qix":1,"bar":1,"foo":3], ["qix":1,"foo":10], ["bar":3], ["bar":2,"foo":7], ["bar":1,"foo":14], ["foo":21]])
    }

    void test_should_change_25_cent(){
		askAndCheckResponse(25, [["baz":1,"foo":4], ["qix":2,"foo":3], ["qix":1,"bar":2], ["qix":1,"bar":1,"foo":7], ["qix":1,"foo":14], ["bar":3,"foo":4], ["bar":2,"foo":11], ["bar":1,"foo":18], ["foo":25]])
    }

    void test_should_change_55_cent(){
		askAndCheckResponse(55, [["baz":2,"qix":1,"foo":2], ["baz":2,"bar":1,"foo":6], ["baz":2,"foo":13], ["baz":1,"qix":3,"foo":1], ["baz":1,"qix":2,"bar":1,"foo":5], ["baz":1,"qix":2,"foo":12], ["baz":1,"qix":1,"bar":3,"foo":2], ["baz":1,"qix":1,"bar":2,"foo":9], ["baz":1,"qix":1,"bar":1,"foo":16], ["baz":1,"qix":1,"foo":23], ["baz":1,"bar":4,"foo":6], ["baz":1,"bar":3,"foo":13], ["baz":1,"bar":2,"foo":20], ["baz":1,"bar":1,"foo":27], ["baz":1,"foo":34], ["qix":5], ["qix":4,"bar":1,"foo":4], ["qix":4,"foo":11], ["qix":3,"bar":3,"foo":1], ["qix":3,"bar":2,"foo":8], ["qix":3,"bar":1,"foo":15], ["qix":3,"foo":22], ["qix":2,"bar":4,"foo":5], ["qix":2,"bar":3,"foo":12], ["qix":2,"bar":2,"foo":19], ["qix":2,"bar":1,"foo":26], ["qix":2,"foo":33], ["qix":1,"bar":6,"foo":2], ["qix":1,"bar":5,"foo":9], ["qix":1,"bar":4,"foo":16], ["qix":1,"bar":3,"foo":23], ["qix":1,"bar":2,"foo":30], ["qix":1,"bar":1,"foo":37], ["qix":1,"foo":44], ["bar":7,"foo":6], ["bar":6,"foo":13], ["bar":5,"foo":20], ["bar":4,"foo":27], ["bar":3,"foo":34], ["bar":2,"foo":41], ["bar":1,"foo":48], ["foo":55]])
    }
	
	def askAndCheckResponse(change, expectedResponse){
		params.id = change
		controller.change()
		def responseArray = JSON.parse(response.contentAsString)
		expectedResponse.each({
			assertThat(responseArray).contains(it)
		})
		assertThat(responseArray).hasSize(expectedResponse.size())
	}
	
	void test_should_change_100_cent(){
		askAndCheckResponse(100, [
			["baz":4,"qix":1,"foo":5], 
			["baz":4,"bar":2,"foo":2], 
			["baz":4,"bar":1,"foo":9], 
			["baz":4,"foo":16], 
			["baz":3,"qix":3,"foo":4], 
			["baz":3,"qix":2,"bar":2,"foo":1], 
			["baz":3,"qix":2,"bar":1,"foo":8], 
			["baz":3,"qix":2,"foo":15], 
			["baz":3,"qix":1,"bar":3,"foo":5], 
			["baz":3,"qix":1,"bar":2,"foo":12], 
			["baz":3,"qix":1,"bar":1,"foo":19], 
			["baz":3,"qix":1,"foo":26], 
			["baz":3,"bar":5,"foo":2], 
			["baz":3,"bar":4,"foo":9], 
			["baz":3,"bar":3,"foo":16], 
			["baz":3,"bar":2,"foo":23], 
			["baz":3,"bar":1,"foo":30], 
			["baz":3,"foo":37], 
			["baz":2,"qix":5,"foo":3], 
			["baz":2,"qix":4,"bar":2], 
			["baz":2,"qix":4,"bar":1,"foo":7], 
			["baz":2,"qix":4,"foo":14], 
			["baz":2,"qix":3,"bar":3,"foo":4], 
			["baz":2,"qix":3,"bar":2,"foo":11], 
			["baz":2,"qix":3,"bar":1,"foo":18], 
			["baz":2,"qix":3,"foo":25], 
			["baz":2,"qix":2,"bar":5,"foo":1], 
			["baz":2,"qix":2,"bar":4,"foo":8], 
			["baz":2,"qix":2,"bar":3,"foo":15], 
			["baz":2,"qix":2,"bar":2,"foo":22], 
			["baz":2,"qix":2,"bar":1,"foo":29], 
			["baz":2,"qix":2,"foo":36], 
			["baz":2,"qix":1,"bar":6,"foo":5], 
			["baz":2,"qix":1,"bar":5,"foo":12], 
			["baz":2,"qix":1,"bar":4,"foo":19], 
			["baz":2,"qix":1,"bar":3,"foo":26], 
			["baz":2,"qix":1,"bar":2,"foo":33], 
			["baz":2,"qix":1,"bar":1,"foo":40], 
			["baz":2,"qix":1,"foo":47], 
			["baz":2,"bar":8,"foo":2], 
			["baz":2,"bar":7,"foo":9], 
			["baz":2,"bar":6,"foo":16], 
			["baz":2,"bar":5,"foo":23], 
			["baz":2,"bar":4,"foo":30], 
			["baz":2,"bar":3,"foo":37], 
			["baz":2,"bar":2,"foo":44], 
			["baz":2,"bar":1,"foo":51], 
			["baz":2,"foo":58], 
			["baz":1,"qix":7,"foo":2], 
			["baz":1,"qix":6,"bar":1,"foo":6], 
			["baz":1,"qix":6,"foo":13], 
			["baz":1,"qix":5,"bar":3,"foo":3], 
			["baz":1,"qix":5,"bar":2,"foo":10], 
			["baz":1,"qix":5,"bar":1,"foo":17], 
			["baz":1,"qix":5,"foo":24], 
			["baz":1,"qix":4,"bar":5], 
			["baz":1,"qix":4,"bar":4,"foo":7], 
			["baz":1,"qix":4,"bar":3,"foo":14], 
			["baz":1,"qix":4,"bar":2,"foo":21], 
			["baz":1,"qix":4,"bar":1,"foo":28], 
			["baz":1,"qix":4,"foo":35], 
			["baz":1,"qix":3,"bar":6,"foo":4], 
			["baz":1,"qix":3,"bar":5,"foo":11], 
			["baz":1,"qix":3,"bar":4,"foo":18], 
			["baz":1,"qix":3,"bar":3,"foo":25], 
			["baz":1,"qix":3,"bar":2,"foo":32], 
			["baz":1,"qix":3,"bar":1,"foo":39], 
			["baz":1,"qix":3,"foo":46], 
			["baz":1,"qix":2,"bar":8,"foo":1], 
			["baz":1,"qix":2,"bar":7,"foo":8], 
			["baz":1,"qix":2,"bar":6,"foo":15], 
			["baz":1,"qix":2,"bar":5,"foo":22], 
			["baz":1,"qix":2,"bar":4,"foo":29], 
			["baz":1,"qix":2,"bar":3,"foo":36], 
			["baz":1,"qix":2,"bar":2,"foo":43], 
			["baz":1,"qix":2,"bar":1,"foo":50], 
			["baz":1,"qix":2,"foo":57], 
			["baz":1,"qix":1,"bar":9,"foo":5], 
			["baz":1,"qix":1,"bar":8,"foo":12], 
			["baz":1,"qix":1,"bar":7,"foo":19], 
			["baz":1,"qix":1,"bar":6,"foo":26], 
			["baz":1,"qix":1,"bar":5,"foo":33], 
			["baz":1,"qix":1,"bar":4,"foo":40], 
			["baz":1,"qix":1,"bar":3,"foo":47], 
			["baz":1,"qix":1,"bar":2,"foo":54], 
			["baz":1,"qix":1,"bar":1,"foo":61], 
			["baz":1,"qix":1,"foo":68], 
			["baz":1,"bar":11,"foo":2], 
			["baz":1,"bar":10,"foo":9], 
			["baz":1,"bar":9,"foo":16], 
			["baz":1,"bar":8,"foo":23], 
			["baz":1,"bar":7,"foo":30], 
			["baz":1,"bar":6,"foo":37], 
			["baz":1,"bar":5,"foo":44], 
			["baz":1,"bar":4,"foo":51], 
			["baz":1,"bar":3,"foo":58], 
			["baz":1,"bar":2,"foo":65], 
			["baz":1,"bar":1,"foo":72], 
			["baz":1,"foo":79], 
			["qix":9,"foo":1], 
			["qix":8,"bar":1,"foo":5], 
			["qix":8,"foo":12], 
			["qix":7,"bar":3,"foo":2], 
			["qix":7,"bar":2,"foo":9], 
			["qix":7,"bar":1,"foo":16], 
			["qix":7,"foo":23], 
			["qix":6,"bar":4,"foo":6], 
			["qix":6,"bar":3,"foo":13], 
			["qix":6,"bar":2,"foo":20], 
			["qix":6,"bar":1,"foo":27], 
			["qix":6,"foo":34], 
			["qix":5,"bar":6,"foo":3], 
			["qix":5,"bar":5,"foo":10], 
			["qix":5,"bar":4,"foo":17], 
			["qix":5,"bar":3,"foo":24], 
			["qix":5,"bar":2,"foo":31], 
			["qix":5,"bar":1,"foo":38], 
			["qix":5,"foo":45], 
			["qix":4,"bar":8], 
			["qix":4,"bar":7,"foo":7], 
			["qix":4,"bar":6,"foo":14], 
			["qix":4,"bar":5,"foo":21], 
			["qix":4,"bar":4,"foo":28], 
			["qix":4,"bar":3,"foo":35], 
			["qix":4,"bar":2,"foo":42], 
			["qix":4,"bar":1,"foo":49], 
			["qix":4,"foo":56], 
			["qix":3,"bar":9,"foo":4], 
			["qix":3,"bar":8,"foo":11], 
			["qix":3,"bar":7,"foo":18], 
			["qix":3,"bar":6,"foo":25], 
			["qix":3,"bar":5,"foo":32], 
			["qix":3,"bar":4,"foo":39], 
			["qix":3,"bar":3,"foo":46], 
			["qix":3,"bar":2,"foo":53], 
			["qix":3,"bar":1,"foo":60], 
			["qix":3,"foo":67], 
			["qix":2,"bar":11,"foo":1], 
			["qix":2,"bar":10,"foo":8], 
			["qix":2,"bar":9,"foo":15], 
			["qix":2,"bar":8,"foo":22], 
			["qix":2,"bar":7,"foo":29], 
			["qix":2,"bar":6,"foo":36], 
			["qix":2,"bar":5,"foo":43], 
			["qix":2,"bar":4,"foo":50], 
			["qix":2,"bar":3,"foo":57], 
			["qix":2,"bar":2,"foo":64], 
			["qix":2,"bar":1,"foo":71], 
			["qix":2,"foo":78], 
			["qix":1,"bar":12,"foo":5], 
			["qix":1,"bar":11,"foo":12], 
			["qix":1,"bar":10,"foo":19], 
			["qix":1,"bar":9,"foo":26], 
			["qix":1,"bar":8,"foo":33], 
			["qix":1,"bar":7,"foo":40], 
			["qix":1,"bar":6,"foo":47], 
			["qix":1,"bar":5,"foo":54], 
			["qix":1,"bar":4,"foo":61], 
			["qix":1,"bar":3,"foo":68], 
			["qix":1,"bar":2,"foo":75], 
			["qix":1,"bar":1,"foo":82], 
			["qix":1,"foo":89], 
			["bar":14,"foo":2], 
			["bar":13,"foo":9], 
			["bar":12,"foo":16], 
			["bar":11,"foo":23], 
			["bar":10,"foo":30], 
			["bar":9,"foo":37], 
			["bar":8,"foo":44], 
			["bar":7,"foo":51], 
			["bar":6,"foo":58], 
			["bar":5,"foo":65], 
			["bar":4,"foo":72], 
			["bar":3,"foo":79], 
			["bar":2,"foo":86], 
			["bar":1,"foo":93], 
			["foo":100]
			])
	}
}
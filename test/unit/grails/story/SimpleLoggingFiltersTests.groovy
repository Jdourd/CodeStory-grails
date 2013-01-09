package grails.story



import grails.test.mixin.*

@TestFor(HelloController)
@Mock(SimpleLoggingFilters)
class SimpleLoggingFiltersTests {

	void test_should_not_redirect() {
	    withFilters(action:"index") {
	        controller.index()
	    }
	    assert response.redirectedUrl == null
	}
}
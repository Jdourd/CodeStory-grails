package grails.story

class HelloController {

    def index() {
		if(params["q"] == "Quelle est ton adresse email") {
			render(text:'g.dhordain@gmail.com', Server:'grails-story')
		} else {
			render(text:'Hello You !', contentType:'text/plain')
		}
	}
}

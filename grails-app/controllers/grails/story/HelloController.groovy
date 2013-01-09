package grails.story

class HelloController {

    def index() {
		def question = params["q"]
		if(question == "Quelle est ton adresse email") {
			render(text:'g.dhordain@gmail.com', contentType:'text/plain')
		} else if(question == 'Es tu abonne a la mailing list(OUI/NON)') {
			render(text:'OUI', contentType:'text/plain')
		} else if(question == 'Es tu heureux de participer(OUI/NON)'){
			render(text:'OUI', contentType:'text/plain')
		} else {
			render(text:'Hello You !', contentType:'text/plain')
		}
	}
}

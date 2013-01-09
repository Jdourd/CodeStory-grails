package grails.story

class HelloController {

    def index() {
		def question = params["q"]
		if(question == "Quelle est ton adresse email") {
			render(text:'g.dhordain@gmail.com', contentType:'text/plain')
		} else if(['Es tu abonne a la mailing list(OUI/NON)',
				   'Es tu heureux de participer(OUI/NON)',
				   'Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)'].contains(question)){
			render(text:'OUI', contentType:'text/plain')
		} else {
			render(text:'Hello You !', contentType:'text/plain')
		}
	}
}

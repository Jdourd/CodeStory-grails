package grails.story

class HelloController {

    def index() {
		def question = params["q"]
		if(question ==~ /^[0-9 ,.*+-\/()]+$/){
			redirect(controller:"SolveEquation", action:"solve",  params: [q: question])
		} else if(question == "Quelle est ton adresse email") {
			render(text:'g.dhordain@gmail.com', contentType:'text/plain')
		} else if(['Es tu abonne a la mailing list(OUI/NON)',
				   'Es tu heureux de participer(OUI/NON)',
				   'Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)',
				   'As tu bien recu le premier enonce(OUI/NON)',
				   'As tu bien recu le second enonce(OUI/NON)'].contains(question)){
			render(text:'OUI', contentType:'text/plain')
		} else if(['Est ce que tu reponds toujours oui(OUI/NON)', 
				   'As tu copie le code de ndeloof(OUI/NON/JE_SUIS_NICOLAS)'].contains(question)) {
			render(text:'NON', contentType:'text/plain')
		} else if(question == 'As tu passe une bonne nuit malgre les bugs de l etape precedente(PAS_TOP/BOF/QUELS_BUGS)') {
			render(text:'QUELS_BUGS', contentType:'text/plain')
		} else {
			render(text:'Hello You !', contentType:'text/plain')
		}
	}
}

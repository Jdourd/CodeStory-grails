class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		
		"/scalaskel/change/$value"(controller:'ScalaskelChange', action:'json')

		"/**"(controller:'hello')
		"500"(view:'/error')
	}
}

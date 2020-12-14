def call(lastCommitHash) {
		script {
				withCredentials([usernamePassword(credentialsId: 'ccv2Credentials', usernameVariable: 'subscriptionId', passwordVariable: 'token')]) {
					response = sh (script: "curl --location --request GET 'https://portalrotapi.hana.ondemand.com/v2/subscriptions/${subscriptionId}/builds' --header 'Authorization: Bearer ${token}'",returnStdout:true)
				}

				echo "$response"
				buildsObj = readJSON text: "$response"
				buildsObj.value.each{ build ->
						build.properties.each { prop ->
								if(prop.key == 'project.repository.revision'){
										if(prop.value == lastCommitHash){
												buildCode = build.code
												echo "Found build for commit hash $lastCommitHash , build code $buildCode"
												return [ found : true, buildCode : buildCode ]
										}
								}
						}
				}
				return [ found : false, buildCode : null ]
		}
}

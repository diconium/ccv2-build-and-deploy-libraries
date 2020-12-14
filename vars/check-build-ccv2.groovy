def call(code) {
		script {
				while (true) {
					withCredentials([usernamePassword(credentialsId: 'ccv2Credentials', usernameVariable: 'subscriptionId', passwordVariable: 'token')]) {
							response = sh (script: "curl --location --request GET 'https://portalrotapi.hana.ondemand.com/v2/subscriptions/${subscriptionId}/builds/$code' --header 'Authorization: Bearer ${token}'",returnStdout:true)
					}
					echo "$response"
					statusResponse = readJSON text: "$response"

					if("FAIL".equals(statusResponse["status"])) {
						error("Build failed on SAP CCV2")
					}

					if("SUCCESS".equals(statusResponse["status"])) {
						break;
					}
					sh('sleep 150s')
				}
				
				echo "+++++ CCV2 build complete +++++"
		}
}

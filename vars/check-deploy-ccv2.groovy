def call(code) {
		script {
				while (true) {
					withCredentials([usernamePassword(credentialsId: 'ccv2Credentials', usernameVariable: 'subscriptionId', passwordVariable: 'token')]) {
							response = sh (script: "curl --location --request GET 'https://portalrotapi.hana.ondemand.com/v2/subscriptions/${subscriptionId}/deployments/$code' --header 'Authorization: Bearer ${token}'",returnStdout:true)
					}
					echo "$response"
					statusResponse = readJSON text: "$response"

					if("FAIL".equals(statusResponse["status"])) {
						error("Deployment failed on CCV2")
					}

					if("DEPLOYED".equals(statusResponse["status"])) {
						break;
					}
					sh('sleep 150s')
				}

				echo "+++++ CCV2 deployment complete +++++"
		}
}

def call(buildCode, databaseUpdateMode, environmentCode, strategy) {
		echo "+++++ Start deployment on SAP CCV2 environment +++++"

		script{
				withCredentials([usernamePassword(credentialsId: 'ccv2Credentials', usernameVariable: 'subscriptionId', passwordVariable: 'token')]) {
						response = sh (script: "curl --location --request POST 'https://portalrotapi.hana.ondemand.com/v2/subscriptions/${subscriptionId}/deployments' --header 'Content-Type: application/json' --header 'Authorization: Bearer ${token}' --header 'Content-Type: text/plain' --data-raw '{\"buildCode\": \"${buildCode}\",\"databaseUpdateMode\": \"${databaseUpdateMode}\",\"environmentCode\": \"${environmentCode}\",\"strategy\": \"${strategy}\"}'",returnStdout:true)
						echo "$response"
						responseObj = readJSON text: "$response"
						code = responseObj["code"]
						echo "+++++ Deployment created on CCV2! Code = $code"
						return code
				}
		}
}

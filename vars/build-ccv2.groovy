def call(branch, name) {
		echo "+++++ Create build on SAP CCV2 environment +++++"

		script{
				withCredentials([usernamePassword(credentialsId: 'ccv2Credentials', usernameVariable: 'subscriptionId', passwordVariable: 'token')]) {
						response = sh (script: "curl --location --request POST 'https://portalrotapi.hana.ondemand.com/v2/subscriptions/${subscriptionId}/builds' --header 'Content-Type: application/json' --header 'Authorization: Bearer ${token}' --header 'Content-Type: text/plain' --data-raw '{\"branch\": \"${branch}\",\"name\": \"${name}\"}'",returnStdout:true)
						echo "$response"
						responseObj = readJSON text: "$response"
						code = responseObj["code"]
						echo "+++++ Build created on CCV2! Code = $code"
						return code
				}
		}
}

#!/bin/bash
urlID='https://dev.glycam.org/json/'

#echo "~~~First we submit a request that just asks for the cookies from the getToken url."
TOKEN=$( curl -v -c cookies.txt -b cookies.txt "${urlID}"getToken/ )
#echo ${TOKEN} > token.html
#echo "~~~Then we use the TOKEN we just received in a request that contains the json object."

RESPONSE=$( curl -v  \
-c cookies.txt \
-b cookies.txt \
--header "X-CSRFToken: "${TOKEN} \
--header "Content-Type: application/json" \
-d "$( cat ../testData/sequence.json )" \
${urlID})

echo ${RESPONSE} > dev_sequence.json
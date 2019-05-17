# ocp-fuse-healthcare




Command 
oc replace --force  -f \
https://raw.githubusercontent.com/jboss-container-images/jboss-amq-7-broker-openshift-image/72-1.2.GA/amq-broker-7-image-streams.yaml -n openshift

oc replace --force -f \
https://raw.githubusercontent.com/jboss-container-images/jboss-amq-7-broker-openshift-image/72-1.2.GA/amq-broker-7-scaledown-controller-image-streams.yaml -n openshift

oc import-image amq-broker-72-openshift:1.2 -n openshift

oc import-image amq-broker-72-scaledown-controller-openshift:1.0 -n openshift

for template in amq-broker-72-basic.yaml \
amq-broker-72-ssl.yaml \
amq-broker-72-custom.yaml \
amq-broker-72-persistence.yaml \
amq-broker-72-persistence-ssl.yaml \
amq-broker-72-persistence-clustered.yaml \
amq-broker-72-persistence-clustered-ssl.yaml;
 do
 oc replace --force -f \
https://raw.githubusercontent.com/jboss-container-images/jboss-amq-7-broker-openshift-image/72-1.2.GA/templates/${template} -n openshift
done

oc new-project fuse-lab
 
oc new-app --template=amq-broker-72-basic --param=AMQ_USER=admin --param=AMQ_PASSWORD=admin

oc policy add-role-to-user view -z default



--- Deploying applications
oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:latest~https://github.com/robinfoe/ocp-fuse-healthcare \
	--name='inbound' \
	--build-env='MAVEN_ARGS=-P inbound' \
	--build-env='ARTIFACT_DIR=inbound/target' \
	--env='JAVA_OPTS=-Dspring.config.location=/deployments/config/inboundapp.properties'


oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:latest~https://github.com/robinfoe/ocp-fuse-healthcare \
	--name='xlate' \
	--build-env='MAVEN_ARGS=-P xlate' \
	--build-env='ARTIFACT_DIR=xlate/target' \
	--env='JAVA_OPTS=-Dspring.config.location=/deployments/config/xlateapp.properties'


oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:latest~https://github.com/robinfoe/ocp-fuse-healthcare \
	--name='outbound' \
	--build-env='MAVEN_ARGS=-P outbound' \
	--build-env='ARTIFACT_DIR=outbound/target' \
	--env='JAVA_OPTS=-Dspring.config.location=/deployments/config/outboundapp.properties'



oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:latest~https://github.com/robinfoe/ocp-fuse-healthcare \
	--name='nextgate' \
	--build-env='MAVEN_ARGS=-P nextgate' \
	--build-env='ARTIFACT_DIR=nextgate/target' \
	--env='JAVA_OPTS=-Dspring.config.location=/deployments/config/nextgateapp.properties'




--- navigate to ocp-fuse-halthcare/config
run the below command 

oc create configmap inbound-config --from-file=inboundapp.properties
oc create configmap xlate-config --from-file=xlateapp.properties

oc create configmap outbound-config --from-file=outboundapp.properties
oc create configmap nextgate-config --from-file=nextgateapp.properties


oc set volume dc/inbound --add --configmap-name=inbound-config --mount-path=/deployments/config --type=configmap
oc set volume dc/xlate --add --configmap-name=xlate-config --mount-path=/deployments/config --type=configmap

oc set volume dc/outbound --add --configmap-name=outbound-config --mount-path=/deployments/config --type=configmap
oc set volume dc/nextgate --add --configmap-name=nextgate-config --mount-path=/deployments/config --type=configmap



oc expose svc/inbound











 
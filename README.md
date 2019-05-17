# ocp-fuse-healthcare - Build
Clone the repository to your local folder.
Before you start , Please ensure you have AMQ broker 7.2  image available in your cluster.
If you do not have, please run the below command

**Make sure you have login to your openshift cluster**

```shell
---
$ oc replace --force  -f \
https://raw.githubusercontent.com/jboss-container-images/jboss-amq-7-broker-openshift-image/72-1.2.GA/amq-broker-7-image-streams.yaml -n openshift

$ oc replace --force -f \
https://raw.githubusercontent.com/jboss-container-images/jboss-amq-7-broker-openshift-image/72-1.2.GA/amq-broker-7-scaledown-controller-image-streams.yaml -n openshift

$ oc import-image amq-broker-72-openshift:1.2 -n openshift

$ oc import-image amq-broker-72-scaledown-controller-openshift:1.0 -n openshift

$ for template in amq-broker-72-basic.yaml \
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
```

## Deploy healthcare project

- Create new project called fuse-lab

```shell
$ oc new-project fuse-lab
```

- Deploy and Configure AMQ Broker

```shell
$ oc new-app --template=amq-broker-72-basic --param=AMQ_USER=admin --param=AMQ_PASSWORD=admin

$ oc policy add-role-to-user view -z default
```

- Deploy Application with s2i

```shell
$ oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:latest~https://github.com/robinfoe/ocp-fuse-healthcare \
	--name='inbound' \
	--build-env='MAVEN_ARGS=-P inbound' \
	--build-env='ARTIFACT_DIR=inbound/target' \
	--env='JAVA_OPTS=-Dspring.config.location=/deployments/config/inboundapp.properties'

$ oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:latest~https://github.com/robinfoe/ocp-fuse-healthcare \
	--name='xlate' \
	--build-env='MAVEN_ARGS=-P xlate' \
	--build-env='ARTIFACT_DIR=xlate/target' \
	--env='JAVA_OPTS=-Dspring.config.location=/deployments/config/xlateapp.properties'

$ oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:latest~https://github.com/robinfoe/ocp-fuse-healthcare \
	--name='outbound' \
	--build-env='MAVEN_ARGS=-P outbound' \
	--build-env='ARTIFACT_DIR=outbound/target' \
	--env='JAVA_OPTS=-Dspring.config.location=/deployments/config/outboundapp.properties'

$ oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:latest~https://github.com/robinfoe/ocp-fuse-healthcare \
	--name='nextgate' \
	--build-env='MAVEN_ARGS=-P nextgate' \
	--build-env='ARTIFACT_DIR=nextgate/target' \
	--env='JAVA_OPTS=-Dspring.config.location=/deployments/config/nextgateapp.properties'
```

- Configure config map - navigate to ocp-fuse-halthcare/config and run the below command

```shell
$ oc create configmap inbound-config --from-file=inboundapp.properties
$ oc create configmap xlate-config --from-file=xlateapp.properties
$ oc create configmap outbound-config --from-file=outboundapp.properties
$ oc create configmap nextgate-config --from-file=nextgateapp.properties

$ oc set volume dc/inbound --add --configmap-name=inbound-config --mount-path=/deployments/config --type=configmap
$ oc set volume dc/xlate --add --configmap-name=xlate-config --mount-path=/deployments/config --type=configmap
$ oc set volume dc/outbound --add --configmap-name=outbound-config --mount-path=/deployments/config --type=configmap
$ oc set volume dc/nextgate --add --configmap-name=nextgate-config --mount-path=/deployments/config --type=configmap
```

- Expose the service and run sample curl

```shell
 $ oc expose svc/inbound
 
 # curl -X POST \
	-H 'Content-type: application/xml' \
	-d '<Person xmlns="http://www.app.customer.com">
    <age>20</age>
    <birthname>Apache</birthname>
    <legalname>
        <given>Robin</given>
        <family>Foe</family>
    </legalname>
    <fathername>Red Hat</fathername>
    <mothername>tomcat</mothername>
    <gender>
        <code>M</code>
        <displaytext>Male</displaytext>
    </gender>
</Person>
' \
	http://<your openshift app route url>/services/person/match
```
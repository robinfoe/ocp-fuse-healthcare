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


oc new-app redhat-openjdk18-openshift~<git_repo_URL> --context-dir=<context-dir> --build-env='MAVEN_ARGS=-e -Popenshift -Dcom.redhat.xpaas.repo.redhatga package'






broker-amq-tcp

 
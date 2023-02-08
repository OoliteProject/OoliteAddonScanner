#!/bin/bash

ADDONSCANNER_HOME=`dirname $0`
JAVA_OPTS=""

if [ ! -f "configuration.properties" ]
then
	pushd .
	cd "${ADDONSCANNER_HOME}"
	java ${JAVA_OPTS} -jar dist/@projectname@-@pomversion@.jar $@
	RETVAL=$?
	popd
else
	java ${JAVA_OPTS} -jar ${ADDONSCANNER_HOME}/dist/@projectname@-@pomversion@.jar $@
	RETVAL=$?
fi

exit $RETVAL
#!/bin/bash

ADDONSCANNER_HOME=`dirname $0`
JAVA_OPTS=""

pushd .
cd "${ADDONSCANNER_HOME}"
java ${JAVA_OPTS} -jar @projectname@-@pomversion@.jar com.chaudhuri.cataloggenerator.Main $@
RETVAL=$?
popd

exit $RETVAL
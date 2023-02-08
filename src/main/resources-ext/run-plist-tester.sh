#!/bin/bash

ADDONSCANNER_HOME=`dirname $0`
JAVA_OPTS=""

pushd .
cd "${ADDONSCANNER_HOME}"
java ${JAVA_OPTS} -cp @projectname@-@pomversion@.jar com.chaudhuri.plistcheck.PlistTest $@
RETVAL=$?
popd

exit $RETVAL
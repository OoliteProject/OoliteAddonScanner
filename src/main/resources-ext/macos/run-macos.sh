#!/bin/bash -x

echo "current directory:"
pwd

ADDONSCANNER_HOME=`dirname $0`
JAVA_HOME="${ADDONSCANNER_HOME}/../Resources/jvm"
# hints from https://www.oracle.com/technical-resources/articles/javase/javatomac.html
JAVA_OPTS="-Dcom.apple.macos.useScreenMenuBar=true -Dcom.apple.mrj.application.growbox.intrudes=false -Dcom.apple.mrj.application.live-resize=true"

pushd .
cd "${ADDONSCANNER_HOME}"
${JAVA_HOME}/bin/java ${JAVA_OPTS} -jar ${ADDONSCANNER_HOME}/../Resources/dist/@projectname@-@pomversion@.jar $@
RETVAL=$?
popd

exit $RETVAL
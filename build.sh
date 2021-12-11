#!/usr/bin/env bash
set -e

KERNEL=`uname -s`

DIR=`dirname $0`
cd $DIR

. ./build.vars

if [[  $KERNEL == "Linux" ]]; then
  mvn package -DskipTests
  REPO=/home/pi/mvn-repo
  M2=~/.m2
else
  mvn -f pom.cyg.xml package -DskipTests
  REPO=../mvn-repo
  M2=/cygdrive/c/Users/glfra/.m2
fi

mvn install:install-file -Dfile=target/${ID}-${VER}.jar -DgroupId=com.github.glfrazier -DartifactId=${ID} -Dversion=${VER} -Dpackaging=jar -DlocalRepositoryPath=${REPO}
rm -rf ${M2}/repository/com/github/glfrazier/${ID}


#!/bin/sh
APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'
APP_HOME=`dirname "$0"`
APP_HOME=`cd "$APP_HOME" && pwd`

MAX_FD="maximum"
warn() { echo "$*"; }
die() { echo; echo "$*"; echo; exit 1; }

if [ "$1" = "--stop" ]; then exit 0; fi

# OS specific
cygwin=false
msys=false
darwin=false
nonstop=false
case "`uname`" in
  CYGWIN* ) cygwin=true ;;
  Darwin* ) darwin=true ;;
  MSYS* | MINGW* ) msys=true ;;
  NONSTOP* ) nonstop=true ;;
esac

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# Java command
if [ -n "$JAVA_HOME" ]; then
  if [ -x "$JAVA_HOME/jre/sh/java" ]; then
    JAVACMD="$JAVA_HOME/jre/sh/java"
  else
    JAVACMD="$JAVA_HOME/bin/java"
  fi
  if [ ! -x "$JAVACMD" ]; then
    die "JAVA_HOME is set but java is not found at $JAVACMD"
  fi
else
  JAVACMD="java"
  which java > /dev/null 2>&1 || die "java not found in PATH"
fi

# JVM options
if [ "$cygwin" = "true" -o "$msys" = "true" ]; then
  APP_HOME=`cygpath --path --mixed "$APP_HOME"`
  CLASSPATH=`cygpath --path --mixed "$CLASSPATH"`
fi

exec "$JAVACMD" \
  $DEFAULT_JVM_OPTS \
  $JAVA_OPTS \
  $GRADLE_OPTS \
  "-Dorg.gradle.appname=$APP_BASE_NAME" \
  -classpath "$CLASSPATH" \
  org.gradle.wrapper.GradleWrapperMain \
  "$@"

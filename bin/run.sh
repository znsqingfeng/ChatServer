#!/usr/bin/env bash
CLASSPATH=../classes/chat
for jar in ../lib/*.jar
do
CLASSPATH=$CLASSPATH:$jar
done
echo "$CLASSPATH"
java -classpath $CLASSPATH chat.server.Server&
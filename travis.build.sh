#!/bin/sh

wget https://dl.dropboxusercontent.com/u/55284851/MineBuilder/MBServerApi-beta.zip
unzip -j "MBServerApi-beta.zip" "MBServerApi.jar" -d "MBServerApi.jar"
find src/ 2>&1 | grep .java > sources.txt
javac -classpath MBServerApi.jar -sourcepath src @sources.txt
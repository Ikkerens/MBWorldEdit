#!/bin/sh

wget https://dl.dropboxusercontent.com/u/55284851/MineBuilder/MBServerApi-beta.zip
unzip -j MBServerApi-beta.zip MBServerAPI.jar -d MBServerApi.jar
find src/ 2>&1 | grep .java > sources.txt
mkdir classes
javac -cp ./MBServerAPI.jar:. -d classes -sourcepath src @sources.txt
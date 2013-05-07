#!/bin/sh

wget https://dl.dropboxusercontent.com/u/55284851/MineBuilder/MBServerApi-beta.zip
unzip MBServerApi-beta.zip 2>&1 >/dev/null
find src/ 2>&1 | grep .java > sources.txt
javac -cp ./MBServerApi.jar:. -sourcepath src @sources.txt
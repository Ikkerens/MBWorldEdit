#!/bin/sh

wget https://dl.dropboxusercontent.com/u/55284851/MineBuilder/MBServerApi-beta.zip
unzip MBServerApi-beta.zip
javac -classpath MBServerApi.jar -sourcepath src
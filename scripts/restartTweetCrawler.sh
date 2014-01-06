#!/bin/bash

NOW=$(date +"%Y-%m-%d")
LOGFILE="logs/log-$NOW.log"
appName="tweetCrawler"
PID=$(ps aux | grep "${appName}" | grep -v grep | awk '{print $2}') 

if [ -n "$PID" ]; then
	echo "Will try to kill ${appName} with pid : $PID " >> $LOGFILE

	kill -0 $PID
	if [ $? -eq 1 ]; then
	  echo "no permission to signal $PID, script terminated" >> $LOGFILE
	  exit 1
	else 
		echo "trying to kill gracefully" >> $LOGFILE
		kill -0 $PID && kill -s 15 $PID && sleep 3
		kill -0 $PID && sleep 30
	fi
	killall -9 $appName
else
	echo "No process by name ${appName} found " >> $LOGFILE
fi

echo "Starting new jvm instance " >> $LOGFILE
nohup java -jar $appName.jar 1 >> /dev/null &


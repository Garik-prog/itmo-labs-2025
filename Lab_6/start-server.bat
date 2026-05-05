@echo off
cd /d "%~dp0"
java -Dfile.encoding=UTF-8 -cp "target\lab6-1.0-SNAPSHOT.jar;target\lib\*" server.Server data.xml
pause
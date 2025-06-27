@echo off
set TOMCAT_DIR=C:\Tomcat10

echo [1] Остановка Tomcat...
%TOMCAT_DIR%\bin\shutdown.bat

echo [2] Очистка старого .war и папки приложения...
del %TOMCAT_DIR%\webapps\BankApp-1.0-SNAPSHOT.war
rmdir /S /Q %TOMCAT_DIR%\webapps\BankApp-1.0-SNAPSHOT

echo [3] Сборка проекта...
mvn clean package

echo [4] Копирование нового .war...
copy target\BankApp-1.0-SNAPSHOT.war %TOMCAT_DIR%\webapps\

echo [5] Запуск Tomcat...
%TOMCAT_DIR%\bin\startup.bat

pause

@echo off
echo ----------------------------
echo Installing Fishbans API to Maven Repo
echo ----------------------------
set M2_HOME=C:\Program Files (x86)\Apache\Maven
set JAVA_HOME=C:\Program Files\Java\jdk
cd target
mkdir temp
echo %MAVEN%
cp FishbansAPI-v1.0.jar temp/FishbansAPI-v1.0.jar
cmd /c mvn install:install-file -Dfile="temp/FishbansAPI-v1.0.jar" -DgroupId=net.ae97 -DartifactId=FishBansAPI -Dversion=1.0 -Dpackaging=jar -DcreateChecksum=true
cp FishbansAPI-v1.0-bare.jar temp/FishbansAPI-v1.0.jar
cmd /c mvn install:install-file -Dfile="temp/FishbansAPI-v1.0.jar" -DgroupId=net.ae97 -DartifactId=FishBansAPI -Dversion=1.0 -Dpackaging=jar -Dclassifier=bare -DcreateChecksum=true
cp FishbansAPI-v1.0-forge.jar temp/FishbansAPI-v1.0.jar
cmd /c mvn install:install-file -Dfile="temp/FishbansAPI-v1.0.jar" -DgroupId=net.ae97 -DartifactId=FishBansAPI -Dversion=1.0 -Dpackaging=jar -Dclassifier=forge -DcreateChecksum=true
cp FishbansAPI-v1.0-bukkit.jar temp/FishbansAPI-v1.0.jar
cmd /c mvn install:install-file -Dfile="temp/FishbansAPI-v1.0.jar" -DgroupId=net.ae97 -DartifactId=FishBansAPI -Dversion=1.0 -Dpackaging=jar -Dclassifier=bukkit -DcreateChecksum=true
cp FishbansAPI-v1.0-include-libs.jar temp/FishbansAPI-v1.0.jar
cmd /c mvn install:install-file -Dfile="temp/FishbansAPI-v1.0.jar" -DgroupId=net.ae97 -DartifactId=FishBansAPI -Dversion=1.0 -Dpackaging=jar -Dclassifier=include-libs -DcreateChecksum=true
rm -rf temp
cd ..
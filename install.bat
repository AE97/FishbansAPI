@echo off
echo ----------------------------
echo Installing Fishbans API to Maven Repo
echo ----------------------------
set M2_HOME=C:\Program Files (x86)\Apache\Maven
set JAVA_HOME=C:\Program Files\Java\jdk
set VERSION=1.1-DEV
cd target
mkdir temp
echo %MAVEN%
cp FishbansAPI-v%VERSION%.jar temp/FishbansAPI-v%VERSION%.jar
cmd /c mvn install:install-file -Dfile="temp/FishbansAPI-%VERSION%.jar" -DgroupId=net.ae97 -DartifactId=FishBansAPI -Dversion=%VERSION% -Dpackaging=jar -DcreateChecksum=true
cp FishbansAPI-v%VERSION%-bare.jar temp/FishbansAPI-v%VERSION%.jar
cmd /c mvn install:install-file -Dfile="temp/FishbansAPI-v%VERSION%.jar" -DgroupId=net.ae97 -DartifactId=FishBansAPI -Dversion=%VERSION% -Dpackaging=jar -Dclassifier=bare -DcreateChecksum=true
cp FishbansAPI-v%VERSION%-forge.jar temp/FishbansAPI-v%VERSION%.jar
cmd /c mvn install:install-file -Dfile="temp/FishbansAPI-v%VERSION%.jar" -DgroupId=net.ae97 -DartifactId=FishBansAPI -Dversion=%VERSION% -Dpackaging=jar -Dclassifier=forge -DcreateChecksum=true
cp FishbansAPI-v%VERSION%-bukkit.jar temp/FishbansAPI-v%VERSION%.jar
cmd /c mvn install:install-file -Dfile="temp/FishbansAPI-v%VERSION%.jar" -DgroupId=net.ae97 -DartifactId=FishBansAPI -Dversion=%VERSION% -Dpackaging=jar -Dclassifier=bukkit -DcreateChecksum=true
cp FishbansAPI-v%VERSION%-include-libs.jar temp/FishbansAPI-v%VERSION%.jar
cmd /c mvn install:install-file -Dfile="temp/FishbansAPI-v%VERSION%.jar" -DgroupId=net.ae97 -DartifactId=FishBansAPI -Dversion=%VERSION% -Dpackaging=jar -Dclassifier=include-libs -DcreateChecksum=true
rm -rf temp
cd ..
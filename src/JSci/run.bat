echo off
cd classes.dir

rem appletviewer ..\SOCRCurveFitter.html

java -jar ..\SOCRCurveFitter.jar

rem ******************************************
rem java -classpath .;..\;..\..\..\classes\;..\..\..\classes\JSci\awt\;..\..\..\classes\xtra.jar;..\..\..\classes\core.jar;..\..\..\classes\sci.jar;..\..\..\classes\wavelet.jar;..\..\..\classes\JSciBeans.jar;.  -ms128m -mx256m CurveFitter

rem java -classpath .;.\xtra.jar;.\core.jar;.\sci.jar;.\wavelet.jar -ms128m -mx256m CurveFitter

rem java -classpath .;.\core.jar;.\sci.jar;.\wavelet.jar -ms128m -mx256m JSci.awt.CurveFitter
rem *******************************************

cd ..
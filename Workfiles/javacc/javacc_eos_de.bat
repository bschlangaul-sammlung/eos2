@echo off
java -Xmx4096M -Xms4096M -classpath "javacc.jar" javacc eos_de.jj
pause
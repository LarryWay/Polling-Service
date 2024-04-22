BIN = bin
EXECUTABLE = Main



exec: compile run
	@echo --------------------------PROGRAM FINISHED--------------------------

compile: src/BasicParser.java src/LoginRequestContext.java
	@javac -cp lib/HttpWebServer.jar:lib/csv.jar  src/${EXECUTABLE}.java src/*.java -d bin

run:
	@java -cp lib/HttpWebServer.jar:lib/csv.jar:. -cp bin ${EXECUTABLE}

clean:
	rm bin/*.class

cleanjunk:
	rm bin/*.class
	rm src/data/runtime/*.txt
	rm src/html/unique_polls/*


factoryreset: cleanjunk
	rm src/data/polls/*.csv
	



rejar:
	jar cf -cp lib/HttpWebServer.jar -cp lib/Http-WebServer/*.java
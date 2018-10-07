.PHONY: build clean run

build: tema1

run:
	java -Xmx1G	 IO ${ARGS}
	
tema1:
	javac *.java
	jar cfe tema1.jar IO IO.class

clean:
	rm -rf *.class
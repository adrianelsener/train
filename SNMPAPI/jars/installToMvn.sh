#!/bin/bash
~/tools/mvn/mvn-latest/bin/mvn install:install-file -DgroupId=ch.adrianelsener.adventnet -DartifactId=logging -Dversion=SNAPSHOT-1.0.0 -Dfile=AdventNetLogging.jar -Dpackaging=jar -DgeneratePom=true
~/tools/mvn/mvn-latest/bin/mvn install:install-file -DgroupId=ch.adrianelsener.adventnet -DartifactId=snmp -Dversion=SNAPSHOT-1.0.0 -Dfile=AdventNetSnmp.jar -Dpackaging=jar -DgeneratePom=true
~/tools/mvn/mvn-latest/bin/mvn install:install-file -DgroupId=ch.adrianelsener.adventnet -DartifactId=crimson -Dversion=SNAPSHOT-1.0.0 -Dfile=crimson.jar -Dpackaging=jar -DgeneratePom=true
~/tools/mvn/mvn-latest/bin/mvn install:install-file -DgroupId=ch.adrianelsener.adventnet -DartifactId=jaxp -Dversion=SNAPSHOT-1.0.0 -Dfile=jaxp.jar -Dpackaging=jar -DgeneratePom=true


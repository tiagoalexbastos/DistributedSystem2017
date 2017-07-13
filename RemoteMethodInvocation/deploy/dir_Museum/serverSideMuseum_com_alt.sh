#!/usr/bin/env bash
#!/usr/bin/env bash
java -cp .:\* -Djava.rmi.server.codebase="file://$(pwd)/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.museum.MuseumServer $1 $2
#!/usr/bin/env bash

# TODO check if necessary
baseDirectory="$NEO4J_HOME/data/databases/"

kill -9 `ps aux | grep neo4j | grep -v grep | grep /usr/bin/java | awk 'NR==1{print $2}'` # kill eny existing neo4j instances

if [ $# -eq 0 ]
  then
    echo "Usage: $0 databaseNameToUse"
    exit 1
fi

databaseDirectory=$1
symlinkName="graph.db"

if [ -d "$baseDirectory$databaseDirectory" ]; then # database exists
  if [ -d "$baseDirectory$symlinkName" ]; then # clear previous symlink
    unlink $baseDirectory$symlinkName
  fi

  ln -s $baseDirectory$databaseDirectory $baseDirectory$symlinkName # symlink to use given database
  echo "Neo4j database in use: $databaseDirectory"
else
  echo "No database exists at $baseDirectory$databaseDirectory"
  exit 1
fi

nohup $NEO4J_HOME/bin/neo4j console > neo4j-start.out 2> neo4j-start.err < /dev/null &
#!/usr/bin/env bash
set -o xtrace

db_name="new_empty.db"

if [ $# -eq 0 ]
  then
    echo "Usage: $0 neo4jHomeFolder [db_name] [remove_prev(1 if yes)]"
    exit 1
fi

if [ $# -gt 1 ]
  then
  db_name=$2
fi

neo4jHomeFolder=$1

neo4j_command=${neo4jHomeFolder}"/bin/neo4j"
neo4j_stop=`${neo4j_command} stop`

databaseDirectory=${neo4jHomeFolder}"/data/databases/"
symlinkName="graph.db"
empty_base_directory=${databaseDirectory}empty_base.db

if [ -d "$neo4jHomeFolder" ]; then # database exists
  if [ -d "$databaseDirectory$symlinkName" ]; then # clear previous symlink
    original=`readlink  ${databaseDirectory}${symlinkName}`
    unlink ${databaseDirectory}${symlinkName}

    if [ $# -eq 3 ] && [ $3 -eq 1 ]; then
      echo "Removing " ${original}
      rm -rf ${original}
    else
      echo "Not removing" ${original}
    fi

  fi

  cp -r ${empty_base_directory} ${databaseDirectory}${db_name}

  ln -s ${databaseDirectory}${db_name} ${databaseDirectory}${symlinkName} # symlink to use given database
  echo "Cleared neo4j DB - new name = "${db_name}
else
  echo "No neo4j basic folder exists at $neo4jHomeFolder"
  exit 1
fi

neo4j_start=`${neo4j_command} start`
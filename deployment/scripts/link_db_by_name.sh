unlink graph.db
curr_dir=`pwd`
echo $curr_dir

if [ $# -eq 0 ]
  then
    echo "Usage: $0 [db_name] "
    exit 1
fi
target=$curr_dir/$1

echo $target
if [ ! -d "$target" ]; then
    cp -r $curr_dir/empty_base.db $target
fi

`ln -s $target graph.db`
mkdir -p $(dirname $1)
echo -e "1,2,3\n4,5,6\n7,8,9" > $1
echo "Create input file at $1"
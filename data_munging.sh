#!/bin/bash
wurl="http://codekata.com/data/04/weather.dat"
furl="http://codekata.com/data/04/football.dat"

puke() { echo "$@"; exit 1; }

columnCheck(){
  check="$1"; shift
  sed -n 3p |
    awk "$check" | grep -qs BOO && puke 'Fields not correct'
}

headerCheck(){
 columnName="$1"; shift
 sed 1q | grep -qs $columnName || puke "No ${columnName} Header"
}

cat<<EOD | headerCheck moo
header line moo boo hoo
data fooo foo foo foo
EOD

minimumSort(){ sort -n -k2,2 | sed 1q; }
cat<<EOF | minimumSort | grep -qs boo || puke "Minimum Sort is not boo"
foo 10
boo 3
goo 5
EOF

###### tests above

weather() {
  url="$1"
  curl -s $url > /tmp/dat
  cat /tmp/dat | headerCheck HDDay

  cat /tmp/dat | columnCheck '{if ( NF < 3 ) {print "BOO"}}'

  cat /tmp/dat |
    sed -n '3,$p' |
    sed -n '/^ *mo/!p' |
    sed 's/\*//g' |
    awk '{printf("%s %d \n", $1, $2 - $3)}' |
    minimumSort
  test -e /tmp/dat && rm /tmp/dat
}

weather $wurl

football() {
  url="$1"
  curl -s $url > /tmp/dat

  cat /tmp/dat | headerCheck Team
  cat /tmp/dat | columnCheck '{if ( NF != 10 ) {print "BOO"}}'

  cat /tmp/dat |
    sed -n '2,$p' |
    sed -n '/----/!p' |
    awk '{printf("%s %d\n", $2, $7 - $9)}' |
    sed 's/-//' |
    minimumSort

  test -e /tmp/dat && rm /tmp/dat
}

football $furl

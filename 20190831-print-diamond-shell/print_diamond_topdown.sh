#!/bin/bash
# given a letter, return a diamond of the letters, e.g. 'C' ->
#     A
#    B B
#   C   C
#    B B
#     A
#

letter="$1"; shift

letters="ABCDEFGHIJKLMNOPQRSTUVWXYZ"
tmp=/tmp/wtf


puke() { echo "$@"; exit 1; }

subletters() {
  char="$1"; shift
  echo $letters | sed -e 's/\(.*'$char'\).*/\1/'
}

[[ `subletters C` = 'ABC' ]] || puke "subletters did not work"

pad() {
  char="$1"; shift
  fill="$1"; shift
  subletters $char | sed 's/^.//' | sed 's/\(.*\)/\1\1/' |
    sed -e 's/^\(.*\).$/\1/' -e "s/./$fill/g"
}

[[ "`pad B X`" = "X" ]] || puke "pad B failed"


layer() {
  char="$1"; shift
  if [[ $char = "A" ]]; then
    echo $char
  else
    pad=` pad $char "_"`
    echo "${char}${pad}${char}"
  fi

}

chars() { 
  # return 'A'..$char
  char="$1"; shift
  subletters $char | sed 's/\(.\)/\1 /g'
}

[[ "`chars C`" = "A B C " ]] || puke "chars did not work"


linepad() {
  char="$1"; shift
  chars="$*"

  lpad=""
  for c in $chars; do
    lpad="${lpad} "
  done

  for c in $chars; do
    lpad=`echo "$lpad" | sed 's/^.//'`
    if [[ $c = $char ]]; then
      echo "$lpad"
      return
    fi
  done
}

[[ `linepad A A B C ` = "  " ]] || puke "linepad failed"

top() {
  ch="$1"; shift
  list=`chars $ch`
  for c in $list; do
    printf "%s%s\n" "`linepad $c $list`" "`layer $c`"
  done
}

diamond() {
  # make the fill character underscore, then replace at end
  char="$1"; shift
  top $char > $tmp
  (cat $tmp;
  cat $tmp | tail -r | sed 1d ) | sed 's/_/ /g'
}


diamond $letter

filled() {
  char="$1"; shift
  top $char > $tmp
  (cat $tmp;
  cat $tmp | tail -r | sed 1d ) | sed 's/ /-/g'| while read line; do
    first=`echo $line | sed 's/\(.\).*/\1/' `
    mid=`echo "$line" | sed "s/_/$first/g"`
    echo "$mid"
  done | sed 's/-/ /g'
}

filled $letter

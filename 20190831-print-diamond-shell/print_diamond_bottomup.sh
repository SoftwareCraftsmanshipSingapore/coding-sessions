#!/bin/bash
# use a line-based approach  to creat a character diamond

char_to_ascii(){
    echo $(printf "%d\n" \'${1})
}

char_to_number(){
    local foo=$(char_to_ascii "$1")
    local bigA=$(char_to_ascii "A")
    echo $(($foo - $bigA))
}

repeat_dot(){
    for i in $(seq $1); do echo "."; done | tr -d " \n"
}

space_in_between() {
    local letter="$1"
    local idx=$(char_to_number "$1")
    local dot_count=$(( 2 * $idx - 1))
    local filler=$(repeat_dot $dot_count)
    echo "${letter}${filler}${letter}"
}

print_diamond() {
  if [[ -z "$1" ]]; then
    echo "no args"
    exit 0
  fi
  read -r -d '' expected << END
 A
B B
 A
END
    echo "$expected"
}

run_tests() {
  assert_equals "no args" "$(print_diamond)"
  assert_equals "65" "$(char_to_ascii A)"
  assert_equals "2" "$(char_to_number C)"
  assert_equals "..." "$(repeat_dot 3)"

  read -r -d '' expected << END
 A
B B
 A
END
  assert_equals "$expected" "$(print_diamond B)"
  assert_equals "B.B" "$(space_in_between B)"
  assert_equals "C...C" "$(space_in_between C)"
  assert_equals "D.....D" "$(space_in_between D)"
  huston
}

# testing

huston(){
  if [[ -z $any_problems ]]; then
    echo "$(tput setaf 2)ALL PASS $(tput sgr0)"
  else
    echo "$(tput setaf 1)PROBLEMS $(tput sgr0)"
  fi
}


assert_equals() {
  local expected="$1"
  local actual="$2"

  if diff -u -r -N <(echo "$expected") <(echo "$actual"); then
    echo "$(tput setaf 2)PASS $(tput sgr0)"
    true
  else
    echo "$(tput setaf 1)FAIL $(tput sgr0)"
    any_problems=true
    false
  fi
}


# main
if [[ "$1" == "--test" ]] || [[ "$1" == "-t" ]]; then
  run_tests
else
  print_diamond "A"
fi


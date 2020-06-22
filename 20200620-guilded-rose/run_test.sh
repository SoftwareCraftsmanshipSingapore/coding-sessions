#!/bin/bash

# python3 texttest_fixture.py 16 > baseline.txt

diff <(python3 texttest_fixture.py 16) baseline.txt && echo OK

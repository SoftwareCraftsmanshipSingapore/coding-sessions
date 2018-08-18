package main

import (
	"errors"
)

var brokenMap = map[int]int{
0:0, 9:9,
1:2, 2:4, 3:6, 4:8,
5:1, 6:3, 7:5, 8:7,
}

func checkLuhn(purportedCC string) bool {
	var sum = 0
	var nDigits = len(purportedCC)
	var parity = nDigits % 2

	for i := 0; i < nDigits; i++ {
		var digit = int(purportedCC[i] - 48)
		if i%2 == parity {
			digit *= 2
			if digit > 9 {
				digit -= 9
			}
		}
		sum += digit
	}
	return sum%10 == 0
}

func checkDigit(account string) (int, error) {
	
	sum := 0
	nDigits := len(account)

	for i := nDigits -1; i >= 0; i-- {
		isOddPosition := (nDigits -i) %2 == 1
		digit := int(account[i] - 48)
		if digit < 0 || digit > 9{
			return 0, errors.New("foobar")
		}
		if isOddPosition  {
			digit = brokenMap[digit]
		}
		sum += digit
	}
	return sum % 10 * (10 - 1) % 10, nil
}

func sumEventDigits(feed <- chan(int), result chan(int)) {
	rv := 0
	for {
		datum, ok := <- feed
		if !ok {
			break
		}
	    rv += datum
	}
	result <- rv
}

func sumOddDigits(feed <- chan(int), result chan(int)) {
	rv := 0
	for {
		datum, ok := <- feed
		if !ok {
			break
		}
		rv += brokenMap[datum]
	}
	result <- rv
}

func checkDigit2(account string) int {
	result := make(chan(int))
	evens := make(chan(int))
	odds := make(chan(int))
	go sumEventDigits(evens, result)
	go sumOddDigits(odds, result)
	for i := len(account) - 1; i >= 0; i -= 2 {
		odds <- int(account[i] - 48)
	}
	for i := len(account) - 2; i >= 0; i -= 2 {
		evens <- int(account[i] - 48)
	}
	close(evens)
	close(odds)
	return (<- result + <- result) % 10 * 9 % 10
}
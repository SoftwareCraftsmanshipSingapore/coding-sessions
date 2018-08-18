package main


import "testing"

func TestFoo(t *testing.T) {
	if 1 != 1 {
		t.Errorf("error")
	}
}

func TestCheckLuhn(t *testing.T) {

	if !checkLuhn("000000000") {
		t.Errorf("error")
	}
}

func TestCheckLuhnCreditCard(t *testing.T) {

	if checkLuhn("4242424242424242") != true {
		t.Errorf("error")
	}
}

func TestCheckRandomAccount(t *testing.T) {

	if !checkLuhn("79927398713") {
		t.Errorf("error")
	}
}

func TestCheckDigit(t *testing.T) {
	check, _ := checkDigit("00")

	if check != 0  {
		t.Errorf("expected 0 got %v", check)

	}
}

func TestCheckDigit9(t *testing.T) {
	check, _ := checkDigit("01")

	if check != 8  {
		t.Errorf("expected 8 got %v", check)

	}
}

func TestCheckDigit79927398710(t *testing.T) {
	check, _ := checkDigit("7992739871")

	if check != 3  {
		t.Errorf("expected 3 got %v", check)

	}
}

func TestCheckDigit12(t *testing.T) {
	if checkLuhn("125") != true {
		t.Error("I duno what the hell!")
	}
	check, _ := checkDigit("12")

	if check != 5  {
		t.Errorf("expected '12' -> 5 got %v", check)

	}
}

func TestCheckDigit1(t *testing.T) {
	if checkLuhn("18") != true {
		t.Error("I duno what the hell!")
	}
	check, _ := checkDigit("1")

	if check != 8  {
		t.Errorf("expected '1' -> 8 got %v", check)

	}
}

func TestCheckDigit10(t *testing.T) {
	if checkLuhn("109") != true {
		t.Error("I duno what the hell!")
	}
	check, _ := checkDigit("10")

	if check != 9  {
		t.Errorf("expected '10' -> 9 got %v", check)
	}
}

func TestCheckDigit2_10(t *testing.T) {
	if checkLuhn("109") != true {
		t.Error("I duno what the hell!")
	}
	check := checkDigit2("10")

	if check != 9  {
		t.Errorf("expected '10' -> 9 got %v", check)
	}
}




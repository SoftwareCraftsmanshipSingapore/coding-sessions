import { Given, When, Then } from 'cucumber'
import { expect } from 'chai'
import { Account, Calendar } from '../../../src/account'

let calendar = new Calendar()
let account = new Account(calendar)

Given('a client makes a deposit of {int} on {string}', function (amount, date) {
  calendar.today = date
  account.deposit(amount)
})

Given('a deposit of {int} on {string}', function (amount, date) {
  calendar.today = date
  account.deposit(amount)
})

Given('a withdrawal of {int} on {string}', function (amount, date) {
  calendar.today = date
  account.withdraw(amount)
})

When('they print their bank statement', function () {
  account.printStatement()
});

Then('they would see:', function (dataTable) {
  let expected = dataTable.hashes()
  expect(account.statement).to.eql(expected)
})

import { Given, When, Then } from 'cucumber'
import { expect } from 'chai'
import { Account } from '../../../src/account'

let account = new Account()

Given('a client makes a deposit of {int} on {string}', function (amount, date) {
  expect(1).to.eq(1)
})

Given('a deposit of {int} on {string}', function (amount, date) {
  expect(1).to.eq(1)
})

Given('a withdrawal of {int} on {string}', function (amount, date) {
  expect(1).to.eq(1)
})

When('they print their bank statement', function () {
  expect(1).to.eq(1)
});

Then('they would see:', function (dataTable) {
  expect(1).to.eq(1)
})

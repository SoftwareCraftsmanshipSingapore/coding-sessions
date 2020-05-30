export class Account {

  constructor(calendar) {
    this.calendar = calendar
    this.balance = 0
    this.transactions = []
    this.statement = []
  }

  deposit(amount) {
    this.balance += amount
    const date = this.calendar.today
    const balance = this.balance
    const transaction = {amount, balance, date}
    this.transactions.push(transaction)
  }

  withdraw(amount) {
    this.deposit(-amount)
  }

  printStatement() {
    this.statement = this.transactions.map( t => (
      { amount: `${t.amount}`,
        balance: `${t.balance}`,
        date: t.date } )).reverse()
  }

}

export class Calendar {
  today = undefined
}
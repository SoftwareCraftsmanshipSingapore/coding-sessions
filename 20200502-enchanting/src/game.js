function pickRandom(maxN) {
  return Math.floor(Math.random() * maxN)
}

export class Weapon {
  constructor(name){
    this.name = name
    this.prefix = ""
  }

  enchant(enchantment){
      this.prefix = enchantment.prefix
  }

  remove_enchantment(){
    this.prefix = ""
  }

  stats() {
    return `${this.prefix} ${this.name}`.trim()
  }
}

export class MagicBook {
  constructor(selectFn){
    this.selectFn = selectFn
    this.enchantments = [
      {prefix: "Inferno"},
      {prefix: "Icy"}
    ]
  }

  giveOne() {
    let maxN = this.enchantments.length
    return this.enchantments[this.selectFn(maxN)]
  }
}

export class Durance {

  constructor(magic, addOrRemove) {
    this.weapon = new Weapon("Dagger of the Nooblet")
    this.magic = magic
    this.addOrRemove = addOrRemove
  }

  enchant() {
    if (this.addOrRemove() === true) {
      let enchantment = this.magic.giveOne()
      this.weapon.enchant(enchantment)
    } else {
      this.weapon.remove_enchantment()
    }
  }

  describeWeapon() { return this.weapon.stats() }
}
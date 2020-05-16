function pickRandom(maxN) {
  return Math.floor(Math.random() * maxN)
}

export class Weapon {
  constructor(weaponSpec){
    this.name = weaponSpec.name
    this.attrs = weaponSpec.attrs

    this.prefix = ""
    this.extraAttrs = []
  }

  enchant(enchantment){
    this.prefix = enchantment.prefix
    this.extraAttrs = [enchantment.extraAttr]
  }

  remove_enchantment(){
    this.prefix = ""
    this.extraAttrs = []
  }
  
  current_enchantment(){
    return this.prefix
  }

  stats() {
    let attrs = this.attrs.concat(this.extraAttrs)
    return `${this.prefix} ${this.name}`.trim() + "\n" + attrs.map(a => " " + a).join("\n")
  }
}

export class MagicBook {

  constructor(enchantments, selectFn, removeFn){
    this.selectFn = selectFn
    this.removeFn = removeFn
    this.enchantments = enchantments
  }

  possibleEnchantments(weapon){
    return this.enchantments.filter(e => e.prefix !== weapon.current_enchantment())
  }

  giveOne(weapon) {
    let enchantments = this.possibleEnchantments(weapon)
    let pick = this.selectFn(enchantments.map(e => e.prefix))
    return enchantments.find(e => e.prefix === pick)
  }

  enchant(weapon) {
    if ( this.removeFn() ){
      weapon.remove_enchantment()
    } else {
      weapon.enchant(this.giveOne(weapon))
    }
  }
}

export class Durance {

  constructor(weaponSpec, magicBook) {
    this.weapon = new Weapon(weaponSpec)
    this.magicBook = magicBook
  }

  enchant() {
    this.magicBook.enchant(this.weapon)
  }

  describeWeapon() { return this.weapon.stats() }
}
export class Attribute {
  constructor(value, description) {
    this.value = value
    this.description = description
  }
  get asString() {
    return `${this.value} ${this.description}`
  }

  get dpsValue() {
    if (this.value.includes("-")) {
      let [minD, maxD] = this.value.split(" - ").map(e => parseFloat(e))
      return (minD + maxD) / 2
    } else {
      return parseFloat(this.value)
    }
  }
}
export class Enchantment {
  constructor(prefix, attributes) {
    this.prefix = prefix
    this.attribute = attributes
  }
  get floatValue() {
    return this.attribute.dpsValue
  }
}
const emptyEnchantment = new Enchantment("", [])
export class Weapon {
  constructor(weaponSpec){
    this.name = weaponSpec.name
    this.attrs = weaponSpec.attrs
    this.enchantment = emptyEnchantment
  }

  enchant = (enchantment) => this.enchantment = enchantment

  remove_enchantment = () => this.enchantment = emptyEnchantment

  stats() {
    let attrs = this.attrs.concat(this.enchantment.attribute)
    return `${this.enchantment.prefix} ${this.name}`.trim() + "\n" + attrs.map(a => " " + a.asString).join("\n")
  }

  dps() {
    let averageDamage = this.attrs[0].dpsValue
    let speed = this.attrs[1].dpsValue
    if (this.enchantment.prefix !== "") {
      speed = (speed + this.enchantment.floatValue) / 10
    }
    return averageDamage / speed
  }
}

export class MagicBook {
  constructor(selectFn, removeFn){
    this.selectFn = selectFn
    this.removeFn = removeFn
    this.enchantments = [
      new Enchantment("Inferno", [new Attribute("+5", "fire damage")]),
      new Enchantment("Icy"    , [new Attribute("+5", "ice damage")]),
      new Enchantment("Foo"    , [new Attribute("+5", "bar")])
    ]
  }

  enchant(weapon) {
    if ( this.removeFn() ){
      weapon.remove_enchantment()
    } else {
      weapon.enchant(this._selectOneExcept(weapon.enchantment))
    }
  }

  _selectOneExcept(currentEnchantment) {
    let enchantments = this.enchantments.filter(e => e.prefix !== currentEnchantment.prefix)
    let pick = this.selectFn(enchantments.map(e => e.prefix))
    let pickedEnchantment = enchantments.find(e => e.prefix === pick)
    return (pickedEnchantment === undefined) ? currentEnchantment : pickedEnchantment
  }
}

export class Durance {
  constructor(weaponSpec, magicBook) {
    this.weapon = new Weapon(weaponSpec)
    this.magicBook = magicBook
  }

  enchant = () => this.magicBook.enchant(this.weapon)

  describeWeapon = () => this.weapon.stats()
}
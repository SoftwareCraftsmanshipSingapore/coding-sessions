export class Enchantment {
  constructor(prefix, attributes) {
    this.prefix = prefix
    this.attributes = attributes
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
    let attrs = this.attrs.concat(this.enchantment.attributes)
    return `${this.enchantment.prefix} ${this.name}`.trim() + "\n" + attrs.map(a => " " + a).join("\n")
  }
}

export class MagicBook {
  constructor(selectFn, removeFn){
    this.selectFn = selectFn
    this.removeFn = removeFn
    this.enchantments = [
      new Enchantment("Inferno", ["+5 fire damage"]),
      new Enchantment("Icy"    , ["+5 ice damage"]),
      new Enchantment("Foo"    , ["bar"])
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
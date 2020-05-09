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

  stats() {
    let attrs = [...this.attrs, ...this.extraAttrs]
    return `${this.prefix} ${this.name}`.trim() + "\n" + attrs.map(a => " " + a).join("\n")
  }
}

export class MagicBook {
  constructor(selectFn){
    this.selectFn = selectFn
    this.enchantments = [
      {prefix: "Inferno", extraAttr: "+5 fire damage"},
      {prefix: "Icy", extraAttr: "+5 ice damage"}
    ]
  }

  giveOne() {
    let maxN = this.enchantments.length
    return this.enchantments[this.selectFn(maxN)]
  }
}

export class Durance {

  constructor(weaponSpec, magic, addOrRemove) {
    this.weapon = new Weapon(weaponSpec)
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
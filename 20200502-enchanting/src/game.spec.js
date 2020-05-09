import {Durance, Weapon, MagicBook} from './game'
import {beforeEach, describe, expect, it} from "@jest/globals";

let weaponSpec = {
  name: "Dagger of the Nooblet",
  attrs: ["5 - 10 attack damage","1.2 attack speed"]
}

let daggerStats =
    "Dagger of the Nooblet\n" +
    " 5 - 10 attack damage\n" +
    " 1.2 attack speed"

describe("Weapon", ()=> {

  let weapon
  beforeEach(() => {
    weapon = new Weapon(weaponSpec)
  })

  it("can make a new weapon", () => {
    expect(weapon.stats()).toEqual(daggerStats)
  })

  it("can be enchanted", () => {
    let expected =
        "prefix Dagger of the Nooblet\n" +
        " 5 - 10 attack damage\n" +
        " 1.2 attack speed\n" +
        " extra"
    weapon.enchant({prefix: "prefix", extraAttr: "extra"})
    expect(weapon.stats()).toEqual(expected)
  })

  it("can remove enchantment", () => {
    weapon.enchant({prefix: "prefix", extraAttr: "extra"})
    weapon.remove_enchantment()
    expect(weapon.stats()).toEqual(daggerStats)
  })

})

describe("Durance", () => {

  let durance, enchantmentPrefix, removeMagic
  function select(prefixes) {
    if (enchantmentPrefix !== "") {
       if (!prefixes.includes(enchantmentPrefix))
         throw new Error("'" + enchantmentPrefix + "'" + " is not in [" + prefixes + "]")
    }
    return enchantmentPrefix
  }
  beforeEach(() => {
    durance = new Durance(
      weaponSpec,
      new MagicBook(select),
      () => !removeMagic
    )
    enchantmentPrefix = ""
    removeMagic = false
  })

  it("should start with a dagger", () => {
    expect(durance.describeWeapon()).toEqual(daggerStats)
  })

  it("enchant once", () => {
    enchantmentPrefix = "Inferno"
    durance.enchant()
    let expected =
        "Inferno Dagger of the Nooblet\n" +
        " 5 - 10 attack damage\n" +
        " 1.2 attack speed\n" +
        " +5 fire damage"
    expect(durance.describeWeapon()).toEqual(expected)
  })

  it("enchant twice", () => {
    enchantmentPrefix = "Inferno"
    durance.enchant()
    enchantmentPrefix = "Icy"
    durance.enchant()
    let expected =
        "Icy Dagger of the Nooblet\n" +
        " 5 - 10 attack damage\n" +
        " 1.2 attack speed\n" +
        " +5 ice damage"
    expect(durance.describeWeapon()).toEqual(expected)
  })

  it("try enchant twice with same enchantment", () => {
    enchantmentPrefix = "Inferno"
    durance.enchant()
    enchantmentPrefix = "Inferno"
    expect(() => durance.enchant()).toThrow(Error("'Inferno' is not in [Icy,Foo]"))
    let expected =
        "Inferno Dagger of the Nooblet\n" +
        " 5 - 10 attack damage\n" +
        " 1.2 attack speed\n" +
        " +5 fire damage"
    expect(durance.describeWeapon()).toEqual(expected)
  })

  it("enchantment can be removed", () => {
    enchantmentPrefix = "Inferno"
    durance.enchant()
    removeMagic = true
    durance.enchant()
    expect(durance.describeWeapon()).toEqual(daggerStats)
  })

})
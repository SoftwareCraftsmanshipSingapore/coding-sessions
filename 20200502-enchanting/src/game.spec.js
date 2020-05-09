import {Durance, Weapon, MagicBook} from './game'
import {beforeEach, describe, expect, it} from "@jest/globals";

describe("Weapon", ()=> {

  let weaponSpec = {
    name: "Dagger of the Nooblet",
    attrs: ["5 - 10 attack damage","1.2 attack speed"]
  }
  let daggerStats =
      "Dagger of the Nooblet\n" +
      " 5 - 10 attack damage\n" +
      " 1.2 attack speed"

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

  let durance, enchantmentIndex, removeMagic
  beforeEach(() => {
    durance = new Durance(new MagicBook((maxN) => enchantmentIndex), () => !removeMagic)
    enchantmentIndex = 0
    removeMagic = false
  })

  it("should start with a dagger", () => {
    expect(durance.describeWeapon()).toEqual("Dagger of the Nooblet")
  })

  it("enchant once", () => {
    durance.enchant()
    expect(durance.describeWeapon()).toEqual("Inferno Dagger of the Nooblet")
  })

  it("enchant twice", () => {
    durance.enchant()
    enchantmentIndex = 1
    durance.enchant()
    expect(durance.describeWeapon()).toEqual("Icy Dagger of the Nooblet")
  })

  it("enchantment can be removed", () => {
    durance.enchant()
    removeMagic = true
    durance.enchant()
    expect(durance.describeWeapon()).toEqual("Dagger of the Nooblet")
  })

})
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

  let durance, enchantmentIndex, removeMagic
  beforeEach(() => {
    durance = new Durance(
      weaponSpec,
      new MagicBook((maxN) => enchantmentIndex),
      () => !removeMagic
    )
    enchantmentIndex = 0
    removeMagic = false
  })

  it("should start with a dagger", () => {
    expect(durance.describeWeapon()).toEqual(daggerStats)
  })

  it("enchant once", () => {
    durance.enchant()
    let expected =
        "Inferno Dagger of the Nooblet\n" +
        " 5 - 10 attack damage\n" +
        " 1.2 attack speed\n" +
        " +5 fire damage"
    expect(durance.describeWeapon()).toEqual(expected)
  })

  it("enchant twice", () => {
    durance.enchant()
    enchantmentIndex = 0
    durance.enchant()
    let expected =
        "Icy Dagger of the Nooblet\n" +
        " 5 - 10 attack damage\n" +
        " 1.2 attack speed\n" +
        " +5 ice damage"
    expect(durance.describeWeapon()).toEqual(expected)
  })

  xit("try enchant twice with same enchantment", () => {
    //TODO: fix randomness control
    durance.enchant()
    enchantmentIndex = 0
    durance.enchant()
    let expected =
        "Inferno Dagger of the Nooblet\n" +
        " 5 - 10 attack damage\n" +
        " 1.2 attack speed\n" +
        " +5 fire damage"
    expect(durance.describeWeapon()).toEqual(expected)
  })

  it("enchantment can be removed", () => {
    durance.enchant()
    removeMagic = true
    durance.enchant()
    expect(durance.describeWeapon()).toEqual(daggerStats)
  })

})
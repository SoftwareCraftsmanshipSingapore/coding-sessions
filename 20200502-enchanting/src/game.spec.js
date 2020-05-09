import {Durance, Weapon, MagicBook} from './game'
import {beforeEach, describe, expect, it} from "@jest/globals";

describe("Weapon", ()=> {

  let weapon
  beforeEach(() => {
    weapon = new Weapon("foo")
  })

  it("can be enchanted", () => {
    expect(weapon.stats()).toEqual("foo")
    weapon.enchant({prefix: "prefix"})
    expect(weapon.stats()).toEqual("prefix foo")
  })

  it("can remove enchantment", () => {
    weapon.enchant({prefix: "prefix"})
    weapon.remove_enchantment()
    expect(weapon.stats()).toEqual("foo")
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
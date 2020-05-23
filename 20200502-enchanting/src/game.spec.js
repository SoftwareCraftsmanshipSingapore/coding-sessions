import {Durance, Weapon, MagicBook, Enchantment} from './game'
import {beforeEach, describe, expect, it} from "@jest/globals";

let weaponSpec = {
  name: "Dagger of the Nooblet",
  attrs: ["5 - 10 attack damage", "1.2 attack speed"]
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
    weapon.enchant(new Enchantment("prefix", ["extra"]))
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
      new MagicBook(select, () => removeMagic)
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


describe("MagicBook", () => {

  let testWeaponSpec = {
    name: "test weapon",
    attrs: ["weapon attr"]
  }

  let enchantmentPrefix, removeEnchantment
  let magicBook, weapon

  beforeEach(() => {
    magicBook = new MagicBook(() => enchantmentPrefix, () => removeEnchantment)
    weapon = new Weapon(testWeaponSpec)
    enchantmentPrefix = ""
    removeEnchantment = false
  })

  it("should enchant a non enchanted weapon", () => {
    enchantmentPrefix = "Inferno"
    magicBook.enchant(weapon)
    let expected =
        "Inferno test weapon\n" +
        " weapon attr\n" + 
        " +5 fire damage"
    expect(weapon.stats()).toEqual(expected)
  })

  it("enchant twice with different enchantments", () => {
    enchantmentPrefix = "Foo"
    magicBook.enchant(weapon)
    enchantmentPrefix = "Inferno"
    magicBook.enchant(weapon)
    let expected =
        "Inferno test weapon\n" +
        " weapon attr\n" + 
        " +5 fire damage"
    expect(weapon.stats()).toEqual(expected)
  })

  it("should be able to remove enchantment", () => {
    enchantmentPrefix = "Inferno"
    magicBook.enchant(weapon)
    let expectedEnchanted =
        "Inferno test weapon\n" +
        " weapon attr\n" +
        " +5 fire damage"
    expect(weapon.stats()).toEqual(expectedEnchanted)
    removeEnchantment = true
    magicBook.enchant(weapon)
    let expected =
        "test weapon\n" +
        " weapon attr"
    expect(weapon.stats()).toEqual(expected)
  })

  it("should not be able to enchant twice with the same", () => {
    //testing a faulty select function
    //NOT testing production behaviour with a correct select function
    const selectMock = jest.fn(() => enchantmentPrefix)
    magicBook = new MagicBook(selectMock, () => removeEnchantment)
    enchantmentPrefix = "Inferno"
    magicBook.enchant(weapon)
    magicBook.enchant(weapon)
    expect(selectMock).toBeCalledWith(["Inferno", "Icy", "Foo"])
    expect(selectMock).toBeCalledWith(["Icy", "Foo"])
    let expectedStats =
        "Inferno test weapon\n" +
        " weapon attr\n" +
        " +5 fire damage"
    expect(weapon.stats()).toEqual(expectedStats)
  })

})
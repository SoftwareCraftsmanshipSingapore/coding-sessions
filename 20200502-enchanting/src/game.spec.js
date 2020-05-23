import {Durance, Weapon, MagicBook, Enchantment, Attribute} from './game'
import {beforeEach, describe, expect, it} from "@jest/globals";

let weaponSpec = {
  name: "Dagger of the Nooblet",
  attrs: [new Attribute("5 - 10", "attack damage"), new Attribute("1.2", "attack speed")]
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
        " +5 extra"
    weapon.enchant(new Enchantment("prefix", new Attribute("+5", "extra")))
    expect(weapon.stats()).toEqual(expected)
  })

  it("can remove enchantment", () => {
    weapon.enchant({prefix: "prefix", extraAttr: "extra"})
    //TODO: verify weapon stats
    weapon.remove_enchantment()
    expect(weapon.stats()).toEqual(daggerStats)
  })

  it("basic weapon has dps", () => {
    let weaponSpec = {
      name: "Dagger of the Nooblet",
      attrs: [new Attribute("5 - 21", "attack damage"), new Attribute("1.2", "attack speed")]
    }
    weapon = new Weapon(weaponSpec)
    let expectedDps = (21 + 5) / 2 / 1.2
    expect(weapon.dps()).toBeCloseTo(expectedDps)
  })

  it("a weapon enchanted with Agility has dps", () => {
    let weaponSpec = {
      name: "Dagger of the Nooblet",
      attrs: [new Attribute("5 - 21", "attack damage"), new Attribute("1.2", "attack speed")]
    }
    weapon = new Weapon(weaponSpec)
    weapon.enchant(new Enchantment("Quick", new Attribute("+5", "agility")))
    let expectedSpeed = (1.2 + 5) / 10
    let expectedDps = (21 + 5) / 2 / expectedSpeed
    expect(weapon.dps()).toBeCloseTo(expectedDps)
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
    attrs: [new Attribute("+5", "weapon attr")]
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
        " +5 weapon attr\n" +
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
        " +5 weapon attr\n" +
        " +5 fire damage"
    expect(weapon.stats()).toEqual(expected)
  })

  it("should be able to remove enchantment", () => {
    enchantmentPrefix = "Inferno"
    magicBook.enchant(weapon)
    let expectedEnchanted =
        "Inferno test weapon\n" +
        " +5 weapon attr\n" +
        " +5 fire damage"
    expect(weapon.stats()).toEqual(expectedEnchanted)
    removeEnchantment = true
    magicBook.enchant(weapon)
    let expected =
        "test weapon\n" +
        " +5 weapon attr"
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
        " +5 weapon attr\n" +
        " +5 fire damage"
    expect(weapon.stats()).toEqual(expectedStats)
  })

})
import {Durance, Weapon, MagicBook} from './game'

describe("Weapon", ()=> {

  it("can be enchanted", () => {
    const weapon = new Weapon("foo")
    weapon.enchant({prefix: "prefix"})
    expect(weapon.stats()).toEqual("prefix foo")
  })

  it("can remove enchantment", () => {
    const weapon = new Weapon("foo")
    weapon.enchant({prefix: "prefix"})
    weapon.remove_enchantment()
    expect(weapon.stats()).toEqual("foo")
  })

})

describe("Durance", () => {

  let index = 0
  function selectFn(maxN) {return index}
  
  let removeMagic = false
  function addRemove() { return !removeMagic }

  const magic = new MagicBook(selectFn)

  function makeDurance() {
    return new Durance(magic, addRemove)
  }

  it("should start with a dagger", () => {
    const durance = makeDurance()
    expect(durance.describeWeapon()).toEqual("Dagger of the Nooblet")
  })

  it("enchant once", () => {
    const durance = makeDurance()
    index = 0
    durance.enchant()
    expect(durance.describeWeapon()).toEqual("Inferno Dagger of the Nooblet")
  })

  it("enchant twice", () => {
    const durance = makeDurance()
    durance.enchant()
    index = 1
    durance.enchant()
    expect(durance.describeWeapon()).toEqual("Icy Dagger of the Nooblet")
  })

  it("enchant can be removed", () => {
    const durance = makeDurance()
    index = 0
    durance.enchant()
    removeMagic = true
    durance.enchant()
    expect(durance.describeWeapon()).toEqual("Dagger of the Nooblet")
  })

})
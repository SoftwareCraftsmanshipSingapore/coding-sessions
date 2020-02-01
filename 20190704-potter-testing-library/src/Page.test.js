import React from 'react'
import { render, fireEvent, cleanup, getNodeText } from '@testing-library/react'
import Page from "./Page"

const setup = () => {
  const utils = render(<Page />)
  return utils
  
}

afterEach(cleanup)

test('Book 1 clicked 100 times', () => {
  const { input, getByText, queryByText } = setup()
  for(let x = 0; x<100; x++){
  	  fireEvent.click(getByText('Buy 1'))
  }
  expect(getNodeText(queryByText(/Your total: .*/))).toBe('Your total: 800')
})

test('100 first and 200 second', () => {
  const { input, getByText, queryByText } = setup()
  for(let x = 0; x<100; x++){
  	  fireEvent.click(getByText('Buy 1'))
  	  fireEvent.click(getByText('Buy 2'))
  	  fireEvent.click(getByText('Buy 2'))
  }
  expect(getNodeText(queryByText(/Your total: .*/))).toBe(`Your total: ${ 100 * (8 + 2*8*0.95)}`)
  expect(getNodeText(queryByText(/Your total: .*/))).toBe(`Your total: 2320`)
})

test('nothing was clikced', () => {
  const { input, getByText, queryByText } = setup()
  expect(getNodeText(queryByText(/Your total: .*/))).toBe('Your total: 0')
})

test('book 1 clikced', () => {
  const { input, getByText, queryByText } = setup()
  fireEvent.click(getByText('Buy 1'))
  expect(getNodeText(queryByText(/Your total: .*/))).toBe('Your total: 8')
})

test('book 1 and 2 were clikced', () => {
  const { input, getByText, queryByText } = setup()
  fireEvent.click(getByText('Buy 1'))
  fireEvent.click(getByText('Buy 2'))
  expect(getNodeText(queryByText(/Your total: .*/))).toBe(`Your total: ${ 2*8*0.95 }`)
})

test('two copies of the same book were chosen', () => {
  const { input, getByText, queryByText } = setup()
  fireEvent.click(getByText('Buy 1'))
  fireEvent.click(getByText('Buy 1'))
  expect(getNodeText(queryByText(/Your total: .*/))).toBe(`Your total: ${ 16 }`)
})

test('choose book1 two times book2 once', () => {
  const { input, getByText, queryByText } = setup()
  fireEvent.click(getByText('Buy 1'))
  fireEvent.click(getByText('Buy 1'))
  fireEvent.click(getByText('Buy 2'))
  expect(getNodeText(queryByText(/Your total: .*/))).toBe(`Your total: ${ 15.2+8 }`)
})

test('choose book1, book2, and book 3', () => {
  const { input, getByText, queryByText } = setup()
  fireEvent.click(getByText('Buy 1'))
  fireEvent.click(getByText('Buy 2'))
  fireEvent.click(getByText('Buy 3'))
  expect(getNodeText(queryByText(/Your total: .*/))).toBe(`Your total: ${ 3*8*0.90 }`)
})
test('choose book1, book2, book 3, and book4', () => {
  const { input, getByText, queryByText } = setup()
  fireEvent.click(getByText('Buy 1'))
  fireEvent.click(getByText('Buy 2'))
  fireEvent.click(getByText('Buy 3'))
  fireEvent.click(getByText('Buy 4'))
  expect(getNodeText(queryByText(/Your total: .*/))).toBe(`Your total: ${ 4*8*0.80 }`)
})

test('choose book1, book2, book 3, book4, and book5', () => {
  const { input, getByText, queryByText } = setup()
  fireEvent.click(getByText('Buy 1'))
  fireEvent.click(getByText('Buy 2'))
  fireEvent.click(getByText('Buy 3'))
  fireEvent.click(getByText('Buy 4'))
  fireEvent.click(getByText('Buy 5'))
  expect(getNodeText(queryByText(/Your total: .*/))).toBe(`Your total: ${ 5*8*0.75 }`)
})
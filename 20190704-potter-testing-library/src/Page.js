import React from 'react';
import {useState} from "react";

const Page = () => {
  const [book1, setBook1] = useState(0);
  const [book2, setBook2] = useState(0);
  const [book3, setBook3] = useState(0);
  const [book4, setBook4] = useState(0);
  const [book5, setBook5] = useState(0);
  
  let total = 0
  
  let order = [book1, book2, book3, book4, book5]
  for (const unsed of [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1]) {
    if (!order.length) { break; }
    if (order.some(e => !e)) {
      order = order.filter(element => element > 0)
      continue
    }
    if (order.length > 4) {
      order[0] -= 1
      order[1] -= 1
      order[2] -= 1
      order[3] -= 1
      order[4] -= 1
      total += 30 
    }
    else if (order.length > 3) {
      order[0] -= 1
      order[1] -= 1
      order[2] -= 1
      order[3] -= 1
      total += 25.6  
    }
    else if (order.length > 2) {
      order[0] -= 1
      order[1] -= 1
      order[2] -= 1
      total += 21.6  
    }
    else if(order.length > 1){
      const x = Math.min(...order);
      order[0] -= x
      order[1] -= x
      total += 15.2 * x
    }
    else{
      total += 8 * order[0]
      order[0] = 0
    }
  }
  
  return <>
    <button onClick={() => setBook1(book1 + 1)}>Buy 1</button>
    <button onClick={() => setBook2(book2 + 1)}>Buy 2</button>
    <button onClick={() => setBook3(book3 + 1)}>Buy 3</button>
    <button onClick={() => setBook4(book4 + 1)}>Buy 4</button>
    <button onClick={() => setBook5(book5 + 1)}>Buy 5</button>
    <div>Book0 {book1} copies</div>
    <div>Book1 {book2} copies</div>
    <div>Book2 {book3} copies</div>
    <div>Book3 {book4} copies</div>
    <div>Book4 {book5} copies</div>
    <div>Your total: {total}</div>
</>;
};

export default Page; 
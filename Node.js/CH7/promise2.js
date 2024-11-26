//result배열의 요소 순서는 Promise.all에 전달되는 순서와 동일하다 
let result = Promise.all([
  new Promise(resolve => setTimeout(() => resolve(1), 3000)),
  new Promise(resolve => setTimeout(() => resolve(2), 3000)),
  new Promise(resolve => setTimeout(() => resolve(3), 3000))
])
.then(result => console.log(result)); //위의 3가지 setTimeout이 실행되어 9초가 아니라 3초이다.

const pro = true;
const promise = new Promise((resolve, reject) => {
  if(pro) {
    resolve('성공!');
  }
  else {
    reject('실패!');
  }
});

promise
.then((data) => {
  console.log(data)
})
.catch((err) => {
  console.error(err)
});

//let, var, const의 차이점
//let 함수 재할당 가능 -> 블록 범위
//const 재할당 x -> 블록범위
//var 함수 재할당 가능 -> 함수범위
let user = {
    id : 'jamsuham',
    pw : '1234',
    name : '잠수함',
    age : 30,
};

let {id, ...rest} = user;
//user에서 id속성을 제외한 나머지 속성들을 rest에 저장
console.log(id);
console.log(rest.pw);
console.log(rest.name);
console.log(rest.age);
console.log(user.name);
console.log(user);

/*
let dic = new Object();

dic.boy = "소년";
dic.girl = "소녀";
dic.friend = "친구";

console.log(dic.boy);
console.log(dic.girl);
console.log(dic.friend);


//object는 const로 많이 한다. 실수를 줄이기 위해서 밑에 과정 실행하면 오류!
const dic = {
    present : "현재"
};

dic = {};
console.log(dic.present);
dic.present = "선물";
console.log(dic.present);
*/

const readline = require("readline");
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

rl.question("다이쓰! 무조건 천원, 상품입력!", function(key){
    let basket = {
        [key] : "1000원",
    }
    console.log(basket[key]);
})

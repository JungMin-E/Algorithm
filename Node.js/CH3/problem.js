const readline = require('readline');

const rl = readline.createInterface ({
    input: process.stdin,
    output: process.stdout,
});

var sum = 0;

rl.question('첫번째 입력값 : ', (a) => {
    sum += parseInt(a); //문자열로 입력받는다고 생각하기 때문에 parseInt로 정수형으로 바꿔주는 구문 필요
    rl.question('두번째 입력값 : ', (b) => {
        sum += parseInt(b);

        console.log('두수의 합은 ' + sum + ' 입니다.');
        rl.close();
    })
})






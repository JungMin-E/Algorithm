const readline = require("readline");
const rl = readline.Interface({
    input: process.stdin,
    output: process.stdout,
});

function func() {
    rl.question('? ', (a) => {
        const [a1, a2] = a.split(/[\+\-\*\/]/);
        //str.split(/[,.]/); ,.이 두가지로 문자열을 나누는
        var b1 = parseInt(a1);
        var b2 = parseInt(a2);
        //-1이 아니라는 소리는 해당 입력받은 문자에 '+'이 포함된다는 소리
        if(a.indexOf('+') != -1) {
            console.log(b1 + b2);
        }
        else if(a.indexOf('-') != -1) {
            console.log(b1 - b2);
        }
        else if(a.indexOf('/') != -1) {
            if(b2 == 0) {
                console.log('0으로 나눌 수 없습니다.');
            }
            else {
                console.log(b1 / b2);
            }
        }
        else if(a.indexOf('*') != -1) {
            console.log(b1 * b2);
        }

        func();
    })
}

func();
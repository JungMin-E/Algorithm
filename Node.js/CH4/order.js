const readline = require("readline");
const rl = readline.Interface({
    input: process.stdin,
    output: process.stdout,
});

var arr = [];
var max;

function menu() {
    rl.question('명령어를 입력하시오: ', (a) => {
        const[a1, a2] = a.split(' '); //a라는 데이터를 공백을 기준으로 a1, a2로 나눈다.
        if(a1 == 'delete') {
            var num = arr.indexOf(a2);
            arr.splice(num, 1);
            menu();
        }
        else if (a1 == 'view') {
            for(var i = 0; i < arr.length; i++) {
                console.log(arr[i]);
            }
            menu();
        }
        else if(a1 == 'add'){
            if(arr.length < max) {
                arr.push(a2);
                menu();
            }
            else {
                console.log('최대개수를 초과하였습니다.');
                menu();
            }
        }
        else if(a1 == 'exit') {
            rl.close();
            return;
        }
        else if(a1 == 'set') {
            max = parseInt(a2);
            menu();
        }
        else {
            arr.push(a1);
            menu();
        }

    });
}

menu();
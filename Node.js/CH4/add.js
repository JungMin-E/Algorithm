const readline = require("readline");
const rl = readline.Interface({
    input: process.stdin,
    output: process.stdout,
});

var arr = [];
var arr2 = ['abc', 'aaa', 'bbb'];

var i = arr2.indexOf('aaa'); //해당 배열의 인덱스 번호를 출력 존재하지 않는 값의 경우는 -1 출력

//arr2.splice(i, 1); 

for(var i = 0; i < arr2.length; i++) {
    console.log(arr2[i]);
}

//console.log('delete 입력시 삭제, view 입력시 모든 데이터 조회');

function menu() {

    rl.question('추가할 데이터를 입력하시오: ', (a) => {
        const[a1, a2] = a.split(' '); //a라는 데이터를 공백을 기준으로 a1, a2로 나눈다.
        if(a1 == 'delete') {
            arr2.pop();
            menu();
        }
        else if (a1 == 'view') {
            for(var i = 0; i < arr.length; i++) {
                console.log(arr2[i]);
            }
            menu();
        }
        else if(a1 == 'add'){
            arr2.push(a2);
            menu();
        }
        else if(a1 == 'exit') {
            rl.close();
            return;
        }
        else {
            arr2.push(a1);
            menu();
        }

    });
}

menu();
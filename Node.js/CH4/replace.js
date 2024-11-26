//명령어를 입력하시오 set 2
//명령어를 입력하시오 add abc
//명령어를 입력하시오 view
//명령어를 입력하시오 delete bbb
//명령어를 입력하시오 replace aaa bbb aaa가 bbb로 바뀜

const readline = require("readline");
const rl = readline.Interface({
    input: process.stdin,
    output: process.stdout,
});

var arr = [];
var max;

function menu() {

    rl.question('명령어를 입력하시오: ', (a) => {
        const[a1, a2, a3] = a.split(' ');
        if(a1 == 'delete') {
            var num = arr.indexOf(a2);

            if(num != -1) {
                arr.splice(num, 1);
                for(var i = 0; i < arr.length; i++) {
                    console.log(arr[i]);
                }
                menu();
            }
            else {
                console.log("입력된 값이 없습니다.");
                menu();
            }
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
        else if(a1 == 'replace') {
            var index = arr.indexOf(a2);
            
            if(index != -1) {
                arr.splice(index, 1, a3); //splice를 이용해서 범위를 바꿀 수 있다.
                for(var i = 0; i < arr.length; i++) {
                    console.log(arr[i]);
                }
                menu();
            }
        }
        else {
            arr.push(a1);
            menu();
        }

    });
}

menu();
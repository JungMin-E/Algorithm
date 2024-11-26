const readline = require("readline");
const rl = readline.Interface({
    input: process.stdin,
    output: process.stdout,
});

var arr = [];

function menu() {
    rl.question('추가할 데이터를 입력하시오: ', (a) => {
        const[a1, a2] = a.split(' '); //a라는 데이터를 공백을 기준으로 a1, a2로 나눈다.
        if(a1 == 'delete') {
            var num = arr.indexOf(a2);
            if(num != -1) {
                arr.splice(1, num);
                menu();
            }
            else {
                console.log("데이터가 없습니다.");
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
            arr.push(a2);
            menu();
        }
        else if(a1 == 'exit') {
            rl.close();
            return;
        }
        else {
            arr.push(a1);
            menu();
        }

    });
}

menu();
const readline = require("readline");
const rl = readline.Interface({
    input: process.stdin,
    output: process.stdout,
});

var arr = [];

function askQuestion(query) {
    return new Promise((resolve) => {
        rl.question(query, resolve);
    });
}
//async는 비동기 함수로 명시적으로 Promise를 암시적으로 반환
async function askInput() {
    console.log('1.조회');
    console.log('2.추가');
    console.log('3.삭제');
    console.log('4.종료');

    const input = await askQuestion('입력 : '); //await 비동기 함수 호출 될 때 사용

    if(parseInt(input) == 1) {
        for(var i = 0; i < arr.length; i++) {
            console.log(arr[i]);
        }
    }
    if(parseInt(input) == 2) {
        await askAdd();
    }
    if(parseInt(input) == 3) {
        await askRemove();

    }
    if(parseInt(input) == 4) {
        rl.close();
        return;
    }
    await askInput();
}

async function askAdd() {
    const input = await askQuestion('단어 : ');
    arr.push(input);
    await askInput();
}

async function askRemove() {
    const input = await askQuestion('단어 : ');
    const index = arr.indexOf(input);

    if(index != -1) {
        arr.splice(index, 1);
        for(var i = 0; i < arr.length; i++) {
            console.log(arr[i]);
        }
    }
    await askInput();
    
}
askInput(); //askInput2와 동일

/*
function askInput2() {
    askQuestion('단어를 입력하세요 (종료 : exit) : ').then(input => {
        if(input == 'exit') {
            rl.close();
        }
        else {
            askInput2();
        }
    });
}
*/
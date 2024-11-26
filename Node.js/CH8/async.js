function pro1() {
    return new Promise(function(resolve, reject) {
        setTimeout(function() {
            resolve('pro1 success');
        }, 1000);
    });
}

/*
function test1() {
    pro1().then(function(result) {
        console.log(result);
    });
}
*/
//위의 pro1().then부터  ~ });까지 동일하게 작동
async function test2() {
    var result = await pro1();
    console.log(result);
}

//test1();
test2(); //function(async부분)을 날리게 되면 작동하지 않는다. 
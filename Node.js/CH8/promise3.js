const readline = require("readline");
const rl = readline.Interface({
    input: process.stdin,
    output: process.stdout,
});
/*
const pro1 = pid => {
    return new Promise((resolve, reject)=> {
        setTimeout(()=> {
            if(pid == "jamsuham") {
                resolve('pro1 success');
            } else {
                reject('pro1 fail');
            }
        }, 1000);
    });
}

const pro2 = ppw => {
    return new Promise((resolve, reject)=> {
        setTimeout(()=> {
            if(ppw == '1111') {
                resolve('pro2 success');
            } else {
                reject('pro2 fail');
            }
        }, 1000);
    });
}

const id = 'jamsuham';
const pw = '1111';

pro1(id)
.then(result => {
    console.log('result', result);
    return pro2(pw);
}) 
.then(result => {
    console.log('result2', result);
})
.catch(err => {
    console.log('err', err);
    return Promise.reject(err);
})
*/

var Promise = require('promise');

var asyncfunction = function(param) {
    return new Promise(function(resolve, reject) {
        setTimeout(function() {
            resolve('hello' + param);
        }, 3000);
    });
};

var promise = asyncfunction('Mookie');
promise
.then(console.log)
.catch(console.err);
const argv = process.argv;

var sum = parseInt(argv[2]) + parseInt(argv[3]);
//argv[0]은 Node.js의 파일 경로 argv[1]은 실행중인 파일 스트랩트 경로 ex) test.js
console.log('두 수의 합은 : ' + sum + ' 입니다.');


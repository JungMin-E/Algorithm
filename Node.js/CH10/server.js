//mongoDB 접속
const express = require('express');
const mongoclient = require('mongodb').MongoClient;
var mydb;
const url = 'mongodb+srv://ljm9990:1234@cluster0.1ip8q.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0';
mongoclient.connect(url)
    .then(client => {
        console.log('몽고DB 접속 성공');
        mydb = client.db('myboard');
    })
    .catch(err => {
        console.error('몽고DB 접속 실패: ', err);
    });

//mysql 접속
/*
const express = require('express');
var mysql = require("mysql2");

var conn = mysql.createConnection ({
    host: "localhost",
    user: "root",
    password: "",
    database: "myboard"
});
conn.connect(); //mysql connect하는 
*/
const app = express();

const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({extented: true}));
app.set('view engine', 'ejs');
app.set('views', __dirname + '/view');

app.listen(8080, function() {
    console.log("포트 8080으로 서버 대기중 ...");
});

app.get('/enter', function(req, res) {
    res.sendFile(__dirname + '/enter.html');
});

//post로만 호출해야지 삭제가능!
app.post('/delete', (req, res) => {
    console.log(req.body);
    console.log('삭제완료');
});

app.get('/list', function(req, res) {
    mydb.collection('post').find().toArray().then(result => {
        res.render('list', {data : result});
    }); 
});

app.post('/save', function(req, res) {
    const { title, content } = req.body;
    const sql = "INSERT INTO post (title, content) VALUES (?, ?)";
    //const sql = "INSERT INTO post (title, content, created) VALUES (?, ?, NOW())";
    mydb.collection('post').insertOne(
        {title : req.body.title, content : req.body.content, date : req.body.date,}
    ).then(result => {
        console.log(result);
        res.send('데이터 추가 성공');
    });
    /*
    conn.query(sql, [title, content], function(err, result) {
        if (err) {
            console.log(err);
            res.status(500).send("Error!");
        } else {
            console.log("Data save successfully:", result);
            res.send("Data save successfully!");
        }
    });
    */
});




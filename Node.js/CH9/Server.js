var mysql = require('mysql2');
const express = require('express');
const app = express();

app.use(express.json()); //json 형식의 본문을 parsing 할 수 있도록 설정하는 거.

var conn = mysql.createConnection( {
    host: "localhost",
    user: "root",
    password: "",
    database: "dbp" //특정 데이터베이스 이름으로 만들기
});

conn.connect();


app.listen(8080, function(){
    console.log("포트 8080으로 서버 대기중 ... ")
});
app.get('/book', function(req, res){
    res.send('도서 목록 관련 페이지입니다.');
})

//app.get('/getPost') 뭐 이런식으로 
app.get('/', function(req, res){
    const {title} = req.query;
    var query1 = `select * from post where title = '${title}'`; //파라미터 두개, 세개 이렇게 들어오면 어떻게 해야할지
    //res.sendFile(__dirname + '/index.html');
    conn.query("select * from post", (err, rows, fields) => {
        if(err)
            throw err;
        res.send(rows);
    });
})
//add, delete, get user 조회, 이런거

// 조회 (모든 게시글 조회)
app.get('/posts', (req, res) => {
    const query = 'SELECT * FROM post';
    conn.query(query, (err, rows) => {
        if (err) {
            console.error('Error executing query: ' + err);
            res.status(500).send('Database query error');
        } else {
            res.json(rows);
        }
    });
});
//삭제 (제목으로 게시글 삭제)
app.get('/posts/delete/:title', (req, res) => {
    const title = req.params.title; // URL 파라미터에서 제목 가져오기
    const query = 'DELETE FROM post WHERE title = ?'; // 제목에 해당하는 게시글 삭제

    conn.query(query, [title], (err, result) => {
        if (err) {
            console.error('Error executing query: ' + err);
            res.status(500).send('Database query error'); //코드 내부 서버 오류시
        }
        else {
            if (result.affectedRows > 0) {
                res.status(200).send('Post deleted successfully'); //코드의 요청이 성공적일때 GET요청이 성공적일때 
            }
            else {
                res.status(404).send('Post not found'); //요청한 리소스가 찾을 수 없을 때
            }
        }
    });
});
// 추가 (쿼리 파라미터로 새 게시글 추가)
app.get('/posts/add', (req, res) => {
    const { title, content, profile_id } = req.query;

    // 필수 파라미터가 없으면 오류 반환
    if (!title || !content || !profile_id) {
        return res.status(400).send('Missing required parameters');
    }

    // 데이터베이스에 데이터 추가
    const query = 'INSERT INTO post (title, created, content, profile_id) VALUES (?, NOW(), ?, ?)';
    conn.query(query, [title, content, profile_id], (err, result) => {
        if (err) {
            console.error('Error executing query: ' + err);
            res.status(500).send('Database query error');
        } 
        res.status(201).send('Post added successfully');
        
    });
});

// 조회 (특정 게시글 조회)
app.get('/posts/profile/:profile_id', (req, res) => {
    const profileId = req.params.profile_id;
    const query = 'SELECT * FROM post WHERE profile_id = ?';

    conn.query(query, [profileId], (err, rows) => {
        if (err) {
            console.error('Error executing query: ' + err);
            res.status(500).send('Database query error');
        }
        else {
            if (rows.length > 0) {
                res.json(rows);
            } 
            else {
                res.status(404).send('Post not found');
            }
        }
    });
});


app.get('/', function(req, res){
    const {title} = req.query;
    var query1 = `select * from post where title = '${title}'`; //파라미터 두개, 세개 이렇게 들어오면 어떻게 해야할지
    //conn.query(query1, [title], (req, result) => {) 와동일 const query1 = `SELECT * FROM post where title = ?`;
    conn.query("select * from post", (err, rows, fields) => {
        if(err)
            throw err;
        res.send(rows);
    });
})

//URL주소에 port번호/view입력하면 해당 테이블 조회할 수 있게
app.get('/:tableName/view', (req, res) => {
    const {tableName} = req.params; //URL로부터 테이블의 이름 가져오기 
    const query = `SELECT * FROM \`${tableName}\``;
    //query실행
    conn.query(query, (err, rows) => {
        if(err)
            throw err;
        res.json(rows);
    });
})
//테이블명받고/add?value값 추가
app.get('/:tableName/add', (req, res) => {
    const { title, content, profile_id } = req.query;
    const { tableName } = req.params;

    const query = `INSERT INTO \`${tableName}\` (title, content, created, profile_id) VALUES (?, ?, NOW(), ?)`;

    conn.query(query, [title, content, profile_id], (err, result) => {
        if (err) {
            //console.error('Error executing query:', err);
            res.status(500).send('Database query error');
            return;
        }
        res.status(201).send('Data added successfully');
    });
});
/*
app.get('/:tableName/delete', (req, res) => {
    const {tableName} = req.params;
    const {title, content} = req.query;

    const query = `DELETE FROM \`${tableName}\` WHERE title = ? OR content = ?`; //title을 입력하거나 content를 입력하면 해당 데이터가 지워지게

    conn.query(query, [title, content], (err, result) => {
        if (err) {
            res.status(500).send('Database query error');
            return;
        }
        res.status(201).send('Data added successfully');
    });
});
*/
//add, delete, get user 조회, 이런거

app.get('/:tableName/view', (req, res) => {
    const {tableName} = req.params; //URL로부터 테이블의 이름 가져오기 
    const {title, profile_id} = req.query;
    
    const query = `SELECT * FROM \`${tableName}\` WHERE title = ? AND profile_id = ?`;
    //query실행
    conn.query(query, [title, profile_id], (err, result) => {
        if (err) {
            res.status(500).send('Database query error');
            return;
        }
        res.status(201).send('Data added successfully');
    });
})

app.get('/:tableName1/:tableName2/view', (req, res) => {
    const {tableName1, tableName2} = req.params;
    
    const query = `SELECT * FROM \`${tableName1}\` AS E LEFT JOIN \`${tableName2}\` AS S ON E.STUDENT_ID = S.STUDENT_ID LEFT JOIN COURSES AS C ON E.COURSE_NAME = C.COURSE_NAME`;
    
    conn.query(query, (err, result) => {
        if(err) {
            res.status(500).send('Database query error');
            return;
        }
        //res.status(200).json(result);
        //res.status(200).send('Database query Success');
    });
});

app.get('/:tableName/:title/delete', (req, res) => {
    const {tableName, title} = req.params;
    const query = `DELETE FROM \`${tableName}\` WHERE title = '${title}'`;

    conn.query(query, (err, result) => {
        if(err) {
            res.status(500).send('Database query error');
            return;
        }
        //res.status(200).json(result);
        
    })
})
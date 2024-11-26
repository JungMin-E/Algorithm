const mongoclient = require('mongodb').MongoClient;
const url = mongodb = "mongodb+srv://ljm9990:1234@cluster0.1ip8q.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

mongoclient.connect(url) 
    .then(client => {
        console.log('OK');
    });
var express = require('express');
var app = express();
var path = require('path');
var formidable = require('formidable');
var fs = require('fs');

app.use(express.static(path.join(__dirname, 'public')));

app.get('/', function(req, res){
    res.sendFile(path.join(__dirname, 'views/landing.html'));
});

app.get('/forge', function(req, res){
  res.sendFile(path.join(__dirname, 'views/forge.html'));
});

app.get('/temp', function(req, res){
    res.sendFile(path.join(__dirname, 'views/index1.html'));
});


app.get('/getmidi', function(req, res) {
    console.log(req.query);

    // var formData = {
    //     bieber: artistslider1,
    //     bach:artistslider2,
    //     beatles:artistslider3,
    //     rhythmicImportance: rhythmicImportance,
    //     melodicImportance: melodicImportance,
    //     speed: speed,
    //     syncopation : syncopation,
    //     phraseLength : phraseLength,
    //     restAmount : restAmount,
    //     restType : restType,
    //     keySignature : keySignature
    // };


    //java -jar MelodySmith.jar
    // corpusPath
    // outputFileName
    // keySignature
    // rhythmicImportance
    // melodicImportance
    // restType
    // restAmount
    // syncopation
    // phraseLength
    // creativity
    // speed
    // artistValues

    var child = require('child_process').spawn(
        'java', ['-jar', './MelodySmith.jar',
            'public/corpus',
            'output.mid',
            req.query.keySignature,
            req.query.rhythmicImportance,
            req.query.melodicImportance,
            req.query.restType,
            req.query.restAmount,
            req.query.syncopation,
            req.query.phraseLength,
            req.query.creativity,
            req.query.speed,
            'beatles:'+req.query.beatles,
            'bach:'+req.query.bach,
            'bieber:'+req.query.bieber
        ]
    );

    var encoded = 'test';
    child.stdout.on('data', function(data) {
        console.log(data.toString());
        if(data.toString().includes('100')){

            var filename = 'output.mid';

            const Datauri = require('datauri').sync;
            encoded = Datauri(filename);
            //res.setHeader('Access-Control-Allow-Origin', 'http://0.0.0.0:8080');
            res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
            res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
            res.setHeader('Access-Control-Allow-Credentials', true);
            console.log('sending');
            res.send(encoded);
        }
    });

    child.stderr.on("data", function (data) {
        console.log(data.toString());
    });


    //
    // var file = fs.readFileSync(filePath);
    // console.log('Initial File content : ' + file);
    //
    // fs.watch(filePath, function(event, filename) {
    //     if(filename){
    //         console.log('Event : ' + event);
    //         console.log(filename + ' file Changed ...');
    //         file = fs.readFileSync(filePath);
    //         console.log('File content at : ' + new Date() + ' is \n' + file);
    //     }
    //     else{
    //         console.log('filename not provided')
    //     }
    // });

    // var found = false;
    //
    // while(!found) {
    //     console.log(1);
    //     if (fs.existsSync(filePath)) {
    //         console.log('Found file');
    //         found = true;
    //     }
    // }
});

app.get('/*', function(req, res){
    res.send('error page here');
});

var server = app.listen(process.env.PORT || 3000, function(){
    console.log('Server listening on port 3000');
});

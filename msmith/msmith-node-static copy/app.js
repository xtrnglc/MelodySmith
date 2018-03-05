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

app.get('/getmidi', function(req, res) {
    //console.log(req.query);

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


    // /corpus output.mid cMajor 1 1 Constructive 1 1 1 1 1 beatles:1 bach:2 bieber:50

    switch(req.query.corpus) {
        case 'corpus1':
            var child = require('child_process').spawn(
                'java', ['-jar', './MelodySmith.jar',
                    'public/corpora/corpus1',
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
                    'beatles:'+req.query.corpora.beatles,
                    'house:'+req.query.corpora.house,
                ]
            );
            break;
        case 'corpus2':
            var child = require('child_process').spawn(
                'java', ['-jar', './MelodySmith.jar',
                    'public/corpora/corpus2',
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
                    'prosonic:'+req.query.corpora.prosonic,
                    'folk:'+req.query.corpora.folk
                ]
            );
            break;
        default:
            var child = require('child_process').spawn(
                'java', ['-jar', './MelodySmith.jar',
                    'public/corpora/corpus3',
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
                    'beatles:'+req.query.corpora.beatles,
                    'house:'+req.query.corpora.house,
                    'prosonic:'+req.query.corpora.prosonic,
                    'folk:'+req.query.corpora.folk
                ]
            );
    }

    var encoded = 'test';
    var bigrams;
    child.stdout.on('data', function(data) {
        //console.log(data.toString());
        if(data.toString().startsWith('{')) {
            console.log('parsing');
            bigrams = JSON.parse(data.toString().replace(/'/g, "\""));
            //console.log(bigrams);
        }
        if(data.toString().includes('100') && !data.toString().startsWith('{')){

            var filename = 'output.mid';

            const Datauri = require('datauri').sync;
            //encoded = "\"datauri\":" + Datauri(filename);
            var response = {
                bigrams: bigrams,
                datauri: Datauri(filename)
            };
            console.log(response);

            console.log('sending');
            res.send(response);
        }
    });

    child.stderr.on("data", function (data) {
        console.log('error');
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
    res.sendFile(path.join(__dirname, 'views/landing.html'));
});

var server = app.listen(process.env.PORT || 3000, function(){
    console.log('Server listening on port 3000');
});

var MidiPlayer = MidiPlayer;
var loadFile, loadDataUri, Player;
var AudioContext = window.AudioContext || window.webkitAudioContext || false;
var ac = new AudioContext || new webkitAudioContext;
var eventsDiv = document.getElementById('events');
var songDataURI = '';
var width = 960;
var height = 400;
var numComparisons = 6;

const grandPiano = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/acoustic_grand_piano-mp3.js';
const acousticGuitar = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/acoustic_guitar_nylon-mp3.js';
const flute = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/flute-mp3.js';
const electricGuitar = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/electric_guitar_clean-mp3.js';
const musicBox = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/music_box-mp3.js';

var instrument = grandPiano;

var changeTempo = function(tempo) {
    Player.tempo = tempo;
}

var play = function(document) {
    Player.play();
    document.refs.playButton.innerHTML = 'Pause';
}

var keySignature = 'gChromatic';

var changeNumComparisons = function(newVal) {
    numComparisons = newVal;
}

var changeNgrams = function(newVal) {
    //console.log('wer' +newVal);
    //document.getElementById('loading').style.display = 'none';
    //newVal.refs.loading1.style.display = 'none';


    // <Progressbar completed={25} ref="Progress1" id="Progress1"/>
    nGramVal = newVal;
}

var svg = d3.select("#canvas")
    .append("svg")
    .attr("width", width)
    .attr("height", height);


var color = d3.scaleLinear().domain([45,75])
    .interpolate(d3.interpolateHcl)
    .range([d3.rgb("#007AFF"), d3.rgb('#FFF500')]);

var forge = function(document) {
    if(Player) {
        stop(document);
    }
    //console.log(document.state);
    document.refs.loading.style.display = 'none';

    document.refs.loadingCube.style.display = 'block';
    //  var artistslider1 = document.getElementById('artist-slider1').value;
    //  var artistslider2 = document.getElementById('artist-slider2').value;
    //  var artistslider3 = document.getElementById('artist-slider3').value;
    //
    //  //var chaosSliderval = document.getElementById('chaos-slider').value;
    //  var durationOfNotesSliderval = document.getElementById('durationOfNotes-slider').value;
    //  var intervalOfNoteSliderval = document.getElementById('intervalOfNote-slider').value;
    //  //var restSlider = document.getElementById('rest-slider').value;
    //  var nGramVal = document.getElementById('n-gram-selector').value;
    //  //var numComparisons = document.getElementById('num-comparisons-selector').value;
    //  if(document.getElementById('keysigdropdown').value === '') {
    //
    //  } else {
    //      keySignature = document.getElementById('keysigdropdown').value;
    //  }
    //
    //

    var formData = {
        bieber: document.state.artistslider1,
        bach: document.state.artistslider2,
        beatles: document.state.artistslider3,
        durationsOfNotesSliderval: document.state.focusOnRhythmSlider,
        intervalOfNoteSliderval: document.state.focusOnIntervalSlider,
        nGramVal: document.state.ngram,
        numComparisons: document.state.decisionsToCompare,
        keySignature : document.state.scaleval
    };
    //
    console.log(formData);

    //console.log(formData);
    $.ajax({
        url : 'http://localhost:3000/getmidi',
        type: 'GET',
        crossDomain: true,
        data: formData,
        success : function(data) {
            initiatePlayer(data, document);
        }
    })
    //initiatePlayer(document);
}

var switchInstrument = function(val) {
    if(val === 'acousticGuitar') {
        instrument = acousticGuitar;
    }
    if(val === 'flute') {
        instrument = flute;
    }
    if(val === 'electricGuitar') {
        instrument = electricGuitar;
    }
    if(val === 'musicBox') {
        instrument = musicBox;
    }if(val === 'grandPiano') {
        instrument = grandPiano;
    }
}

var showControls = function(document) {
    document.refs.loadingCube.style.display = 'none';
    document.refs.canvas.style.display = 'block';
    document.refs.musicControls.style.display = 'block';
    document.refs.tempoDiv.style.display = 'block';
    document.refs.playBarButton.style.display = 'block';
}

var initiatePlayer = function(data, document) {
    //console.log(data);
    //console.log('initiating player');
    showControls(document);


    Soundfont.instrument(ac, instrument).then(function (instrument) {
        document.refs.loading.style.display = 'none';

        loadDataUri = function(dataUri) {
            Player = new MidiPlayer.Player(function(event) {
                //console.log(event);
                if (event.name == 'Note on' && event.velocity > 0) {
                    instrument.play(event.noteName, ac.currentTime, {gain:event.velocity/100});
                    //document.querySelector('#track-' + event.track + ' code').innerHTML = JSON.stringify(event);
                    //console.log(event);
                }
                if(event.name=="Note on"){
                    document.newNote(event);
                    // var elLength = 40*(event.delta<=1?1:event.delta/120);
                    // var element = svg.append("g");
                    // //console.log(element);
                    // element.attr("transform","translate("+(-1*elLength)+" 0)");
                    // element.append("rect")
                    //     .attr("width", elLength)
                    //     .attr("height", 20)
                    //     .attr("rx", 5)
                    //     .attr("ry", 5)
                    //     .attr("x", 0)
                    //     .attr("y", (event.noteNumber - 45)*12)
                    //     .attr("fill", color(event.noteNumber));
                    //
                    // element.append("text")
                    //     .attr("x", 3)
                    //     .attr("y", 15 + (event.noteNumber - 45)*12)
                    //     .text(event.noteName);
                    //
                    //
                    // var t = d3.transition()
                    //     .duration(4000)
                    //     .ease(d3.easeLinear);
                    // element.transition(t)
                    //     .attr("transform","translate("+(width+300-elLength)+" 0)")
                    //     .remove();
                    // console.log(element);

                }


                document.refs.tempoDisplay.innerHTML = Player.tempo;
                document.refs.playBar.style.width = 100 - Player.getSongPercentRemaining() + '%';
            });

            Player.loadDataUri(dataUri);

            document.refs.playButton.removeAttribute('disabled');

            //buildTracksHtml();
            play(document);
        }

        songDataURI = data;

        loadDataUri(songDataURI);
    });
}

var changeInstrument = function(instrumentval, document) {
    console.log('changing instruments ' + instrumentval);
    switchInstrument(instrumentval);
    if(Player) {
        Player.stop();
        initiatePlayer(songDataURI, document);
    }

}

var pause = function(document) {
    Player.pause();
    document.refs.playButton.innerHTML = 'Play';
}

var stop = function(document) {
    Player.stop();
    document.refs.playButton.innerHTML = 'Play';
}


var mario = 'data:audio/midi;base64,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gDwAAJA+aAiAPgAAkDxiCIA8AACQPF8IgDwAAJA8bQiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AAH/LwA=';


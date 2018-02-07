var MidiPlayer = MidiPlayer;
var loadFile, loadDataUri, Player;
var AudioContext = window.AudioContext || window.webkitAudioContext || false;
var ac = new AudioContext || new webkitAudioContext;
var eventsDiv = document.getElementById('events');
var songDataURI = '';
var width = 960;
var height = 500;

const grandPiano = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/acoustic_grand_piano-mp3.js';
const acousticGuitar = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/acoustic_guitar_nylon-mp3.js';
const flute = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/flute-mp3.js';
const electricGuitar = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/electric_guitar_clean-mp3.js';
const churchOrgan = 'https://raw.githubusercontent.com/gleitz/midi-js-soundfonts/gh-pages/MusyngKite/church_organ-mp3.js';

var instrument = grandPiano;

var changeTempo = function(tempo) {
	Player.tempo = tempo;

}

var play = function() {
	Player.play();
	document.getElementById('play-button').innerHTML = 'Pause';
}

var keySignature = 'gChromatic';

var cbutton = function() {
    document.getElementById('c-button').style.backgroundColor = '#0FA0CE';
    document.getElementById('a-button').style.backgroundColor = '#33C3F0';
}

var abutton = function() {
    document.getElementById('c-button').style.backgroundColor = '#33C3F0';
    document.getElementById('a-button').style.backgroundColor = '#0FA0CE';
}

var svg = d3.select("#canvas")
    .append("svg")
    .attr("width", width)
    .attr("height", height);

var color = d3.scaleLinear().domain([45,75])
    .interpolate(d3.interpolateHcl)
    .range([d3.rgb("#007AFF"), d3.rgb('#FFF500')]);

var forge = function() {

    if(Player) {
        stop();
    }
    document.getElementById('loading').style.display = 'none';

    document.getElementById('loadingcube').style.display = 'block';
    var artistslider1 = document.getElementById('artist-slider1').value;
    var artistslider2 = document.getElementById('artist-slider2').value;
    var artistslider3 = document.getElementById('artist-slider3').value;

    //var chaosSliderval = document.getElementById('chaos-slider').value;
    var durationOfNotesSliderval = document.getElementById('durationOfNotes-slider').value;
    var intervalOfNoteSliderval = document.getElementById('intervalOfNote-slider').value;
    //var restSlider = document.getElementById('rest-slider').value;
    var nGramVal = document.getElementById('n-gram-selector').value;
    var numComparisons = document.getElementById('num-comparisons-selector').value;

    if(document.getElementById('keysigdropdown').value === '') {

    } else {
        keySignature = document.getElementById('keysigdropdown').value;
    }


    var formData = {
        backstreetboys: artistslider1,
        beethoven:artistslider2,
        katy:artistslider3,
        durationsOfNotesSliderval: durationOfNotesSliderval,
        intervalOfNoteSliderval: intervalOfNoteSliderval,
        nGramVal: nGramVal,
        numComparisons: numComparisons,
        keySignature : keySignature
    };

    //console.log(formData);
    $.ajax({
        url : '/getmidi',
        type: 'GET',
        data: formData,
        success : function(data) {
            initiatePlayer(data);
        }
    })
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
    if(val === 'churchOrgan') {
        instrument = churchOrgan;
    }if(val === 'grandPiano') {
        instrument = grandPiano;
    }

}

var initiatePlayer = function(data, instrumentVal) {
    document.getElementById('loadingcube').style.display = 'none';

    document.getElementById('canvas').style.display = 'block';

    switchInstrument(instrumentVal);

    Soundfont.instrument(ac, instrument).then(function (instrument) {
        document.getElementById('loading').style.display = 'none';

        loadDataUri = function(dataUri) {
            Player = new MidiPlayer.Player(function(event) {
                if (event.name == 'Note on' && event.velocity > 0) {
                    instrument.play(event.noteName, ac.currentTime, {gain:event.velocity/100});
                    //document.querySelector('#track-' + event.track + ' code').innerHTML = JSON.stringify(event);
                    //console.log(event);
                }
                if(event.name=="Note on"){
                    //http://localhost/gist/audio/jesu/
                    var elLength = 40*(event.delta<=1?1:event.delta/120);
                    var element = svg.append("g");
                    //console.log(element);
                    element.attr("transform","translate("+(-1*elLength)+" 0)");
                    element.append("rect")
                        .attr("width", elLength)
                        .attr("height", 20)
                        .attr("rx", 5)
                        .attr("ry", 5)
                        .attr("x", 0)
                        .attr("y", (event.noteNumber - 45)*12)
                        .attr("fill", color(event.noteNumber));

                    element.append("text")
                        .attr("x", 3)
                        .attr("y", 15 + (event.noteNumber - 45)*12)
                        .text(event.noteName);


                    var t = d3.transition()
                        .duration(4000)
                        .ease(d3.easeLinear);
                    element.transition(t)
                        .attr("transform","translate("+(width+300-elLength)+" 0)")
                        .remove();
                }


                document.getElementById('tempo-display').innerHTML = Player.tempo;
                document.getElementById('play-bar').style.width = 100 - Player.getSongPercentRemaining() + '%';
            });

            Player.loadDataUri(dataUri);

            document.getElementById('play-button').removeAttribute('disabled');

            //buildTracksHtml();
            play();
        }

        songDataURI = data;

        loadDataUri(songDataURI);
    });
}

var changeInstrument = function(instrument) {
    console.log('changing instruments ' + instrument);
    Player.stop();
    initiatePlayer(songDataURI, instrument);
}

var pause = function() {
	Player.pause();
	document.getElementById('play-button').innerHTML = 'Play';
}

var stop = function() {
	Player.stop();
	document.getElementById('play-button').innerHTML = 'Play';
}

var buildTracksHtml = function() {
	Player.tracks.forEach(function(item, index) {
		var trackDiv = document.createElement('div');
		trackDiv.id = 'track-' + (index+1);
		var h5 = document.createElement('h5');
		h5.innerHTML = 'Track ' + (index+1);
		var code = document.createElement('code');
		trackDiv.appendChild(h5);
		trackDiv.appendChild(code);
		eventsDiv.appendChild(trackDiv);
	});
}



var mario = 'data:audio/midi;base64,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gDwAAJA+aAiAPgAAkDxiCIA8AACQPF8IgDwAAJA8bQiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AACQPGIIgDwAAJA8YgiAPAAAkDxiCIA8AAH/LwA=';



var MidiPlayer = MidiPlayer;
var ABCJS = ABCJS;
var loadFile, loadDataUri, loadDataUriPlay, Player, PlayerHolder1,PlayerHolder2,PlayerHolder3,PlayerHolder4;
var AudioContext = window.AudioContext || window.webkitAudioContext || false;
var ac = new AudioContext || new webkitAudioContext;
var eventsDiv = document.getElementById('events');
var songDataURI = '';
var width = 960;
var height = 400;
var numComparisons = 6;
var restType = 'Constructive';
var algorithmChoice = 'PHRASED';
var rhythmicImportance = '1';
var melodicImportance = '1';
var syncopation = '1';
var phraseLength = '2';
var creativity = '1';
var speed = '1';
var restAmount = '1';
var instrumentText = 'grandPiano';
var keySignature = 'cMajor';
var bigrams;
var corpus = 'corpus1';
var width;
var created = false;
var abcString = '';
var tempo = 120;
var cooleys = 'X:1\nT: Cooley\'s\nM: 4/4\nL: 1/8\nR: reel\nK: Emin\nD2|:"Em"EB{c}BA B2 EB|~B2 AB dBAG|"D"FDAD BDAD|FDAD dAFD|\n"Em"EBBA B2 EB|B2 AB defg|"D"afe^c dBAF|1"Em"DEFD E2 D2:|2"Em"DEFD E2 gf||\n|:"Em"eB B2 efge|eB B2 gedB|"D"A2 FA DAFA|A2 FA defg|\n"Em"eB B2 eBgB|eB B2 defg|"D"afe^c dBAF|1"Em"DEFD E2 gf:|2"Em"DEFD E4|]\n';


// Rhythmic importance (Int 0-100)  "How closely should rhythmic details be analyzed?" (edited)
// Melodic importance (Int 0-100)  "How closely should intervalic details be analyzed?" (edited)
//
// Composition Controls:
// Rest Type (Switch with options: Constructive, Destructive)  "How should rests be added to the melody?" (edited)
// Rest Amount (Int 0-20) "How often should rests be played?" (edited)
// Syncopation  (Int 0-100) "How syncopated should the rhythm be?" (edited)
// Phrase Length (Int 0-20) "About how many notes should one phrase last?" (edited)
// Creativity (Int 0-10) "How often can less conventional notes be played?" (edited)
// Speed (Int 0-100) "Should shorter or longer notes be preferred?" (edited)

var switchCorpus = function(val) {
    corpus = val;
}

var toggleRestType = function() {
    if(restType === 'Constructive') {
        restType = 'Destructive';
    } else {
        restType = 'Constructive';
    }
}

var setRestAmount = function(val) {
    restAmount = val * 2;
    console.log(document.getElementById('content1').offsetHeight);
}

var setRhythmicImportance = function(val) {
    rhythmicImportance = val * 10;
}

var setMelodicImportance = function(val) {
    melodicImportance = val * 10;
}

var setSyncopation = function(val) {
    syncopation = val * 10;
}

var setPhraseLength = function(val) {
    phraseLength = val * 2;
}

var setCreativity = function(val) {
    creativity = val * 2;
}

var setSpeed = function(val) {
    speed = val * 10;
}

const grandPiano = 'javascripts/acoustic_grand_piano-mp3.js';
const acousticGuitar = 'javascripts/acoustic_guitar_nylon-mp3.js';
const flute = 'javascripts/flute-mp3.js';
const electricGuitar = 'javascripts/electric_guitar_clean-mp3.js';
const musicBox = 'javascripts/music_box-mp3.js';

var instrument = grandPiano;

var changeTempo = function(val) {
    tempo = val;
}

var play = function() {
    Player.play();
    document.getElementById('play-button').innerHTML = '<i class="fa fa-pause"></i>';
}

var changeNumComparisons = function(newVal) {
    numComparisons = newVal;
}

var toggleMusicSheet = function(val) {
    if(val) {
        ABCJS.renderAbc('notation', abcString, { add_classes: true });
        document.getElementById('vis-content4').style.display = 'block';
        document.getElementById('outputblock').style.display = 'none';
        document.getElementById('bigramvisualization').style.display = 'none';
        document.getElementById('canvasstatic').style.display = 'none';
        var h = document.getElementById('notation').style.height;
        var w = document.getElementById('notation').style.width;
        document.getElementById('vis-content4').style.height = h;
        document.getElementById('vis-content4').style.width = w;
        document.getElementById('vis-content4').style.backgroundColor = '#FFFFFF';
        document.getElementById('vis-content4').style.borderRadius = '5px';

        // var line0 = document.getElementById('notation').getElementsByClassName('abcjs-l0');
        // document.getElementById('notation').getElementsByClassName('abcjs-title')[0].setAttribute("fill", "#FFFFFF");
        // for (var i = 0; i < line0.length; i++) {
        //     line0[i].setAttribute("fill", "#FFFFFF");
        // }

        // document.getElementById('notation').querySelectorAll('.abcjs-m0').forEach((el) => {
        //     if (el.getAttribute("fill"))
        //         el.setAttribute("fill", "#3D9AFC");
        //     if (el.getAttribute("stroke"))
        //         el.setAttribute("stroke", "#3D9AFC");
        // });
    } else {
        document.getElementById('vis-content4').style.display = 'none';
        document.getElementById('outputblock').style.display = 'block';
        document.getElementById('bigramvisualization').style.display = 'block';
        document.getElementById('canvasstatic').style.display = 'block';
    }
}

var changeNgrams = function(newVal) {
    nGramVal = newVal;
}

var svgStatic = d3.select("#canvasstatic")
    .append("svg")
    .attr("width", width)
    .attr("height", height);

var svgBlock = d3.select("#canvasblock")
    .append("svg")
    .attr("width", '500%')
    .attr("height", height);

var svgBlock1 = d3.select("#canvasblock1")
    .append("svg")
    .attr("width", '500%')
    .attr("height", height);

var svgBlock2 = d3.select("#canvasblock2")
    .append("svg")
    .attr("width", '500%')
    .attr("height", height);

var svgBlock3 = d3.select("#canvasblock3")
    .append("svg")
    .attr("width", '500%')
    .attr("height", height);

var svgBlock4 = d3.select("#canvasblock4")
    .append("svg")
    .attr("width", '500%')
    .attr("height", height);

var color = d3.scale.linear().domain([45,75])
    .interpolate(d3.interpolateHcl)
    .range([d3.rgb("#007AFF"), d3.rgb('#FFF500')]);

var forge = function() {
    width = document.getElementById('containerid').offsetHeight;

    if(Player) {
        stop();
    }
    document.getElementById('play-bar').style.display = 'none';
    document.getElementById('loadingcube').style.display = 'block';


    if(document.getElementById('keysigdropdown').value === '') {

    } else {
        keySignature = document.getElementById('keysigdropdown').value;
    }

    if(document.getElementById('algorithmdropdown').value === '') {

    } else {
        algorithmChoice = document.getElementById('algorithmdropdown').value;
    }

    switch(corpus) {
        case 'corpus1':
            var corpora = {
                beatles: document.getElementById('beatles-slider-1').value,
                house: document.getElementById('house-slider-1').value
            };
            break;
        case 'corpus2':
            var corpora = {
                folk: document.getElementById('folks-slider-2').value,
                prosonic: document.getElementById('prosonic-slider-2').value
            };
            break;
        default:
            var corpora = {
                folk: document.getElementById('folks-slider-3').value,
                prosonic: document.getElementById('prosonic-slider-3').value,
                beatles: document.getElementById('beatles-slider-3').value,
                house: document.getElementById('house-slider-3').value
            };
    }

    var formData = {
        corpus: corpus,
        corpora: corpora,
        rhythmicImportance: rhythmicImportance,
        melodicImportance: melodicImportance,
        speed: speed,
        syncopation : syncopation,
        phraseLength : phraseLength,
        restAmount : restAmount,
        creativity : creativity,
        restType : restType,
        keySignature : keySignature,
        algorithmChoice : algorithmChoice
    };

    console.log(formData);

    //console.log(formData);
    $.ajax({
        url : '/getmidi',
        type: 'GET',
        data: formData,
        success : function(data) {
            initiatePlayer(data, instrumentText);

        }
    })
}

var switchInstrument = function(val) {
    var str = val;
    if(val === 'acousticGuitar') {
        instrument = acousticGuitar;
        str = 'Acoustic Guitar';
    }
    if(val === 'flute') {
        instrument = flute;
        str = 'Flute';
    }
    if(val === 'electricGuitar') {
        instrument = electricGuitar;
        str = 'Electric Guitar';
    }
    if(val === 'musicBox') {
        instrument = musicBox;
        str = 'Music Box';
    }if(val === 'grandPiano') {
        instrument = grandPiano;
        str = 'Grand Piano';
    }
    instrumentText = val;
    document.getElementById('instrument-menu').innerHTML = '<li>' + str +
        '                <ul class=\'sub-menu\'>\n' +
        '                    <li data-dropdown-value="acousticGuitar" onClick="changeInstrument(\'acousticGuitar\')">Acoustic Guitar</li>\n' +
        '                    <li data-dropdown-value="churchOrgan" onClick="changeInstrument(\'musicBox\')">Music Box</li>\n' +
        '                    <li data-dropdown-value="flute" onClick="changeInstrument(\'flute\')">Flute</li>\n' +
        '                    <li data-dropdown-value="grandPiano" onClick="changeInstrument(\'grandPiano\')">Grand Piano</li>\n' +
        '                    <li data-dropdown-value="electricGuitar" onClick="changeInstrument(\'electricGuitar\')">Electric Guitar</li>\n' +
        '                </ul>'
}

var playing = false;
var snd;
var sampleplayid;
function playSound(sample, id) {
    if(!playing) {

        snd = new Audio('samples/' + sample + ".mp3");
        snd.play();
        playing = true;
        document.getElementById(id).innerHTML = '<i class="fa fa-pause"></i>';

        sampleplayid = id;
    } else {
        if(snd) {
            if(id === sampleplayid) {
                snd.pause();
                playing = false;
                document.getElementById(id).innerHTML = '<i class="fa fa-play"></i>';
            } else {
                snd.pause();
                snd = new Audio('samples/' + sample + ".mp3");
                snd.play();
                playing = true;
                document.getElementById(id).innerHTML = '<i class="fa fa-pause"></i>';
                document.getElementById(sampleplayid).innerHTML = '<i class="fa fa-play"></i>';
                sampleplayid = id;
            }

        }
    }
}

var showControls = function() {
    document.getElementById('play-bar').style.display = 'block';
    document.getElementById('loadingcube').style.display = 'none';
    document.getElementById('canvasstatic').style.display = 'block';
    document.getElementById('tempo-div').style.display = 'inline-block';
    document.getElementById('tempo-slider').style.display = 'inline-block';
    document.getElementById('play-bar-button').style.display = 'block';
    document.getElementById('visualization').style.display = 'block';
    //document.getElementById('play-button').style.marginLeft = '32%';
    document.getElementById('play-button').style.display = 'inline-block';
    document.getElementById('restart-button').style.display = 'inline-block';
    document.getElementById('instrument-menu-div').style.display = 'inline-block';

}

function fillBiGrams() {
    if (typeof(window.fooreinit) == 'function') {
        document.getElementById('visualization').style.display = 'block';
        fooreinit(bigrams);
    }
}

var initiatePlayer = function(data, instrumentVal) {

    showControls();

    switchInstrument(instrumentVal);

    svgBlock.selectAll("*").remove();

    Soundfont.instrument(ac, instrument).then(function (instrument) {

        loadDataUri = function(dataUri) {
            var lastNoteEvent = null;
            var differenceInTicks = 0;
            Player = new MidiPlayer.Player(function(event) {

                if(event.name == 'Note off') {
                    // console.log(event);
                    differenceInTicks = event.delta;
                    instrument.stop();

                    //http://localhost/gist/audio/jesu/
                    // var elLength = 40*(event.delta<=1?1:event.delta);
                    var elLength = 6 * differenceInTicks;
                    var element = svgStatic.append("g");

                    element.attr("transform","translate("+(-1*elLength)+" 0)");
                    element.append("rect")
                        .attr("width", elLength * width / 960)
                        .attr("height", 20)
                        .attr("rx", 5)
                        .attr("ry", 5)
                        .attr("x", 0)
                        .attr("y", (event.noteNumber - 45)*12)
                        .attr("fill", color(event.noteNumber));

                    element.append("text")
                        .attr("x", 3)
                        .attr("y", 15 + (event.noteNumber - 45)*12)
                        .text(event.noteName)
                        .style('fill', 'white');

                    element.transition()
                        .ease('linear')
                        .duration(4000)
                        .attr("transform","translate("+(width+300-elLength)+" 0)")
                        .remove();
                    differenceInTicks = 0;

                }
                if (event.name == 'Note on' && event.velocity > 0) {
                    instrument.play(event.noteName, ac.currentTime);
                    //document.querySelector('#track-' + event.track + ' code').innerHTML = JSON.stringify(event);
                    //console.log(event);
                    lastNoteEvent = event;
                }

                if(Player.tempo > 99) {
                    document.getElementById('tempo-display').innerHTML = Player.tempo;
                    // document.getElementById('play-button').style.marginLeft = '29%';
                } else  {
                    document.getElementById('tempo-display').innerHTML = ' ' +Player.tempo;
                    //document.getElementById('play-button').style.marginLeft = '29.75%'
                }
                document.getElementById('play-bar').style.width = 100 - Player.getSongPercentRemaining() + '%';
            });

            Player.loadDataUri(dataUri);

            document.getElementById('play-button').removeAttribute('disabled');


            buildTracksHtml();
            play();
        }
        if(data.datauri) {
            songDataURI = data.datauri;
            bigrams = data.bigrams;
            abcString = data.abcString;
        }

        loadDataUri(songDataURI);

        // document.getElementById('text-area').innerText = '{{' + [[0,1],[1,0]] +'| json}}';
        //$scope.updateTransitionMatrix(bigrams);
        //angular.injector(['ng', 'myApp']).get("utils").setHash(bigrams);
        fillBiGrams(bigrams);
        toggleMusicSheet(false);
        Player.tempo = tempo;
       // if(Player.isPlaying()) { Player.pause();Player.setTempo(tempo);Player.play()} else {Player.setTempo(tempo)}
    });
}


var changeInstrument = function(instrumentval) {
    if(Player) {
        Player.stop();
        initiatePlayer(songDataURI, instrumentval);
    } else {
        switchInstrument(instrumentval);
    }
}

var pause = function() {
    Player.pause();
    document.getElementById('play-button').innerHTML = '<i class="fa fa-play"></i>';
}

var stop = function() {
    Player.stop();
    document.getElementById('play-button').innerHTML = '<i class="fa fa-pause"></i>';
}

var buildTracksHtml = function() {
    var offset = 0;
    var offset1 = 0;
    var offset2 = 0;
    var offset3 = 0;
    var offset4 = 0;

    Player.tracks[0].events.forEach(function(event, index) {
        //console.log(event);
        if(event.name.includes('Note off')) {
            // var trackDiv = document.createElement('div');
            // trackDiv.id = 'track-' + (index+1);
            // var h5 = document.createElement('h5');
            // h5.innerHTML = event.noteName + " " + event.delta;
            // trackDiv.appendChild(h5);
            // trackDiv.style.color = '#fffff';
            // eventsDiv.appendChild(trackDiv);


            //http://localhost/gist/audio/jesu/
            // var elLength = 40*(event.delta<=1?1:event.delta);

            var elLength = 12 * event.delta;
            var element = svgBlock.append("g");

            element.attr("transform","translate("+(-1*elLength)+" 0)");
            element.append("rect")
                .attr("width", elLength * width / 960)
                .attr("height", 20)
                .attr("rx", 5)
                .attr("ry", 5)
                .attr("x", event.delta + 60 + offset)
                .attr("y", (event.noteNumber - 45)*12)
                .attr("fill", color(event.noteNumber));

            element.append("text")
                .attr("x", event.delta + 60 + offset)
                .attr("y", 15 + (event.noteNumber - 45)*12)
                .text(event.noteName)
                .style('fill', 'white');
            offset += event.delta + 30;


        }

    });

    if(corpus == 'corpus1') {

    } else if(corpus == 'corpus2') {

    } else {

    }

}

var myApp = angular.module('myApp', []);

myApp.controller('MainCtrl', function($scope, utils, $window) {
    var mtx = {'C':[0,1], 'B1':[0.2,0.8]};
    angular.element($window).on('resize', function() { $scope.$apply(); });
    $scope.diagramCenter = [0.5, 0.5];
    $scope.isSelectedTransition = function(i, j) {
        return !!$scope.selectedTransition;
        if (!$scope.selectedTransition) return false;
        return $scope.selectedTransition[0] === i
            && $scope.selectedTransition[1] === j;
    };
    $scope.speedRange = 2;
    $scope.$watch('speedRange', function(speed) {
        $scope.duration = 2000 / +speed;
    });

    $scope.initFoo = function (obj) {
        // do angular stuff
        $scope.updateTransitionMatrix(obj);
    }
    var initialize = function (obj) {
        $scope.initFoo(obj);
    }

    //initialize();

    $window.fooreinit = initialize;

    $scope.updateTM = function() {
        var matrix = utils.getHash();
        $scope.updateTransitionMatrix(matrix);
    }

    $scope.updateTransitionMatrix = function(matrix) {
        var newMatrix = [];

        $scope.states = Object.keys(matrix).map(function(key, i) {
            //debugger;
            label= String.fromCharCode(65 + i);
            newMatrix.push(matrix[key]);
            return { label: key, index: i };
        });

        $scope.transitionMatrix = newMatrix;

    };

    // var hash = utils.getHash();
    // console.log(hash);
    // console.log(hash.tm);
    // if(hash && hash.tm) $scope.updateTransitionMatrix(hash.tm);
    //debugger;
    $scope.updateTransitionMatrix(mtx);

    $scope.transitionMatrixJSON = JSON.stringify($scope.transitionMatrix)
        .replace(/\],/gm, '],\n');
    // console.log($scope.transitionMatrixJSON);

});

myApp.factory('utils', function() {
    var utils = {};
    utils.getHash = function() {
        var hash = window.location.hash;
        if(!hash) return;
        hash = hash.slice(1); // remove the '#'
        try { return JSON.parse(decodeURIComponent(hash)); }catch(e) {};
    };
    utils.setHash = function(obj) {
        window.location.hash = encodeURIComponent(JSON.stringify(obj));
    };
    utils.sample = function(probs) {
        var t = 0;
        var r = Math.random();
        for(var i = 0; i < probs.length; i++) {
            t = t + probs[i];
            if (r <= t) return i;
        }
        throw new Error('invalid distribution');
    };
    utils.normalizeTransitionMatrix = function(matrix, idx1, idx2) {
        // The next states this state will transition to.
        var states = matrix[idx1];

        // Convert to numbers.
        states.forEach(function(d, i){ states[i] = +d; });

        // We need to re-normalize the transitions for each state so that they
        // add to one.

        // `val` - The selected next state value.
        var val = states[idx2];

        if(val === 1) return states.forEach(function(d, i) {
            if(i === idx2) return;
            states[i] = 0;
        });

        // `r` - The remaining state probability.
        var r = states.reduce(function(total, state, i){
            return total + (i === idx2 ? 0 : state);
        }, 0);

        if(r === 0) r = states.length - 1, states.forEach(function(d, i) {
            if(i === idx2) return;
            states[i] = 1;
        });

        // normalize the remaining states and then multiply by the remaining
        // probability, `( 1 - val)`.
        states.forEach(function(d, i) {
            if(i === idx2) return;
            states[i] = states[i] / r * (1 - val);
        });
    };
    return utils;
});

myApp.directive('stDiagram', function($compile) {
    function link(scope, el, attr) {
        //console.log(attr);
        el = d3.select(el[0]);
        calcResize();
        var svg = el.select('svg');
        var alignG = svg.append('g');
        var centerG = alignG.append('g');
        var color = d3.scale.category10();
        var links = centerG.append('g').attr('class', 'links').selectAll('paths');
        var nodes = centerG.append('g').attr('class', 'nodes').selectAll('g');
        var markers = svg.append('defs').selectAll('.linkMarker');
        var currentStateG = centerG.append('g').attr('class', 'currentState')
            .attr('transform', 'translate(' + [w / 2, h / 2] + ')')
            .style('opacity', 0);
        var w, h, r = 20;
        var linkElements = {};
        var force = d3.layout.force()
            .linkDistance(function(d){ return w / 16 + (1 - d.value) * 200 * w / 1200 })
            .charge(-4000);

        currentStateG
            .append('circle')
            .attr('r', 10);

        function calcResize() {
            return w = el.node().clientWidth, h = el.node().clientHeight, w + h;
        }
        scope.$watch(calcResize, resize);
        scope.$watch('center', resize, true);
        scope.$watch('states', update, true);
        scope.$watch('transitionMatrix', update, true);
        scope.$watch('selectedTransition', update);

        function resize() {
            force.size([w, h]);
            svg.attr({width: w, height: h});
            var center = scope.center;
            var cx = (center && angular.isDefined(center[0])) ? center[0] : 0.5;
            var cy = (center && angular.isDefined(center[1])) ? center[1] : 0.5;
            alignG.attr('transform', 'translate(' + [ w * cx, h * cy ] + ')');
            centerG.attr('transform', 'translate(' + [ - w / 2, - h / 2] + ')');
        }

        function update() {
            var linksData = [];
            var enter;
            scope.transitionMatrix.forEach(function(transitions, idx1) {
                // idx1 - the index of the currently state
                // transitions - an array of the next state probabilities were
                // each index in the array coorisponds to a state in `scope.states`.
                transitions.forEach(function(prob, idx2) {
                    if(prob === 0) return;
                    linksData.push({
                        source: scope.states[idx1],
                        target: scope.states[idx2],
                        value: +prob
                    });
                });
            });
            nodes = nodes.data(scope.states);
            enter = nodes.enter().append('g')
                .attr('class', 'node')
                .style('fill', function(d){ return color(d.index); })
                .call(force.drag);
            enter.append('circle')
                .attr('r', r);
            enter.append('text')
                .attr('transform', 'translate(' + [0, 5] + ')')
            nodes.exit().remove();

            var linkKey = function(d) {
                return (d.source && d.source.index) + ':'
                    + (d.target && d.target.index);
            };
            links = links.data(linksData, linkKey)
            links.enter().append('path')
                .attr('marker-end', function(d) {
                    if(!d.source || !d.target) debugger;
                    return 'url(#linkMarker-' + d.source.index + '-' + d.target.index + ')';
                }).classed('link', true)
                .style('stroke', function(d){ return color(d.source.index); })
            links.exit().remove();
            links.each(function(d, i) {
                linkElements[d.source.index + ':' +d.target.index] = this;
                var active = false, inactive = false;
                if (scope.selectedTransition) {
                    active = scope.selectedTransition[0] === d.source.index
                        && scope.selectedTransition[1] === d.target.index;
                    inactive = !active;
                }
                d3.select(this)
                    .classed('active', active)
                    .classed('inactive', inactive);
            });

            markers = markers.data(linksData, linkKey);
            markers.enter().append('marker')
                .attr('class', 'linkMarker')
                .attr('id', function(d) {
                    return 'linkMarker-' + d.source.index + '-' + d.target.index })
                .attr('orient', 'auto')
                .attr({markerWidth: 2, markerHeight: 4})
                .attr({refX: 0, refY: 2})
                .append('path')
                .attr('d', 'M0,0 V4 L2,2 Z')
                .style('fill', function(d){ return color(d.source.index); });
            markers.exit().remove();

            force.nodes(scope.states)
                .links(linksData)
                .start();
        }

        force.on('tick', function() {
            var _r = r;
            links
                .style('stroke-width', function(d) {
                    return Math.sqrt(100 * d.value || 2); })
                .attr('d', function(d) {
                    var r = _r;
                    var p1 = vector(d.source.x, d.source.y);
                    var p2 = vector(d.target.x, d.target.y);
                    var dir = p2.sub(p1);
                    var u = dir.unit();
                    if(d.source !== d.target) {
                        r *= 2;
                        var right = dir.rot(Math.PI /2).unit().scale(50);
                        var m = p1.add(u.scale(dir.len() / 2)).add(right);
                        u = p2.sub(m);
                        l = u.len();
                        u = u.unit();
                        p2 = m.add(u.scale(l - r));
                        u = p1.sub(m);
                        l = u.len();
                        u = u.unit();
                        p1 = m.add(u.scale(l - r));
                        return 'M' + p1.array() + 'S' + m.array() + ' ' + p2.array();
                    }else{
                        var s = 50, rot = Math.PI / 8;
                        r = r * 1.5;
                        p1 = p1.add(vector(1, -1).unit().scale(r - 10))
                        p2 = p2.add(vector(1, 1).unit().scale(r))
                        var c1 = p1.add(vector(1, 0).rot(-rot).unit().scale(s));
                        var c2 = p2.add(vector(1, 0).rot(rot).unit().scale(s - 10));
                        return 'M' + p1.array() + ' C' + c1.array() + ' '
                            + c2.array() + ' ' + p2.array();
                    }
                });
            nodes.attr('transform', function(d) {
                return 'translate(' + [d.x, d.y] + ')';
            }).select('text').text(function(d){ return d.label; })
        });

        var currentState = 0;
        function loop() {
            var i = currentState;
            var nextStates = scope.transitionMatrix[i];
            var nextState = -1;
            var rand = Math.random();
            var total = 0;
            for(var j = 0; j < nextStates.length; j++) {
                total += nextStates[j];
                if(rand < total) {
                    nextState = j;
                    break;
                }
            }
            var cur = scope.states[currentState];
            var next = scope.states[nextState];
            var path = linkElements[cur.index + ':' + next.index];
            scope.$apply(function() {
                scope.$emit('stateChange', next);
            });
            currentStateG
                .transition().duration(+scope.duration * 0.25)
                .style('opacity', 1)
                .ease('cubic-in')
                .attrTween('transform', function() {
                    var m = d3.transform(d3.select(this).attr('transform'));
                    var start = vector.apply(null, m.translate);
                    var scale = m.scale;
                    var s = d3.interpolateArray(scale, [1, 1]);
                    return function(t) {
                        var end = path.getPointAtLength(0);
                        end = vector(end.x, end.y);
                        var p = start.add(end.sub(start).scale(t));
                        return 'translate(' + p.array() + ') scale(' + s(t) + ')';
                    };
                })
                .attrTween('transform', function() {
                    //var l = path.getTotalLength();
                    return function(t) {
                        // var p = path.getPointAtLength(t * l);
                        // return 'translate(' + [p.x, p.y] + ') scale(1)';
                    };
                })
                .transition().duration(+scope.duration * 0.25)
                .attrTween('transform', function() {
                    // var m = d3.transform(d3.select(this).attr('transform'));
                    // var translation = vector.apply(null, m.translate);
                    // var scale = m.scale;
                    // var s = d3.interpolateArray(scale, [2, 2]);
                    // return function(t) {
                    //     var end = vector(next.x, next.y);
                    //     var p = translation.add(end.sub(translation).scale(t));
                    //     return 'translate(' + p.array() + ') scale(' + s(t) + ')';
                    // };
                })
                .each('end', function() {
                    loop();
                })
            currentState = nextState;
        }
        setTimeout(loop, +scope.duration);
    }
    return {
        link: link,
        restrict: 'E',
        replace: true,
        scope: {
            states: '=',
            center: '=',
            transitionMatrix: '=',
            duration: '=',
            selectedTransition: '=',
            state: '=?'
        },
        template: ''
        + '<div class="st-diagram">'
        + '<svg>' + '</svg>'
        + '</div>'
    };
});


//ajaxResultPost('monkey', 'type', 'res');
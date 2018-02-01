import React, { Component } from 'react';

class Playbar extends Component {
    render() {
        return (
            <div>
                <p>Hit forge when you are ready to listen to some new music!</p>
                    <div className="playbarborder">
                    <div className="playbar">
                    </div>
                </div>
                <p>
                    <button className="button button-primary" id="play-button" disabled="disabled">Play</button>
                    <button className="button">Stop</button>
                    <button className="button button-primary" id="forge-button">Forge</button>
                </p>
                <p>
                    Tempo: <span id="tempo-display"></span> bpm<br />
                    <input type="range" min="50" max="200" />
                    <br />
                </p>
                <div id="events" class="well"></div>
            </div>

        );
    }

}

export default Playbar;
//
// <p id="loading">Hit forge when you are ready to listen to some new music!</p>
// <div style="border:1px solid #ccc;margin-bottom:12px;background:#f7f7f7;">
//     <div id="play-bar" style="height:20px;background:#33C3F0;width:0%;"></div>
// </div>
// <p>
//     <button class="button button-primary" id="play-button" onclick="Player.isPlaying() ? pause() : play();" disabled="disabled">Play</button>
//     <button class="button" onclick="stop()">Stop</button>
//     <button class="button button-primary" onClick="forge()" id="forge-button">Forge</button>
//
//     </p>
//     <p>
//         Tempo: <span id="tempo-display"></span> bpm<br />
//         <input type="range" onchange="Player.pause();Player.setTempo(this.value);Player.play()" min="50" max="200">
//             <br />
//     </p>
//     <div id="events" class="well"></div>
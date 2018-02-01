import React, { Component } from 'react';

class ArtistsBlock extends Component {

    constructor(props) {
        super(props);

        this.state = {
            artist1slider: '',
            artist2slider: '',
            artist3slider: ''
        };
    }

    onArtist1SliderChange(artist1slider) {
        this.setState({artist1slider});
    }

    onArtist2SliderChange(artist2slider) {
        this.setState({artist2slider});
    }

    onArtist3SliderChange(artist3slider) {
        this.setState({artist3slider});
    }

    render() {
        return (
            <div>
                <h4>Artists</h4>
                <p>Backstreet Boys</p>
                <input type="range" onChange={event => this.onArtist1SliderChange(event.target.value)} id="artist-slider1" min="0" max="100" />
                <p>Katy Perry</p>
                <input type="range" onChange={event => this.onArtist2SliderChange(event.target.value)} id="artist-slider2" min="0" max="100" />
                <p>Beethoven</p>
                <input type="range" onChange={event => this.onArtist3SliderChange(event.target.value)} id="artist-slider3" min="0" max="100" />
            </div>
        );
    }
}

export default ArtistsBlock;

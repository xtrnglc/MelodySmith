import React, { Component } from 'react';

class ArtistBlock extends Component {
    constructor(props) {
        super(props);

        this.state = {
            artistslider1: '50',
            artistslider2: '50',
            artistslider3: '50'
        };
    }

    render() {
        return (

            <div>
                <h4>Artists</h4>
                <div className="tooltip">Justin Bieber
                    <span className="tooltiptext">Give this artist more influence in the composition</span>
                </div>
                <p> </p>
                <input type="range" id="artist-slider1" min="0" max="100" value={this.state.term} onChange={event => this.onInputChangeArtist1(event.target.value)} />
                <p>Bach</p>
                <input type="range" id="artist-slider2" min="0" max="100" value={this.state.term} onChange={event => this.onInputChangeArtist2(event.target.value)} />
                <p>Beatles</p>
                <input type="range" id="artist-slider3" min="0" max="100" value={this.state.term} onChange={event => this.onInputChangeArtist3(event.target.value)} />
            </div>
        );
    }

    onInputChangeArtist1(artistslider1) {
        this.setState({artistslider1}, () => {
            this.props.onSliderChange(this.state);
        });
    }
    onInputChangeArtist2(artistslider2) {
        this.setState({artistslider2}, () => {
            this.props.onSliderChange(this.state);
        });
    }
    onInputChangeArtist3(artistslider3) {
        this.setState({artistslider3}, () => {
            this.props.onSliderChange(this.state);
        });
    }
}

export default ArtistBlock;

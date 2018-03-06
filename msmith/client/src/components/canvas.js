import React, { Component } from 'react';
import d3 from 'react-d3';
import select from 'd3-selection';
import shape from 'd3-shape';
import scale, {scaleLinear} from 'd3-scale';
import color, {rgb} from 'd3-color';
import interpolate, {interpolateHcl} from 'd3-interpolate';
import transition from 'd3-transition';

class Canvas extends Component {
    constructor(props) {
        super(props);



       console.log(svg);
        this.state = {
            width: '960',
            height: '400'
        };
    }

    renderNote(event) {
        var svg = select("#canvas")
            .append("svg")
            .attr("width", width)
            .attr("height", height);

        var color = scaleLinear().domain([45,75])
            .interpolate(interpolateHcl)
            .range([rgb("#007AFF"), rgb('#FFF500')]);

       // document.buttonPress();
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


        var t = transition()
            .duration(4000)
            .ease(d3.easeLinear);
        element.transition(t)
            .attr("transform","translate("+(width+300-elLength)+" 0)")
            .remove();
        console.log(element);

        return element;
    }




    render() {
        return (
            <div>
                {this.renderNote(this.props.event)}
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

export default Canvas;

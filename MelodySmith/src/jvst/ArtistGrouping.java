/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jvst;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Daniel Mattheiss
 */
public class ArtistGrouping {
    public String artistName;
    public double artistInfluence;
    public ArrayList<File> artistMIDIFiles = null;
    public ArtistGrouping(String artistName, double artistInfluence, ArrayList<File> artistMIDIFiles) {
        this.artistName = artistName;
        this.artistInfluence = artistInfluence;
        this.artistMIDIFiles = artistMIDIFiles;
    }
}
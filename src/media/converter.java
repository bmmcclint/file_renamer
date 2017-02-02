/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package media;

import com.mpatric.mp3agic.BaseException;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 *
 * @author Brandon McClinton
 */
public class converter {
  public ArrayList<String> stuff = new ArrayList<>();

  public static void main(String[] args) {
    fileList(new File(args[0]));
  }

  public static void fileList(File file) {
    for (File f : file.listFiles()) {
      if (f.isDirectory()) {
        fileList(f);
        System.out.println(f);
      } else if (f.getName().endsWith(".mp3")) {
        getID3tags(f);
      }
    }
  }

  public static void getID3tags(File file) {
    String filePath = file.getAbsolutePath();
    try (FileInputStream potsong = new FileInputStream(file)) {
      Mp3File song = new Mp3File(filePath);
      if (song.hasId3v2Tag()) {
        ID3v2 tag = song.getId3v2Tag();
        String artist = tag.getArtist();
        System.out.println("Artist: " + artist.replaceAll("/", " "));
        String title = tag.getTitle();
        System.out.println("Title: " + title.replaceAll("()*-+=/", " "));
        String albArt = tag.getAlbumArtist();
        System.out.println("Album Artist: " + albArt.replaceAll("()*-+=/", " ") + "\n");
        renameSong(file, artist, title, albArt);
      }
    } catch (IOException | BaseException e) {
    }
  }

  public static void renameSong(File file, String title, String artist, String albArt) {
    String path = "/Users/bmcclint/Desktop/Renamed_Tracks/";
    try {
      FileInputStream renamed = new FileInputStream(file);
      file.renameTo(new File(path + title + " - " + artist + ".mp3"));
//        System.out.println(file);
      
    } catch (IOException e) {
    }
  }
  
  public static void stringTokenizer(String title, String artist, String albArt) {
    StringTokenizer str = new StringTokenizer(artist, "()+-*/");
    while (str.hasMoreTokens()) {
      System.out.println(str.nextToken());
    }
  }  
}

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

/**
 *
 * @author Brandon McClinton
 */
public class converter {

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
        System.out.println("Artist: " + artist);
        String title = tag.getTitle();
        System.out.println("Title: " + title);
        String albArt = tag.getAlbumArtist();
        System.out.println("Album Artist: " + albArt + "\n");
        if (artist.contains("/") || title.contains("/")) {
          replaceTag(file, artist, title, filePath);
        }
        else {
          renameSong(file, artist, title, albArt);
        }

      } else if (!song.hasId3v2Tag()) {
        System.out.println("Dig deeper! \n");
      }
      potsong.close();
    } catch (IOException | BaseException e) {
    }
  }

  private static void replaceTag(File file, String artist, String title, String filePath) {
    try (FileInputStream newTags = new FileInputStream(file)) {
      Mp3File song = new Mp3File(filePath);
      ID3v2 tag = song.getId3v2Tag();
      if (artist.contains("/") || title.contains("/")) {
        String artistReplace = artist.replace("/", " ");
        tag.setArtist(artistReplace);
        System.out.println("New Artitst: " + artistReplace);
        
        String titleReplace = title.replace("/", " ");
        tag.setTitle(titleReplace);
        System.out.println("New Title: " + titleReplace + "\n");
        
        newTags.close();
        renameSongNewTags(file, artistReplace, titleReplace);
      }
    } catch (IOException | BaseException e) {

    }
  }

  public static void renameSong(File file, String title, String artist, String albArt) {
    String path = "/Users/bmcclint/Desktop/Renamed_Tracks/";
    try (FileInputStream renamed = new FileInputStream(file)) {
      if (artist != null) {
        file.renameTo(new File(path + title + " - " + artist + ".mp3"));
        System.out.println(file.getName());
      }
      else if (artist == null) {
        file.renameTo(new File(path + albArt + " - " + title + ".mp3"));
      }
      renamed.close();
    } catch (IOException e) {
    }
  }
  
  public static void renameSongNewTags(File file, String artistReplace, String titleReplace) {
    String path = "/Users/bmcclint/Desktop/Renamed_Tracks/";
    try (FileInputStream newTagsName = new FileInputStream(file)) {
      file.renameTo(new File(path + artistReplace + " - " + titleReplace + ".mp3"));
      System.out.println(file.getName());
      newTagsName.close();
    }
    catch (IOException e) {}
  }
}

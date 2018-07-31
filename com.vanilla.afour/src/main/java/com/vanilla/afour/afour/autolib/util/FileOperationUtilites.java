package com.vanilla.afour.afour.autolib.util;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by siddharth.p on 12/21/2016.
 */
public class FileOperationUtilites {
    /**
     * This method will return absolute file path from the parameter input.
     * pseudoFilePath formath: 'PROJECT_HOME:[FileName/FolderName :].*?[FileName].ext]{1}
     *
     * @param pseudoFilePath
     * @return
     */
    public String getFilePath(String pseudoFilePath) {
        String absFilePath = null;
        String projectHomeDir = System.getProperty("user.dir");
        absFilePath = projectHomeDir;
        String[] folderNames = pseudoFilePath.split(":");
        for (String name : folderNames) {
            absFilePath += System.getProperty("file.separator") + name;
        }

        return absFilePath;
    }
    /*
    public static void main(String[] args){
		System.out.println(new FileOperationUtilites().getFilePath("ElementRepository:mobile:landingScreen.xml"));
	}
	*/

    /**
     * This method will return base64Binary from the parameter input.
     * pseudoFilePath formath: 'PROJECT_HOME:[FileName/FolderName :].*?[FileName].ext]{1}
     *
     * @param pseudoFilePath
     * @return
     */
    public String encodeImageToBase64Binary(String fileName) {
        File file = new File(fileName);
        String imageDataString = null;
        try {
            // Reading a Image file from file system
            FileInputStream imageInFile = new FileInputStream(file);
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
            imageDataString = Base64.encodeBase64URLSafeString(imageData);
            imageInFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        } catch (Exception ex) {
            System.out.println("Exception while converting the Image to Base64Binary" + ex);
        }
        return imageDataString;
    }
}

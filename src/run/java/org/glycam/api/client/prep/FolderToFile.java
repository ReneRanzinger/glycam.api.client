package org.glycam.api.client.prep;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Reads a folder with glycam files (GlyTouCan ID is the file name) and writes
 * them into a single file.
 *
 * @author Rene Ranzinger
 *
 */
public class FolderToFile
{

    public static void main(String[] args) throws IOException
    {
        // folder with the glycam files
        String t_folder = "./data/glycam_iupac/";
        // target file
        PrintWriter t_writer = new PrintWriter("./data/2021.08.20.glygen.txt");
        // get all files in the folder
        File t_fileFolder = new File(t_folder);
        String[] t_files = t_fileFolder.list();
        // for each file
        for (String t_fileName : t_files)
        {
            String t_fileContent = FolderToFile.readFile(t_folder + t_fileName,
                    StandardCharsets.UTF_8);
            String t_glyTouCanId = t_fileName.substring(0, t_fileName.length() - 4);
            t_writer.println(t_glyTouCanId + ":" + t_fileContent.trim());
        }
        t_writer.flush();
        t_writer.close();
    }

    static String readFile(String t_path, Charset a_encoding) throws IOException
    {
        byte[] t_bytes = Files.readAllBytes(Paths.get(t_path));
        return new String(t_bytes, a_encoding);
    }
}

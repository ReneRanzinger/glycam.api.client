package org.glycam.api.client;

import java.io.IOException;

public class AppRun
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        String t_comandLine = "-g ./data/2022.04.10.glycan.csv -o ./data/output/ -v";
        t_comandLine = "-j ./data/output/jobs.old.json -o ./data/output/ -v";
        String[] t_args = t_comandLine.split(" ");
        App.main(t_args);
    }
}

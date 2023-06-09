package org.glycam.api.client;

import java.io.IOException;

public class AppRun
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        String t_comandLine = "-g ./data/2023.05.18.glycan.csv -o ./data/output/ -b https://glycam.org -v -e";
        // t_comandLine = "-j ./data/output/2022.04.30.jobs.json -o
        // ./data/output/ -v";
        String[] t_args = t_comandLine.split(" ");
        App.main(t_args);
    }
}

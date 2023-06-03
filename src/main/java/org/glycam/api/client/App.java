package org.glycam.api.client;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.glycam.api.client.csv.CSVFileWriterError;
import org.glycam.api.client.csv.CSVFileWriterStatus;
import org.glycam.api.client.csv.CSVFileWriterWarning;
import org.glycam.api.client.csv.SequenceFileParser;
import org.glycam.api.client.json.GlycamJobSerializer;
import org.glycam.api.client.om.GlycamJob;
import org.glycam.api.client.om.Warning;
import org.glycam.api.client.util.GlycamUtil;

public class App
{
    private static final String PDB_FOLDER_NAME = "pdb";

    public static void main(String[] a_args)
    {
        // parse the command line arguments and store them
        Options t_options = App.buildComandLineOptions();
        AppArguments t_arguments = App.processCommandlineArguments(a_args, t_options);
        if (t_arguments == null)
        {
            // error messages and been printed already
            App.printComandParameter(t_options);
            return;
        }
        long t_startTime = System.currentTimeMillis();
        // load the jobs
        List<GlycamJob> t_jobs = new ArrayList<>();
        try
        {
            if (t_arguments.getGlycanFileNamePath() != null)
            {
                SequenceFileParser t_csvParser = new SequenceFileParser();
                t_jobs = t_csvParser.loadFile(t_arguments.getGlycanFileNamePath());
            }
            else
            {
                t_jobs = GlycamJobSerializer.deserialize(t_arguments.getGlycanDumpFileNamePath());
                GlycamJobSerializer.prepForRerun(t_jobs);
            }
        }
        catch (Exception e)
        {
            System.out.println("Critical error while loading input files: " + e.getMessage());
            e.printStackTrace(System.out);
            return;
        }
        // create the folders
        try
        {
            App.createFolders(t_arguments.getOutputFolder());
        }
        catch (Exception e)
        {
            System.out.println("Failed to create output folders: " + e.getMessage());
            return;
        }
        // process the jobs
        String t_filePrefix = App.getFilePrefix();
        try
        {
            GlycamUtil t_util = new GlycamUtil(
                    t_arguments.getOutputFolder() + File.separator + PDB_FOLDER_NAME,
                    t_arguments.getMaxWaitingTime(), t_arguments.getPollingSleepTime(),
                    t_arguments.getMaxQueueLength(), t_arguments.isVerbose(),
                    t_arguments.getGlycamBaseUrl(), t_arguments.getOutputFolder(), t_filePrefix);
            t_util.process(t_jobs);
            // try the timeouts one more time
            t_util.reTestTimeout(t_jobs);
        }
        catch (Exception e)
        {
            System.out.println("Critical error while processing Glycam Jobs: " + e.getMessage());
            e.printStackTrace(System.out);
            return;
        }
        // store the jobs
        try
        {
            GlycamJobSerializer.serialize(t_jobs, t_arguments.getOutputFolder() + File.separator
                    + t_filePrefix + "-final.jobs.json");
        }
        catch (Exception e)
        {
            System.out.println(
                    "Processing finished! However, there was a critical error while sericalizing the jobs: "
                            + e.getMessage());
            e.printStackTrace(System.out);
        }
        // save the errors
        try
        {
            CSVFileWriterError t_errorLog = new CSVFileWriterError(t_arguments.getOutputFolder()
                    + File.separator + t_filePrefix + "-final.error-log.csv");
            for (GlycamJob t_glycamJob : t_jobs)
            {
                String t_status = t_glycamJob.getStatus();
                if (!t_status.equals(GlycamJob.STATUS_SUCCESS)
                        && !t_status.equals(GlycamJob.STATUS_INIT)
                        && !t_status.equals(GlycamJob.STATUS_PDB_EXIST))
                {
                    t_errorLog.writeError(t_glycamJob);
                }
            }
            t_errorLog.closeFile();
            // write warning
            CSVFileWriterWarning t_warningLog = new CSVFileWriterWarning(
                    t_arguments.getOutputFolder() + File.separator + t_filePrefix
                            + "-final.warning-log.csv");
            for (GlycamJob t_glycamJob : t_jobs)
            {
                for (Warning t_warning : t_glycamJob.getWarnings())
                {
                    t_warningLog.writeWarning(t_glycamJob, t_warning);
                }
            }
            t_warningLog.closeFile();
            // write status
            CSVFileWriterStatus t_statusLog = new CSVFileWriterStatus(t_arguments.getOutputFolder()
                    + File.separator + t_filePrefix + "-final.status-log.csv");
            for (GlycamJob t_glycamJob : t_jobs)
            {
                t_statusLog.writeStatus(t_glycamJob);
            }
            t_statusLog.closeFile();
        }
        catch (Exception e)
        {
            System.out.println(
                    "Processing finished! However, there was a critical error while writing errors and warnings: "
                            + e.getMessage());
            e.printStackTrace(System.out);
        }
        Long t_durationMinutes = (System.currentTimeMillis() - t_startTime) / 60000;
        System.out.println("Finished after " + t_durationMinutes.toString() + " minutes");
    }

    private static String getFilePrefix()
    {
        Date t_date = new Date(System.currentTimeMillis());
        SimpleDateFormat t_formatter = new SimpleDateFormat("yyyy.MM.dd");
        return t_formatter.format(t_date);
    }

    private static void createFolders(String a_outputFolder)
    {
        File t_file = new File(a_outputFolder + File.separator + PDB_FOLDER_NAME);
        t_file.mkdirs();
    }

    /**
     * Process the command line options and create the AppArgument object.
     *
     * If the processing failed the error messages and command line options have
     * been printed.
     *
     * @param a_args
     *            Command line arguments given by the user
     * @param a_options
     *            Configuration object for the command line parameters
     * @return AppArguments object with the extracted command line options or
     *         NULL if parsing/validation failed. In this case error messages
     *         and valid command line options have been printed to System.out.
     */
    private static AppArguments processCommandlineArguments(String[] a_args, Options a_options)
    {
        // initialize the arguments from command line
        AppArguments t_arguments = null;
        try
        {
            t_arguments = App.parseArguments(a_args, a_options);
            if (t_arguments == null)
            {
                // failed, message was printed, time to go
                return null;
            }
        }
        catch (ParseException e)
        {
            System.out.println("Invalid commandline arguments: " + e.getMessage());
            return null;
        }
        catch (Exception e)
        {
            System.out.println(
                    "There was an error processing the command line arguments: " + e.getMessage());
            return null;
        }
        return t_arguments;
    }

    /**
     * Parse the command line parameters or load the values from a properties
     * file. Values are validated as well.
     *
     * @param a_args
     *            Command line parameters handed down to the application.
     * @return Validated parameter object or null if loading/validation fails.
     *         In that case corresponding error message are printed to console.
     * @throws ParseException
     *             Thrown if the command line parsing fails
     */
    private static AppArguments parseArguments(String[] a_args, Options a_options)
            throws ParseException
    {
        // create the command line parser
        CommandLineParser t_parser = new DefaultParser();
        // parse the command line arguments
        CommandLine t_commandLine = t_parser.parse(a_options, a_args);
        AppArguments t_arguments = new AppArguments();
        // set from arguments
        t_arguments.setGlycanFileNamePath(t_commandLine.getOptionValue("g"));
        t_arguments.setGlycanDumpFileNamePath(t_commandLine.getOptionValue("j"));
        t_arguments.setOutputFolder(t_commandLine.getOptionValue("o"));
        String t_value = t_commandLine.getOptionValue("w");
        if (t_value != null)
        {
            try
            {
                Long t_time = Long.parseLong(t_value);
                t_arguments.setMaxWaitingTime(t_time);
            }
            catch (Exception e)
            {
                System.out.println("Waiting time (-w) is expecting a number. But got: " + t_value);
                return null;
            }
        }
        t_value = t_commandLine.getOptionValue("s");
        if (t_value != null)
        {
            try
            {
                Long t_time = Long.parseLong(t_value);
                t_arguments.setPollingSleepTime(t_time);
            }
            catch (Exception e)
            {
                System.out.println("Polling time (-s) is expecting a number. But got: " + t_value);
                return null;
            }
        }
        t_value = t_commandLine.getOptionValue("q");
        if (t_value != null)
        {
            try
            {
                Integer t_length = Integer.parseInt(t_value);
                t_arguments.setMaxQueueLength(t_length);
            }
            catch (Exception e)
            {
                System.out.println("Queue length (-q) is expecting a number. But got: " + t_value);
                return null;
            }
        }
        t_value = t_commandLine.getOptionValue("b");
        if (t_value != null)
        {
            t_arguments.setGlycamBaseUrl(t_value);
        }
        t_arguments.setVerbose(t_commandLine.hasOption("v"));
        // check settings
        if (!App.checkArguments(t_arguments))
        {
            return null;
        }
        return t_arguments;
    }

    /**
     * Check the command line arguments.
     *
     * @param a_arguments
     *            Argument object filled with the parsed command line parameters
     * @return TRUE if the parameters are valid. FALSE if at least one parameter
     *         is incorrect. In that case a message is printed to System.out
     */
    private static boolean checkArguments(AppArguments a_arguments)
    {
        boolean t_valid = true;
        // input file
        if (a_arguments.getGlycanDumpFileNamePath() != null)
        {
            if (a_arguments.getGlycanFileNamePath() != null)
            {
                System.out.println("Only one input file option (-g or -j) can be used at a time.");
                t_valid = false;
            }
            else
            {
                File t_file = new File(a_arguments.getGlycanDumpFileNamePath());
                if (t_file.exists())
                {
                    if (!t_file.isFile())
                    {
                        System.out.println("Job file (-j) is not a file: "
                                + a_arguments.getGlycanDumpFileNamePath());
                        t_valid = false;
                    }
                }
                else
                {
                    System.out.println("Job file (-j) does not exist: "
                            + a_arguments.getGlycanDumpFileNamePath());
                    t_valid = false;
                }
            }
        }
        else if (a_arguments.getGlycanFileNamePath() != null)
        {
            File t_file = new File(a_arguments.getGlycanFileNamePath());
            if (t_file.exists())
            {
                if (!t_file.isFile())
                {
                    System.out.println("Glycan file (-g) is not a file: "
                            + a_arguments.getGlycanFileNamePath());
                    t_valid = false;
                }
            }
            else
            {
                System.out.println(
                        "glycan file (-g) does not exist: " + a_arguments.getGlycanFileNamePath());
                t_valid = false;
            }
        }
        else
        {
            System.out.println("An input file option (-g or -j) needs to be provided.");
            t_valid = false;
        }
        // output folder
        if (a_arguments.getOutputFolder() != null)
        {
            File t_file = new File(a_arguments.getOutputFolder());
            if (!t_file.exists())
            {
                if (!t_file.mkdirs())
                {
                    System.out.println(
                            "Unable to create output folder: " + a_arguments.getOutputFolder());
                    t_valid = false;
                }
            }
        }
        else
        {
            System.out.println("Output folder (-o) is missing.");
            t_valid = false;
        }
        // waiting time
        if (a_arguments.getMaxWaitingTime() <= 0)
        {
            System.out.println(
                    "Waiting time (-w) has to be a number greater than 0. Default 600000 milliseconds.");
            t_valid = false;
        }
        // polling sleep time
        if (a_arguments.getPollingSleepTime() <= 2000)
        {
            System.out.println(
                    "Polling sleep time (-s) has to be a number greater than 2000 milliseconds. Default 3000 milliseconds");
            t_valid = false;
        }
        // queue length
        if (a_arguments.getMaxQueueLength() <= 0)
        {
            System.out.println(
                    "Maximum queue length (-q) has to be a number greater than 0. Default 5.");
            t_valid = false;
        }
        // base URL
        String t_url = a_arguments.getGlycamBaseUrl();
        if (t_url.startsWith("http://") || t_url.startsWith("https://"))
        {
            if (t_url.endsWith("/"))
            {
                System.out.println(
                        "Base URL (-b) should not end with '/'. Default https://glycam.org.");
                t_valid = false;
            }
        }
        else
        {
            System.out.println(
                    "Base URL (-b) needs to start with 'http://' or 'https://'. Default https://glycam.org.");
            t_valid = false;
        }
        return t_valid;
    }

    /**
     * Print out the command line parameter.
     */
    private static void printComandParameter(Options a_options)
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("<command> -g <glycanFile> -o <OutputFolder>", a_options);
    }

    /**
     * Build the command line argument object that contains all options
     *
     * @return Object with the command line options
     */
    private static Options buildComandLineOptions()
    {
        // create the Options
        Options t_options = new Options();
        // glycan file
        Option t_option = new Option("g", "glycan-file", true,
                "CSV file containing the GlyTouCan IDs and Glycam sequences to be processed.");
        t_option.setArgs(1);
        t_option.setRequired(false);
        t_options.addOption(t_option);
        // output folder
        t_option = new Option("j", "glycan-jobs", true,
                "Glycan job file to re-run the processing for glycans not successful in the earlier run.");
        t_option.setArgs(1);
        t_option.setRequired(false);
        t_options.addOption(t_option);
        // output folder
        t_option = new Option("o", "output", true,
                "Path for the output folder that will be used to store the PDB files, error log and glycan job file.");
        t_option.setArgs(1);
        t_option.setRequired(true);
        t_options.addOption(t_option);
        // max waiting time
        t_option = new Option("w", "waiting-time", true,
                "Maximum waiting time (milliseconds) for a job to finish. If the processing time is longer it will be considered timeout. Default: 600000");
        t_option.setArgs(1);
        t_option.setRequired(false);
        t_options.addOption(t_option);
        // polling sleep time
        t_option = new Option("s", "polling-time", true,
                "Waiting time between two polling requests (milliseconds). Can not be smaller than 2000. Default: 3000");
        t_option.setArgs(1);
        t_option.setRequired(false);
        t_options.addOption(t_option);
        // queue length
        t_option = new Option("q", "queue", true,
                "Maximum number of jobs that be queue up. If this number is reached submission will stop till one job finished or times out. Default: 5");
        t_option.setArgs(1);
        t_option.setRequired(false);
        t_options.addOption(t_option);
        // verbose
        t_option = new Option("v", "verbose", false,
                "Prints current job number to report progress.");
        t_option.setArgs(0);
        t_option.setRequired(false);
        t_options.addOption(t_option);
        // base url
        t_option = new Option("b", "base-url", true,
                "Base URL of glycam. e.g., https://glycam.org or https://test.glycam.org");
        t_option.setArgs(1);
        t_option.setRequired(false);
        t_options.addOption(t_option);
        return t_options;
    }

}

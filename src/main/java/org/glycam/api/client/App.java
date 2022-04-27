package org.glycam.api.client;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.glycam.api.client.csv.CSVError;
import org.glycam.api.client.csv.SequenceFileParser;
import org.glycam.api.client.json.GlycamJobSerializer;
import org.glycam.api.client.om.GlycamJob;
import org.glycam.api.client.util.GlycamUtil;

public class App
{

    public static void main(String[] args) throws IOException, InterruptedException
    {
        long t_startTime = System.currentTimeMillis();
        // load the glycam sequence
        SequenceFileParser t_csvParser = new SequenceFileParser();
        List<GlycamJob> t_jobs = t_csvParser.loadFile("./data/2022.04.10.glycan.csv");

        GlycamUtil t_util = new GlycamUtil("./data/output/pdb/",
                GlycamUtil.DEFAULT_MAX_WAITING_TIME);
        t_util.process(t_jobs);

        GlycamJobSerializer.serialize(t_jobs, "./data/output/jobs.json");

        CSVError t_errorLog = new CSVError("./data/output/");

        for (GlycamJob t_glycamJob : t_jobs)
        {
            String t_status = t_glycamJob.getStatus();
            if (!t_status.equals(GlycamJob.STATUS_SUCCESS)
                    && !t_status.equals(GlycamJob.STATUS_INIT))
            {
                t_errorLog.writeError(t_glycamJob);
            }
        }
        t_errorLog.closeFile();
        Long t_durationMinutes = (System.currentTimeMillis() - t_startTime) / 60000;
        System.out.println("Finished after " + t_durationMinutes.toString() + " minutes");
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
                App.printComandParameter(a_options);
                return null;
            }
        }
        catch (ParseException e)
        {
            System.out.println("Invalid commandline arguments: " + e.getMessage());
            App.printComandParameter(a_options);
            return null;
        }
        catch (Exception e)
        {
            System.out.println(
                    "There was an error processing the command line arguments: " + e.getMessage());
            App.printComandParameter(a_options);
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
        t_arguments.setOutputPath(t_commandLine.getOptionValue("o"));
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
                System.out.println("Waiting time (-w) is expectinga number.");
                return null;
            }
        }
        t_value = t_commandLine.getOptionValue("s");

        t_value = t_commandLine.getOptionValue("q");
        t_arguments.setMappingFolder();
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
        // config file
        if (a_arguments.getConfigFile() != null)
        {
            // config file must exist
            File t_file = new File(a_arguments.getConfigFile());
            if (t_file.exists())
            {
                if (t_file.isDirectory())
                {
                    System.out.println("Config file (-c) can not be a directory.");
                    t_valid = false;
                }
            }
            else
            {
                System.out.println("Config file (-c) does not exist.");
                t_valid = false;
            }
        }
        else
        {
            System.out.println("Config file (-c) is missing.");
            t_valid = false;
        }
        // properties file
        if (a_arguments.getConfigFile() != null)
        {
            // file must exist
            File t_file = new File(a_arguments.getPropertiesFile());
            if (t_file.exists())
            {
                if (t_file.isDirectory())
                {
                    System.out.println("Properties file (-p) can not be a directory.");
                    t_valid = false;
                }
            }
            else
            {
                System.out.println("Properties file (-p) does not exist.");
                t_valid = false;
            }
        }
        else
        {
            System.out.println("Properties file (-p) is missing.");
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
                    System.out.println("Unable to create output folder.");
                    t_valid = false;
                }
            }
        }
        else
        {
            System.out.println("Output folder (-o) is missing.");
            t_valid = false;
        }
        // mapping folder
        if (a_arguments.getMappingFolder() != null)
        {
            File t_file = new File(a_arguments.getMappingFolder());
            if (t_file.exists())
            {
                if (!t_file.isDirectory())
                {
                    System.out.println("Mapping folder (-m) has to be a directory.");
                    t_valid = false;
                }
            }
            else
            {
                System.out.println("Mapping folder (-m) does not exist.");
                t_valid = false;
            }
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

        return t_options;
    }

}



public class StringTester
{

    public static void main(String[] args)
    {
        String t_response = "output: \"{\\\"entity\\\": {\\\"type\\\": \\\"Sequence\\\"}, \\\"responses\\\": [{\\\"Build3DStructure\\\": {\\\"payload\\\": \\\"5c7f2265-0eb8-48ce-9737-1b913bd56645\\\"}}]}\"";
        System.out.println(t_response);
        Integer t_pos = t_response.indexOf("payload");
        String t_jobId = t_response.substring(t_pos);
        String[] t_parts = t_jobId.split("\"");
        t_jobId = t_parts[2];
        t_jobId = t_jobId.replaceAll("\\\\", "");
        System.out.println(t_jobId);
    }

}

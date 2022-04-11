package org.glycam.api.client.json;

import org.glycam.api.client.json.glycan.Build;
import org.glycam.api.client.json.glycan.Entity;
import org.glycam.api.client.json.glycan.GlycanRequest;
import org.glycam.api.client.json.glycan.Inputs;
import org.glycam.api.client.json.glycan.Sequence;
import org.glycam.api.client.json.glycan.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestBuilder
{
    public static String buildGlycanRequest(String a_sequence) throws JsonProcessingException
    {
        Sequence t_sequence = new Sequence();
        t_sequence.setPayload(a_sequence);
        Inputs t_inputs = new Inputs();
        t_inputs.setSequence(t_sequence);

        Build t_build = new Build();
        t_build.setType("Build3DStructure");
        Services t_services = new Services();
        t_services.setBuild(t_build);

        Entity t_entity = new Entity();
        t_entity.setInputs(t_inputs);
        t_entity.setSerivces(t_services);
        t_entity.setType("Sequence");

        GlycanRequest t_request = new GlycanRequest();
        t_request.setEntity(t_entity);

        // create the JSON string
        ObjectMapper t_mapper = new ObjectMapper();
        String t_json = t_mapper.writeValueAsString(t_request);
        return t_json;
    }
}

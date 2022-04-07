package org.glycam.api.client.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class SequenceBuildInputUtil
{
    private String m_sequence = null;

    private SequenceBuildInputUtil(String a_sequence)
    {
        this.m_sequence = a_sequence;
    }

    public static String glycanSequenceToJSON(String a_sequence)
    {
        SequenceBuildInputUtil t_util = new SequenceBuildInputUtil(a_sequence);
        JSONObject t_topLevel = new JSONObject();
        JSONObject t_entity = t_util.buildEntity();
        t_topLevel.put("entity", t_entity);
        return t_topLevel.toJSONString();
    }

    private JSONObject buildEntity()
    {
        JSONObject t_entity = new JSONObject();
        // build entity
        t_entity.put("type", "Sequence");
        t_entity.put("services", this.buildServices());
        t_entity.put("inputs", this.buildInputs());
        t_entity.put("resources", new JSONArray());
        return t_entity;
    }

    private JSONObject buildInputs()
    {
        JSONObject t_object = new JSONObject();
        t_object.put("sequence", this.buildSequence());
        return t_object;
    }

    private JSONObject buildSequence()
    {
        JSONObject t_object = new JSONObject();
        t_object.put("payload", this.m_sequence);
        return t_object;
    }

    private Object buildServices()
    {
        JSONObject t_object = new JSONObject();
        t_object.put("build", this.buildBuild());
        return t_object;
    }

    private Object buildBuild()
    {
        JSONObject t_object = new JSONObject();
        t_object.put("type", "Build3DStructure");
        return t_object;
    }

}

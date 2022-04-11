package org.glycam.api.client.json.glycan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Inputs
{
    private Sequence m_sequence = null;

    @JsonProperty("sequence")
    public Sequence getSequence()
    {
        return this.m_sequence;
    }

    public void setSequence(Sequence a_sequence)
    {
        this.m_sequence = a_sequence;
    }
}

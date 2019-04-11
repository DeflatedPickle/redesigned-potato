package com.deflatedpickle.radialmenu;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.Arrays;
import java.util.List;

@XStreamAlias("radial")
public class Radial {
    @XStreamAlias("connections")
    @XStreamAsAttribute
    public Boolean connections;

    @XStreamImplicit(itemFieldName = "button")
    public List<Button> buttonList;

    public Radial(Boolean connections, Button... buttons) {
        this.connections = connections;
        this.buttonList = Arrays.asList(buttons);
    }
}

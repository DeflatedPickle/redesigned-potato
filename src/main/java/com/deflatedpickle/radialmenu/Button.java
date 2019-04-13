package com.deflatedpickle.radialmenu;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.Arrays;
import java.util.List;

@XStreamAlias("button")
public class Button {
    @XStreamAlias("text")
    @XStreamAsAttribute
    public String text;

    @XStreamAlias("command")
    @XStreamAsAttribute
    public String command;

    @XStreamImplicit(itemFieldName = "button")
    public List<Button> buttonList;

    public RadialButton radialButton;

    public Button(String text, String command, Button... buttons) {
        this.text = text;
        this.command = command;
        this.buttonList = Arrays.asList(buttons);
    }
}

package com.deflatedpickle.radialmenu;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("button")
public class Button {
    @XStreamAlias("text")
    @XStreamAsAttribute
    public String text;

    public Button(String text) {
        this.text = text;
    }
}

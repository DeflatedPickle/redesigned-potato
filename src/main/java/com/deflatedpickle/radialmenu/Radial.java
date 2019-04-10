package com.deflatedpickle.radialmenu;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.Arrays;
import java.util.List;

@XStreamAlias("radial")
public class Radial {
    @XStreamImplicit(itemFieldName = "button")
    public List<Button> buttonList;

    public Radial(Button... buttons) {
        this.buttonList = Arrays.asList(buttons);
    }
}

package com.cjmalloy.multipage.client.ui.skin;

import com.google.gwt.resources.client.CssResource;


public interface PageTransition extends CssResource
{
    String initial();
    String in();
    String out();
}

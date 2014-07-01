package com.cjmalloy.multipage.client.ui;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;


public abstract class Page extends Composite implements ResizeHandler
{
    public Page()
    {
        Window.addResizeHandler(this);
        Window.enableScrolling(false);
        Scheduler.get().scheduleFinally(new ScheduledCommand()
        {
            @Override
            public void execute()
            {
                onResize(null);
            }
        });
    }

    public abstract void onHide();

    @Override
    public void onResize(ResizeEvent event)
    {
        setPixelSize(Window.getClientWidth(), Window.getClientHeight());
    }

    public abstract void onShow();

    public void remove(int delay)
    {
        if (delay <= 0)
        {
            Page.this.removeFromParent();
            return;
        }
        Scheduler.get().scheduleFixedDelay(new RepeatingCommand()
        {
            @Override
            public boolean execute()
            {
                Page.this.removeFromParent();
                return false;
            }
        }, delay);
    }

    public interface PageFactory
    {
        public void create(Callback<Page, Throwable> callback);
    }
}

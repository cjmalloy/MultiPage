package com.cjmalloy.multipage.client;

import java.util.HashMap;
import java.util.Map;

import com.cjmalloy.multipage.client.ui.Page;
import com.cjmalloy.multipage.client.ui.Page.PageFactory;
import com.cjmalloy.multipage.client.ui.skin.PageTransition;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;


public class MultiPage {

  private static Map<String, PageFactory> pages = new HashMap<>();

  private Page currentPage = null;
  private PageFactory defaultPage = null;
  private PageTransition transition = null;
  private int removeDelay = 0;

  public MultiPage() {
    History.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        setPage(event.getValue());
      }
    });
  }

  public MultiPage(PageTransition t, int removeDelay) {
    this();
    setTransition(t, removeDelay);
  }

  public void put(String pageIndex, PageFactory factory) {
    put(pageIndex, factory, false);
  }

  public void put(String pageIndex, PageFactory factory, boolean isDefault) {
    pages.put(pageIndex, factory);
    if (isDefault) {
      defaultPage = factory;
    }
  }

  public void setPage(final Page p) {
    if (transition != null) {
      p.setStyleName(transition.initial(), true);
    }
    RootPanel.get().add(p, 0, 0);
    if (currentPage != null) {
      if (transition != null) {
        currentPage.setStyleName(transition.out(), true);
      }
      currentPage.remove(removeDelay);
      currentPage.onHide();
    }
    p.onShow();
    currentPage = p;
    if (transition != null) {
      Scheduler.get().scheduleDeferred(new ScheduledCommand() {
        @Override
        public void execute() {
          p.setStyleName(transition.in(), true);
        }
      });
    }
  }

  public void setPage(final String historyToken) {
    PageFactory f = defaultPage;
    if (pages.containsKey(historyToken)) {
      f = pages.get(historyToken);
    }

    if (f != null) {
      f.create(new Callback<Page, Throwable>() {
        @Override
        public void onFailure(Throwable reason) {
          throw new Error(reason);
        }

        @Override
        public void onSuccess(Page result) {
          setPage(result);
        }
      });
    }
  }

  public void setTransition(PageTransition t, int removeDelay) {
    this.transition = t;
    this.removeDelay = removeDelay;
  }
}

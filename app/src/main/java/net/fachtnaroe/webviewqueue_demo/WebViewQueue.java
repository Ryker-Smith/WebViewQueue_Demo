package net.fachtnaroe.webviewqueue_demo;

import com.google.appinventor.components.runtime.Clock;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;

// Version/Date 2023-03-04-1345

public class WebViewQueue extends Form implements HandlesEventDispatching {
    /* suggestion: name your variable of this type as 'wvq' so
    that your code would read as, eq:
    wvq.toGame(cmd)
    wvq.fromGame()
     */
    public final static Integer queue_max=0;
    public static Integer default_timer=1000;
    public static boolean busy;
    private static String[] queue_out =new String[queue_max + 1];
    private static Integer head;
    private static fachtnaWebViewer theWebView;
    private HandlesEventDispatching containingForm;
    public static Clock ticker;

    public WebViewQueue(HandlesEventDispatching form, fachtnaWebViewer wvcomponent){
        // constructor
        head=0;
        containingForm=form;
//        ticker=new Clock(this);
//        ticker.TimerInterval(default_timer);

//        for (int i=0; i<queue_max;i++){
//            queue_out[i]="";
//        }
        theWebView=wvcomponent;
//        ticker.TimerEnabled(true);
    }

    public boolean toGame(String cmd) {
        // enqueue the instructiom
        head++;
        EventDispatcher.unregisterEventForDelegation( this, this.toString(), "fachtnaWebViewStringChange");
        theWebView.WebViewString(cmd);
        EventDispatcher.registerEventForDelegation( this, this.toString(), "fachtnaWebViewStringChange");
        // if we make it this far then
        return true;
    }

    public String fromGame(){

        return theWebView.WebViewString();
    }

    public Integer qSize(){

        return head;
    }

    public boolean qFull(){

        return false;
    }
}

package net.fachtnaroe.webviewqueue_demo;

import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.HandlesEventDispatching;

public class WebViewQueue extends Object {
    /* suggestion: name your variable of this type as 'wvq' so
    that your code would read as, eq:
    wvq.toGame(cmd)
    wvq.fromGame()
     */
    public final static byte queue_max=4;
    public static boolean busy;
    private static String[] queue_out =new String[queue_max + 1];
    private static byte head;
    private static fachtnaWebViewer theWebView;
    private HandlesEventDispatching containingForm;

    public WebViewQueue(HandlesEventDispatching form, fachtnaWebViewer wvcomponent){
        // constructor
        head=0;
        containingForm=form;
        for (int i=0; i<queue_max;i++){
            queue_out[i]="";
        }
        theWebView=wvcomponent;
    }

    public boolean toGame(String cmd) {
        // enqueue the instructiom
        EventDispatcher.unregisterEventForDelegation( containingForm, containingForm.toString(), "fachtnaWebViewStringChange");
        theWebView.WebViewString(cmd);
        EventDispatcher.registerEventForDelegation( containingForm, containingForm.toString(), "fachtnaWebViewStringChange");
        // if we make it this far then
        return true;
    }

    public String fromGame(){
        return theWebView.WebViewString();
    }

    public byte qSize(){
        return 0;
    }

    public boolean qFull(){
        return false;
    }
}
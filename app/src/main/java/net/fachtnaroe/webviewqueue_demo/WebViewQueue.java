package net.fachtnaroe.webviewqueue_demo;

import com.google.appinventor.components.runtime.Clock;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;

import static net.fachtnaroe.webviewqueue_demo.MainActivity.dbg;

// Version/Date 2023-03-04-2055

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

    @Override
    public boolean canDispatchEvent(Component component, String s) {
        return false;
    }

    @Override
    public boolean dispatchEvent(Component component, String s, String s1, Object[] objects) {
        return false;
    }
    public WebViewQueue(HandlesEventDispatching form, fachtnaWebViewer wvcomponent){
        // constructor
        head=0;
        containingForm=form;
        dbg("DD "+form.toString());

        for (int i=0; i<queue_max;i++){
            queue_out[i]="";
        }
        theWebView=wvcomponent;

    }

    public boolean toGame(String cmd) {
        head++;
        dbg("   toGame      ["+containingForm.toString()+"]");
        EventDispatcher.unregisterEventForDelegation( this, containingForm.toString(), "fachtnaWebViewStringChange");
        theWebView.WebViewString(cmd);
        EventDispatcher.registerEventForDelegation( this, containingForm.toString(), "fachtnaWebViewStringChange");
        return true;
    }

    public String fromGame(){
        dbg("   fromGame    ["+containingForm.toString()+"]");
        EventDispatcher.unregisterEventForDelegation( this, containingForm.toString(), "fachtnaWebViewStringChange");
        String recvd=theWebView.WebViewString();
        EventDispatcher.registerEventForDelegation( this, containingForm.toString(), "fachtnaWebViewStringChange");
        return recvd;
    }

    public Integer qSize(){

        return head;
    }

    public boolean qFull(){

        return false;
    }
}

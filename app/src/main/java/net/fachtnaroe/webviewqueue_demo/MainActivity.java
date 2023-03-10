package net.fachtnaroe.webviewqueue_demo;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Clock;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.VerticalArrangement;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Form implements HandlesEventDispatching {

    private
    Button sendButton;
    Label rxLabel, txLabel, heading, topHeading, qSizeLbl, qNowLbl;
    TextBox rxTextBox, txTextBox, dbgLabel;
    HorizontalArrangement main;
    VerticalArrangement androidDisplay, outer;
    customisedWebViewer htmlDisplay;
    static boolean debugging_onoff=true;
    Clock clicketty;

    protected void $define() {
        this.Sizing("Responsive");
        this.BackgroundColor(0xFF167340);
        outer=new VerticalArrangement(this);
        outer.WidthPercent(100);
        outer.HeightPercent(100);
        topHeading=new Label(outer);
        topHeading.Width(Component.LENGTH_FILL_PARENT);
        topHeading.Text(
                getPackageName()+" (build "+Integer.toString(BuildConfig.VERSION_CODE)+")"
        );
        topHeading.TextColor(Component.COLOR_WHITE);
        topHeading.FontSize(12);
        topHeading.BackgroundColor(Component.COLOR_BLACK);
        topHeading.TextAlignment(ALIGNMENT_CENTER);

        main = new HorizontalArrangement(outer);
        main.Height(Component.LENGTH_FILL_PARENT);
        main.WidthPercent(100);

        htmlDisplay=new customisedWebViewer(main);
        htmlDisplay.WidthPercent(50);
        htmlDisplay.Height(Component.LENGTH_FILL_PARENT);
        htmlDisplay.HomeUrl("file:///android_asset/webviewqueue_demo.html");
        htmlDisplay.WebViewString("htmlDisplay_started");
        htmlDisplay.GoHome();

        androidDisplay = new VerticalArrangement(main);
        androidDisplay.WidthPercent(50);
        androidDisplay.Height(Component.LENGTH_FILL_PARENT);

        heading = new Label(androidDisplay);
        heading.Text("android");
        heading.TextColor(Component.COLOR_WHITE);
        heading.FontSize(45);
        qSizeLbl=new Label(androidDisplay);
        qSizeLbl.TextColor(Component.COLOR_WHITE);
        qSizeLbl.FontTypeface(Component.TYPEFACE_MONOSPACE);
        qNowLbl=new Label(androidDisplay);
        qNowLbl.TextColor(Component.COLOR_WHITE);
        qNowLbl.FontTypeface(Component.TYPEFACE_MONOSPACE);

        rxLabel = new Label(androidDisplay);
        rxLabel.Width(Component.LENGTH_FILL_PARENT);
        rxLabel.Text("Receive");
        rxLabel.TextColor(Component.COLOR_WHITE);
        rxLabel.FontSize(20);
        rxLabel.FontBold(true);
        rxTextBox=new TextBox(androidDisplay);
        rxTextBox.Width(Component.LENGTH_FILL_PARENT);
        rxTextBox.BackgroundColor(Component.COLOR_WHITE);
        rxTextBox.Height(50);

        txLabel = new Label(androidDisplay);
        txLabel.Width(Component.LENGTH_FILL_PARENT);
        txLabel.Text("\nSend");
        txLabel.TextColor(Component.COLOR_WHITE);
        txLabel.FontSize(20);
        txLabel.FontBold(true);
        txTextBox=new TextBox(androidDisplay);
        txTextBox.Width(Component.LENGTH_FILL_PARENT);
        txTextBox.Text("hello world");
        txTextBox.BackgroundColor(Component.COLOR_WHITE);
        txTextBox.Height(50);

        sendButton=new Button(androidDisplay);
        sendButton.Width(Component.LENGTH_FILL_PARENT);
        sendButton.Text("Send");

        dbgLabel=new TextBox(androidDisplay);
        dbgLabel.Width(Component.LENGTH_FILL_PARENT);
        dbgLabel.BackgroundColor(Component.COLOR_WHITE);
        dbgLabel.Height(50);
        dbgLabel.Text("---");

        clicketty=new Clock(this);
        clicketty.TimerInterval(500);
        clicketty.TimerEnabled(true);
        qSizeLbl.Text("Sequence counter");

        EventDispatcher.registerEventForDelegation(this, this.toString(), "Click");
        EventDispatcher.registerEventForDelegation(this, this.toString(), "Timer");
        EventDispatcher.registerEventForDelegation(this, this.toString(), "wvq_fromGame");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {

//        dbg("dispatchEvent: [" + formName + "] [" +component.toString() + "] [" + componentName + "] " + eventName);
        if (eventName.equals("Click")) {
            if (component.equals(sendButton)) {
                htmlDisplay.toGame(
                        htmlDisplay.as_JSON(new String[] {"content", txTextBox.Text()})
                );
                qSizeLbl.Text("Seq: "+
                        // the readOnly_Sequence value may not be current
                        Long.toString(htmlDisplay.readOnly_Sequence())
                 );
                return true;
            }
        }
        else if(component.equals("Timer")){
            return true;
        }
        else if( eventName.equals("wvq_fromGame") ) {
            if (component.equals(htmlDisplay)) {
                /*  The runOnUiThread error was being raised:
                    "Only the original thread that created a view hierarchy can touch its views"
                    by the updating of the main screen when this block executed
                    https://stackoverflow.com/questions/18656813/android-only-the-original-thread-that-created-a-view-hierarchy-can-touch-its-vi

                 */
                MainActivity.getActiveForm().runOnUiThread(new Runnable(){
                    public void run(){
                        try {
                            String r=htmlDisplay.fromGame();
                            JSONObject parser = new JSONObject(r);
                            qSizeLbl.Text("Seq: "+parser.getString("sequence"));
                            rxTextBox.Text(parser.getString("content"));
                        }
                        catch (JSONException e){
                            dbg(e.toString());
                        }
                    }
                });
                return true;
            }
        }
        return false;
    }

    public static void dbg (String debugMsg) {
        if (debugging_onoff) {
            System.err.print( "~~~> " + debugMsg + " <~~~\n");
        }
    }
}

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
import com.google.appinventor.components.runtime.WebViewer;
import com.google.appinventor.components.runtime.VerticalArrangement;

public class MainActivity extends Form implements HandlesEventDispatching {

    private
    Button sendButton;
    Label rxLabel, txLabel, heading, topHeading, qSizeLbl, qNowLbl;
    TextBox rxTextBox, txTextBox, dbgLabel;
    HorizontalArrangement main;
    VerticalArrangement androidDisplay, outer;
    fachtnaWebViewer htmlDisplay;
    WebViewQueue wvq;
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

        htmlDisplay=new fachtnaWebViewer(main);
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

        wvq=new WebViewQueue(this, htmlDisplay);
        qSizeLbl.Text("Max  Q: "+wvq.queue_max.toString());
        qNowLbl.Text ("Size Q: "+wvq.qSize());
        clicketty=new Clock(this);
        clicketty.TimerInterval(1000);
        clicketty.TimerEnabled(true);

        dbg(" Starting register["+this.toString()+"]");
        EventDispatcher.registerEventForDelegation(this, this.toString(), "Click");
        EventDispatcher.registerEventForDelegation(this, this.toString(), "Timer");
        EventDispatcher.registerEventForDelegation(this, this.toString(), "fachtnaWebViewStringChange");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {

        //dbg("dispatchEvent: [" + formName + "] [" +component.toString() + "] [" + componentName + "] " + eventName);

        if (eventName.equals("Click")) {
            if (component.equals(sendButton)) {
                wvq.toGame(txTextBox.Text());
                dbg("Sending: "+txTextBox.Text() );
                return true;
            }
        }
        else if(component.equals("Timer")){
            qNowLbl.Text ("Size Q: "+wvq.qSize());
        }
        else if( eventName.equals("fachtnaWebViewStringChange") ) {
            dbg("   component   ["+component.toString()+"]");
            if (component.equals(htmlDisplay)) {
                String r=wvq.fromGame();
                rxTextBox.Text(r);
            }
            return true;
        }
        return false;
    }

    public static void dbg (String debugMsg) {

        if (debugging_onoff) {
            System.err.print( "~~~> " + debugMsg + " <~~~\n");
        }
    }
}

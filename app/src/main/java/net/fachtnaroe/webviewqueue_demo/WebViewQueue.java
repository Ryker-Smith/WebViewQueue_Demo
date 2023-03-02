package net.fachtnaroe.gwlogin2023_fr;

public class WebViewQueue {
    /* suggestion: name your variable of this type as 'wvq' so
    that your code would read as, eq:
    wvq.toGame(cmd)
    wvq.fromGame()
     */
    public final static byte queue_max=4;
    private static String[] queue_out =new byte[queue_max + 1];
    private static String inbound; // only one 'inbound' data item permitted
    private static byte head;

    public WebViewQueue(){
        // constructor
        head=0;
        for (int i=0; i<queue_max;i++){
            queue_out[i]="";
        }
    }

    boolean toGame(String cmd) {
        // enqueue the instructiom
        // if anything goes wrong, return false;
        queue_out[head]=cmd;
        head++;
        if (head>queue_max) {
            head=queue_max;
        }
        // if we make it this far then
        return true;
    }

    String fromGame(){
        return inbound;
    }
    byte qSize(){
        return 0;
    }
    boolean qFull(){
        return false;
    }
}

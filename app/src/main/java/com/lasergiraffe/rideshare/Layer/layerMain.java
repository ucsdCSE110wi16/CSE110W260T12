package com.lasergiraffe.rideshare.Layer;

import android.app.Activity;
import android.os.Bundle;

import com.layer.sdk.LayerClient;

/**
 * Created by lamki on 2/19/2016.
 */
public class layerMain extends Activity{
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //Testing Layer
        // Instatiates a LayerClient object
        LayerClient.Options options = new LayerClient.Options().googleCloudMessagingSenderId("355953973684");
        LayerClient client = LayerClient.newInstance(this, "layer:///apps/staging/709b7990-d5be-11e5-a6ef-50e81500048b", options);

        // Asks the LayerSDK to establish a network connection with the Layer service
        client.connect();
    }
}

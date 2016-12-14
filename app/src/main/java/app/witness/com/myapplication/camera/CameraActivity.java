package app.witness.com.myapplication.camera;

import android.os.Bundle;

import app.witness.com.myapplication.main.BaseActivity;


public class CameraActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CamaraView camaraView = new CamaraView(this);
        setContentView(camaraView);
    }
}

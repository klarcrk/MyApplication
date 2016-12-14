package app.witness.com.myapplication.dagger;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import javax.inject.Inject;

import app.witness.com.myapplication.R;
import app.witness.com.myapplication.main.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaggerActivity2 extends BaseActivity {

    @BindView(R.id.btnMakeCoffee)
    Button btnMakeCoffee;
    @BindView(R.id.tvCoffee)
    TextView tvCoffee;

    @Inject
    Poetry poetry;
    @Inject
    Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_coffee_make);
        ButterKnife.bind(this);
        PoeComponent.getInstance().inject(this);
    }

    @OnClick(R.id.btnMakeCoffee)
    public void onClick() {
        String json = mGson.toJson(poetry);
        String text = json + ",mPoetry:"+poetry;
        tvCoffee.setText(text);
    }
}

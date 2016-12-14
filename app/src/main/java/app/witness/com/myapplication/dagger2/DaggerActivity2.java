package app.witness.com.myapplication.dagger2;

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
    Gson mGson;

    @Inject
    Poetry poetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_coffee_make);
        ButterKnife.bind(this);
        DaggerDagger2Component.builder().build().inject(this);
    }

    @OnClick(R.id.btnMakeCoffee)
    public void onClick() {
        String result = mGson.toJson(poetry);
        tvCoffee.setText(result);
    }
}

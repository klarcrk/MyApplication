package app.witness.com.myapplication.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        setContentView(listView);
        activityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        intentMap = queryActivities();
        activityAdapter.addAll(intentMap.keySet());
        listView.setAdapter(activityAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = activityAdapter.getItem(position);
                Intent intent = intentMap.get(item);
                Intent activityIntent = new Intent(intent);
                startActivity(activityIntent);
            }
        });
        /*Intent intent = getIntent();
        String path = intent.getStringExtra("com.example.android.apis.Path");
        if (path == null) {
            path = "";
        }
        getData(path);*/
    }

    ArrayAdapter<String> activityAdapter;
    Map<String, Intent> intentMap;

    public Map<String, Intent> queryActivities() {
        PackageManager pm = getPackageManager();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_SAMPLE_CODE);
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        String packageName = getPackageName();
        Map<String, Intent> intentMap = new HashMap<>();
        if (infos != null) {
            for (ResolveInfo info : infos) {
                ActivityInfo activityInfo = info.activityInfo;
                if (activityInfo.packageName.equals(packageName)) {
                    Intent activityIntent = getActivityIntent(activityInfo);
                    String name = activityInfo.name;
                    String[] nameValues = name.split("\\.");
                    String nameValue;
                    if (nameValues != null) {
                        nameValue = nameValues[nameValues.length - 1];
                    } else {
                        nameValue = name;
                    }
                    String updateNameValue = updateNameValue(intentMap, nameValue);
                    intentMap.put(updateNameValue, activityIntent);
                }
            }
        }
        return intentMap;
    }

    private String updateNameValue(Map<String, Intent> intentMap, String nameValue) {
        if (intentMap.containsKey(nameValue)) {
            nameValue = nameValue + 1;
            return updateNameValue(intentMap, nameValue);
        }
        return nameValue;
    }

    public Intent getActivityIntent(ActivityInfo activityInfo) {
        Intent intent = new Intent();
        intent.setClassName(activityInfo.packageName, activityInfo.name);
        return intent;
    }

    protected List<Map<String, Object>> getData(String prefix) {
        List<Map<String, Object>> myData = new ArrayList<>();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
        if (null == list)
            return myData;
        String[] prefixPath;
        String prefixWithSlash = prefix;
        if (prefix.equals("")) {
            prefixPath = null;
        } else {
            prefixPath = prefix.split("/");
            prefixWithSlash = prefix + "/";
        }
        int len = list.size();
        Map<String, Boolean> entries = new HashMap<>();
        for (int i = 0; i < len; i++) {
            ResolveInfo info = list.get(i);
            CharSequence labelSeq = info.loadLabel(pm);
            String label = labelSeq != null
                    ? labelSeq.toString()
                    : info.activityInfo.name;

            if (prefixWithSlash.length() == 0 || label.startsWith(prefixWithSlash)) {

                String[] labelPath = label.split("/");

                String nextLabel = prefixPath == null ? labelPath[0] : labelPath[prefixPath.length];

                if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {
                    addItem(myData, nextLabel, activityIntent(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name));
                } else {
                    if (entries.get(nextLabel) == null) {
                        addItem(myData, nextLabel, browseIntent(prefix.equals("") ? nextLabel : prefix + "/" + nextLabel));
                        entries.put(nextLabel, true);
                    }
                }
            }
        }
        Collections.sort(myData, sDisplayNameComparator);
        return myData;
    }

    private final static Comparator<Map<String, Object>> sDisplayNameComparator =
            new Comparator<Map<String, Object>>() {
                private final Collator collator = Collator.getInstance();

                public int compare(Map<String, Object> map1, Map<String, Object> map2) {
                    return collator.compare(map1.get("title"), map2.get("title"));
                }
            };

    protected Intent activityIntent(String pkg, String componentName) {
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }

    protected Intent browseIntent(String path) {
        Intent result = new Intent();
        result.setClass(this, MainActivity.class);
        result.putExtra("com.example.android.apis.Path", path);
        return result;
    }

    protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);

        Intent intent = new Intent((Intent) map.get("intent"));
        intent.addCategory(Intent.CATEGORY_SAMPLE_CODE);
        startActivity(intent);
    }
}

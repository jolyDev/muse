package org.andresoviedo.app.model3D.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.google.ar.core.ArCoreApk;

import org.andresoviedo.dddmodel2.R;
import org.andresoviedo.lang.LanguageManager;
import org.andresoviedo.lang.Tokens;
import org.andresoviedo.util.android.ContentUtils;

import java.util.HashMap;
import java.util.Map;

import static com.google.ar.core.ArCoreApk.InstallStatus.INSTALLED;

public class GridMenu extends Activity {

    GridLayout gridView;

    private static final String REPO_URL = "https://github.com/andresoviedo/android-3D-model-viewer/raw/master/models/index";
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1000;
    private static final int REQUEST_INTERNET_ACCESS = 1001;
    private static final int REQUEST_CODE_OPEN_FILE = 1101;
    private static final int REQUEST_CODE_OPEN_MATERIAL = 1102;
    private static final int REQUEST_CODE_OPEN_TEXTURE = 1103;
    public static final int REQUEST_CODE_QR_CODE = 1104;
    private static final int PERMISSION_STORAGE_CODE = 1000;
    private static final String SUPPORTED_FILE_TYPES_REGEX = "(?i).*\\.(obj|stl|dae|gltf)";

    private Map<String, Object> loadModelParameters = new HashMap<>();
    private LanguageManager lang = LanguageManager.GetInstance();
    SharedPreferences sPref;

    private final String prefsId = "ui";
    private final String languageId = "style";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_menu);

        _loadLanguagePreferences();
        gridView = (GridLayout) findViewById(R.id.grid_items);
        _UpdateMenuItems();
        int childCount = gridView.getChildCount();
        Log.i("onCreate", "childCount="+childCount);

        for (int i= 0; i < childCount; i++){
            CardView container = (CardView) gridView.getChildAt(i);
            setAction(i, container);
        }

    }

    private void setAction(int i, CardView container) {
        if(i == 3){
            container.setOnClickListener((View view)->{
                LanguageSettings();
            });
        } else {
            container.setOnClickListener((View view)->{
                Toast.makeText(getApplicationContext(), "sjfifojid", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void _UpdateMenuItems()
    {
        String[] menuItems = checkAR_Permission() ?
                new String[]{
                        lang.Get(Tokens.scanQR_AR),
                        lang.Get(Tokens.AR),
                        lang.Get(Tokens.atlas),
                        lang.Get(Tokens.language),
                        lang.Get(Tokens.about),
                        lang.Get(Tokens.exit)
                } :
                new String[]{
                        lang.Get(Tokens.scanQR),
                        lang.Get(Tokens.viewItems),
                        lang.Get(Tokens.atlas),
                        lang.Get(Tokens.language),
                        lang.Get(Tokens.about),
                        lang.Get(Tokens.exit)
                };

        int childCount = gridView.getChildCount();

        for (int i= 0; i < childCount; i++){
            CardView container = (CardView) gridView.getChildAt(i);
            LinearLayout linlay = (LinearLayout)container.getChildAt(0);
            TextView text = (TextView) linlay.getChildAt(1);
            text.setText(menuItems[i]);
        }
    }

    private boolean checkAR_Permission()
    {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);

        if(availability.isSupported())
        {
            try {
                return ArCoreApk.getInstance().requestInstall(this, true) == INSTALLED;
            }
            catch (Exception e) {
                // uninstalled for somereason
                return false;
            }
        }
        else if (availability.isTransient()) {
            // Re-query at 5Hz while compatibility is checked in the background.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkAR_Permission();
                    _UpdateMenuItems();
                }
            }, 200);
        }

        return false;
    }

    void _loadLanguagePreferences()
    {
        sPref = getSharedPreferences(prefsId, MODE_PRIVATE);
        lang.code = sPref.getInt(languageId, lang.ENG);
    }

    void _saveLanguagePreferences()
    {
        sPref = getSharedPreferences(prefsId, MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt(languageId, lang.code);
        editor.apply();
    }

    private void LanguageSettings(){
        ContentUtils.showListDialog(this, lang.Get(Tokens.language),
                new String[]{
                        lang.Get(Tokens.english),
                        lang.Get(Tokens.ukrainian),
                        lang.Get(Tokens.russian),
                        lang.Get(Tokens.back)
                },
                (dialog, which) ->
                {
                    switch(which) {
                        case 0:
                            lang.code = lang.ENG;
                            break;
                        case 1:
                            lang.code = lang.UA;
                            break;
                        case 2:
                            lang.code = lang.RUS;
                            break;
                        default:
                            return;
                    }
                    _saveLanguagePreferences();
                    _UpdateMenuItems();
                });
    }
}
package org.andresoviedo.app.model3D.view;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.ar.core.ArCoreApk;

import org.andresoviedo.android_3d_model_engine.services.wavefront.WavefrontLoader;
import org.andresoviedo.app.model3D.Atlas.AtlasActivity;
import org.andresoviedo.app.model3D.DevTools.LinkConventer;
import org.andresoviedo.app.model3D.arcorehelpers.ArCoreHelper;
import org.nnmu.R;
import org.andresoviedo.lang.LanguageManager;
import org.andresoviedo.lang.Tokens;
import org.andresoviedo.util.android.AndroidUtils;
import org.andresoviedo.util.android.ContentUtils;
import org.andresoviedo.util.android.FileUtils;
import org.andresoviedo.util.view.TextActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.ar.core.ArCoreApk.InstallStatus.INSTALLED;

public class MenuActivity extends ListActivity {

    private static final String REPO_URL = "https://github.com/andresoviedo/android-3D-model-viewer/raw/master/models/index";
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1000;
    private static final int REQUEST_INTERNET_ACCESS = 1001;
    private static final int REQUEST_CODE_OPEN_FILE = 1101;
    private static final int REQUEST_CODE_OPEN_MATERIAL = 1102;
    private static final int REQUEST_CODE_OPEN_TEXTURE = 1103;
    public static final int REQUEST_CODE_QR_CODE = 1104;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1005;
    private static final int DUMMY = 1892;
    private static final String SUPPORTED_FILE_TYPES_REGEX = "(?i).*\\.(obj|stl|dae|gltf)";

    private Map<String, Object> loadModelParameters = new HashMap<>();
    private LanguageManager lang = LanguageManager.GetInstance();
    SharedPreferences sPref;
    public static ProgressDialog atlas_loading_dialog = null;

    String [] menu_items = new String[]{
            lang.Get(Tokens.scull),
            lang.Get(Tokens.heart),
            lang.Get(Tokens.map),
            lang.Get(Tokens.microscope),
            lang.Get(Tokens.termokauter),
            lang.Get(Tokens.back)
    };

    private final String prefsId = "ui";
    private final String languageId = "style";

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

        setListAdapter(new ArrayAdapter<>(this, R.layout.activity_menu_item, menuItems));
    }

    private void RunAtlas()
    {
        atlas_loading_dialog = new ProgressDialog(MenuActivity.this);

        atlas_loading_dialog.setCancelable(false);
        atlas_loading_dialog.setMessage(lang.Get(Tokens.loading));
        atlas_loading_dialog.show();

        Intent atlas = new Intent(MenuActivity.this.getApplicationContext(), AtlasActivity.class);
        atlas.putExtra("title", lang.Get(Tokens.atlas));
        MenuActivity.this.startActivity(atlas);
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

    private LinkConventer.MuseamObj GetMuseamObjFromId(String[] menu_items, int id)
    {
        LinkConventer.MuseamObj obj = new LinkConventer.MuseamObj("none", "none", "none");

        Map<String, LinkConventer.MuseamObj> map = LinkConventer.GetInstance().ConvertManager;

        String item = menu_items[id];
        if (item.equals(lang.Get(Tokens.scull)))
            obj = map.get(LinkConventer.skull_easter);
        else if (item.equals(lang.Get(Tokens.heart)))
            obj = map.get(LinkConventer.heart_easter);
        else if (item.equals(lang.Get(Tokens.map)))
            obj = map.get(LinkConventer.map_easter);
        else if (item.equals(lang.Get(Tokens.microscope)))
            obj = map.get(LinkConventer.microscope_easter);
        else if (item.equals(lang.Get(Tokens.termokauter)))
            obj = map.get(LinkConventer.termokauter_easter);
        else if (item.equals(lang.Get(Tokens.back)))
            obj = null;

        return obj;
    }

    public void AR()
    {
        if (checkAR_Permission())
        {
            ContentUtils.showListDialog(this, lang.Get(Tokens.items),
                    menu_items,
                    (dialog, which) ->
                    {
                        LinkConventer.MuseamObj obj = GetMuseamObjFromId(menu_items, which);

                        if (obj == null)
                            return;

                        ArCoreHelper.showArObject(
                                getApplicationContext(),
                                obj.ar_link,
                                lang.Get(obj.name));
                    });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, DUMMY);

        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.INTERNET}, DUMMY);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, DUMMY);

        _loadLanguagePreferences();
        setContentView(R.layout.activity_menu);
        _UpdateMenuItems();
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

    void about()
    {
        Intent aboutIntent = new Intent(MenuActivity.this.getApplicationContext(), TextActivity.class);
        aboutIntent.putExtra("title", lang.Get(Tokens.about));
        aboutIntent.putExtra("text", "# Unimplemented");
        MenuActivity.this.startActivity(aboutIntent);
    }

    void help()
    {
        Intent helpIntent = new Intent(MenuActivity.this.getApplicationContext(), TextActivity.class);
        helpIntent.putExtra("title", lang.Get(Tokens.help));
        helpIntent.putExtra("text", getResources().getString(R.string.help_text));
        MenuActivity.this.startActivity(helpIntent);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        try {
            String option = (String) getListView().getItemAtPosition(position);

            if (option.equals(lang.Get(Tokens.scanQR_AR)))
                startQRActivity(checkAR_Permission());
            else if (option.equals(lang.Get(Tokens.AR)))
                AR();
            else if (option.equals(lang.Get(Tokens.atlas)))
                RunAtlas();
            else if (option.equals(lang.Get(Tokens.viewItems)))
                loadModelFromAssets();
            else if(option.equals(lang.Get(Tokens.scanQR)))
                startQRActivity(false);
            else if (option.equals(lang.Get(Tokens.language)))
                LanguageSettings();
            else if (option.equals(lang.Get(Tokens.about)))
                about();
            else if (option.equals(lang.Get(Tokens.help)))
                help();
            else if (option.equals(lang.Get(Tokens.exit)))
                MenuActivity.this.finish();
            }
        catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void startQRActivity(boolean isAR)
    {
        Intent qrCodeIntent = new Intent(MenuActivity.this.getApplicationContext(), SimpleScannerActivity.class);
        qrCodeIntent.putExtra(SimpleScannerActivity.AR_Status, isAR);
        MenuActivity.this.startActivityForResult(qrCodeIntent, REQUEST_CODE_QR_CODE);
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

    private void loadModelFromAssets() {
        ContentUtils.showListDialog(this, lang.Get(Tokens.items),
                        menu_items,
                        (dialog, which) ->
                        {
                            LinkConventer.MuseamObj obj = GetMuseamObjFromId(menu_items, which);

                            if (obj == null)
                                return;

                            ContentUtils.provideAssets(this);
                            launchModelRendererActivity(Uri.parse("assets://" + getPackageName() + "/" + obj.local_link));
                });
    }

    private void loadModelFromRepository() {
        if (!AndroidUtils.checkPermission(this, Manifest.permission.INTERNET, REQUEST_INTERNET_ACCESS)) {
            return;
        }
        new LoadRepoIndexTask().execute();
    }

    class LoadRepoIndexTask extends AsyncTask<Void, Integer, List<String>> {

        private final ProgressDialog dialog;

        public LoadRepoIndexTask() {
            this.dialog = new ProgressDialog(MenuActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Loading...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            return ContentUtils.getIndex(REPO_URL);
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            if (strings == null){
                Toast.makeText(MenuActivity.this, "Couldn't load repo index", Toast.LENGTH_LONG).show();
                return;
            }
            ContentUtils.createChooserDialog(MenuActivity.this, "Select file", null,
                    strings, SUPPORTED_FILE_TYPES_REGEX,
                    (String file) -> {
                        if (file != null) {
                            launchModelRendererActivity(Uri.parse(file));
                        }
                    });
        }
    }

    private void loadModelFromSdCard() {
        // check permission starting from android API 23 - Marshmallow
        if (!AndroidUtils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_READ_EXTERNAL_STORAGE)) {
            return;
        }
        FileUtils.createChooserDialog(this, "Select file", null, null, SUPPORTED_FILE_TYPES_REGEX,
                (File file) -> {
                    if (file != null) {
                        ContentUtils.setCurrentDir(file.getParentFile());
                        launchModelRendererActivity(Uri.parse("file://" + file.getAbsolutePath()));
                    }
                });
    }

    private void loadModelFromContentProvider() {
        loadModelParameters.clear();
        ContentUtils.clearDocumentsProvided();
        askForFile(REQUEST_CODE_OPEN_FILE, "*/*");
    }

    private void askForFile(int requestCode, String mimeType) {
        Intent target = ContentUtils.createGetContentIntent(mimeType);
        Intent intent = Intent.createChooser(target, "Select file");
        try {
            startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Error. Please install a file content provider", Toast.LENGTH_LONG).show();
        }
    }

    private void launch(String file){
        if (file != null) {
            ContentUtils.provideAssets(this);
            launchModelRendererActivity(Uri.parse("assets://" + getPackageName() + "/" + file));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContentUtils.setThreadActivity(this);
        switch (requestCode) {
            case REQUEST_CODE_QR_CODE:
                if(resultCode != Activity.RESULT_CANCELED && data != null){
                    launch(data.getStringExtra("file"));
                }
                break;
            case REQUEST_READ_EXTERNAL_STORAGE:
                loadModelFromSdCard();
                break;
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                RunAtlas();
                break;
            case REQUEST_INTERNET_ACCESS:
                loadModelFromRepository();
                break;
            case REQUEST_CODE_OPEN_FILE:
                if (resultCode != RESULT_OK) {
                    return;
                }
                final Uri uri = data.getData();
                if (uri == null) {
                    return;
                }

                // save user selected model
                loadModelParameters.put("model", uri);

                // detect model type
                if (uri.toString().toLowerCase().endsWith(".obj")) {
                    askForRelatedFiles(0);
                } else if (uri.toString().toLowerCase().endsWith(".stl")) {
                    askForRelatedFiles(1);
                } else if (uri.toString().toLowerCase().endsWith(".dae")) {
                    askForRelatedFiles(2);
                } else if (uri.toString().toLowerCase().endsWith(".gltf")) {
                    askForRelatedFiles(3);
                }
                else {
                    // no model type from filename, ask user...
                    ContentUtils.showListDialog(this, "Select type", new String[]{"Wavefront (*.obj)", "Stereolithography (*" +
                            ".stl)", "Collada (*.dae)", "Gltf (*.gltf)"}, (dialog, which) -> askForRelatedFiles(which));
                }
                break;
            case REQUEST_CODE_OPEN_MATERIAL:
                if (resultCode != RESULT_OK || data.getData() == null) {
                    launchModelRendererActivity(getUserSelectedModel());
                    break;
                }
                String filename = (String) loadModelParameters.get("file");
                ContentUtils.addUri(filename, data.getData());
                // check if material references texture file
                String textureFile = WavefrontLoader.getTextureFile(data.getData());
                if (textureFile == null) {
                    launchModelRendererActivity(getUserSelectedModel());
                    break;
                }
                ContentUtils.showDialog(this, "Select texture file", "This model references a " +
                                "texture file (" + textureFile + "). Please select it", "OK",
                        "Cancel", (DialogInterface dialog, int which) -> {
                            switch (which) {
                                case DialogInterface.BUTTON_NEGATIVE:
                                    launchModelRendererActivity(getUserSelectedModel());
                                    break;
                                case DialogInterface.BUTTON_POSITIVE:
                                    loadModelParameters.put("file", textureFile);
                                    askForFile(REQUEST_CODE_OPEN_TEXTURE, "image/*");
                            }
                        });
                break;
            case REQUEST_CODE_OPEN_TEXTURE:
                if (resultCode != RESULT_OK || data.getData() == null) {
                    launchModelRendererActivity(getUserSelectedModel());
                    break;
                }
                String textureFilename = (String) loadModelParameters.get("file");
                ContentUtils.addUri(textureFilename, data.getData());
                launchModelRendererActivity(getUserSelectedModel());
        }
    }

    private Uri getUserSelectedModel() {
        return (Uri) loadModelParameters.get("model");
    }

    private void askForRelatedFiles(int modelType) {
        loadModelParameters.put("type", modelType);
        switch (modelType) {
            case 0: // obj
                // check if model references material file
                String materialFile = WavefrontLoader.getMaterialLib(getUserSelectedModel());
                if (materialFile == null) {
                    launchModelRendererActivity(getUserSelectedModel());
                    break;
                }
                ContentUtils.showDialog(this, "Select material file", "This model references a " +
                                "material file (" + materialFile + "). Please select it", "OK",
                        "Cancel", (DialogInterface dialog, int which) -> {
                            switch (which) {
                                case DialogInterface.BUTTON_NEGATIVE:
                                    launchModelRendererActivity(getUserSelectedModel());
                                    break;
                                case DialogInterface.BUTTON_POSITIVE:
                                    loadModelParameters.put("file", materialFile);
                                    askForFile(REQUEST_CODE_OPEN_MATERIAL, "*/*");
                            }
                        });
                break;
            case 1: // stl
                launchModelRendererActivity(getUserSelectedModel());
                break;
            case 2: // dae
                // TODO: pre-process file to ask for referenced textures
                // XmlParser.parse(colladaFile)
                launchModelRendererActivity(getUserSelectedModel());
                break;
            case 3: // gltf
                launchModelRendererActivity(getUserSelectedModel());
                break;

        }
    }

    private void launchModelRendererActivity(Uri uri) {
        Log.i("Menu", "Launching renderer for '" + uri + "'");
        Intent intent = new Intent(getApplicationContext(), ModelActivity.class);
        intent.putExtra("uri", uri.toString());
        intent.putExtra("immersiveMode", "true");

        // content provider case
        if (!loadModelParameters.isEmpty()) {
            intent.putExtra("type", loadModelParameters.get("type").toString());
            loadModelParameters.clear();
        }

        startActivity(intent);
    }
}

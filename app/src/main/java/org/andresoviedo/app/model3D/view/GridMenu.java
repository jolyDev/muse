package org.andresoviedo.app.model3D.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.google.zxing.Result;

import org.andresoviedo.android_3d_model_engine.services.collada.ColladaLoader;
import org.andresoviedo.android_3d_model_engine.services.wavefront.WavefrontLoader;
import org.andresoviedo.app.model3D.Atlas.AtlasActivity;
import org.andresoviedo.app.model3D.DevTools.ArCoreHelper;
import org.andresoviedo.app.model3D.DevTools.LinkConventer;
import org.andresoviedo.app.model3D.DevTools.MenuItemsHolder;
import org.andresoviedo.app.model3D.DevTools.NetworkManager;
import org.andresoviedo.app.model3D.DevTools.PermissionManager;
import org.andresoviedo.lang.LanguageManager;
import org.andresoviedo.lang.Tokens;
import org.andresoviedo.util.android.AndroidUtils;
import org.andresoviedo.util.android.AssetUtils;
import org.andresoviedo.util.android.ContentUtils;
import org.andresoviedo.util.android.FileUtils;
import org.andresoviedo.util.view.TextActivity;
import org.apache.commons.io.FilenameUtils;
import org.nmmu.R;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.google.ar.core.ArCoreApk.InstallStatus.INSTALLED;

public class GridMenu extends Activity {

    GridLayout gridView;
    TextView menuTitle;

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1000;
    private static final int REQUEST_INTERNET_ACCESS = 1001;
    private static final int REQUEST_READ_CONTENT_PROVIDER = 1002;

    private static final int REQUEST_CODE_LOAD_MODEL = 1101;
    private static final int REQUEST_CODE_OPEN_MATERIAL = 1102;
    private static final int REQUEST_CODE_OPEN_TEXTURE = 1103;
    private static final int REQUEST_CODE_QR_CODE = 1104;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1105;
    private static final int REQUEST_CODE_ADD_FILES = 1200;
    private static final String SUPPORTED_FILE_TYPES_REGEX = "(?i).*\\.(obj|stl|dae)";
    private static final String REPO_URL = "https://github.com/andresoviedo/android-3D-model-viewer/raw/master/models/index";

    private static GridMenu main = null;

    public static GridMenu getThis() {
        return main;
    }

    /**
     * Load file user data
     */
    private Map<String, Object> loadModelParameters = new HashMap<>();
    private LanguageManager lang = LanguageManager.GetInstance();
    SharedPreferences sPref;
    public static ProgressDialog atlas_loading_dialog = null;
    private final String prefsId = "ui";
    private final String languageId = "style";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionManager.CheckAndAskAll(GridMenu.this);

        setContentView(R.layout.activity_grid_menu);

        gridView = (GridLayout) findViewById(R.id.grid_items);
        menuTitle = (TextView) findViewById(R.id.menuTitle);

        _loadLanguagePreferences();
        UpdateMenuItems();

        addActionListeners();
        main = GridMenu.this;
    }

    private void addActionListeners() {
        int childCount = gridView.getChildCount();
        Log.i("onCreate", "childCount="+childCount);

        for (int i= 0; i < childCount; i++){
            CardView container = (CardView) gridView.getChildAt(i);
            setAction(i, container);
        }
    }

    private void setAction(int i, CardView container) {
        try {
            LinearLayout linlay = (LinearLayout)container.getChildAt(0);
            TextView text = (TextView) linlay.getChildAt(1);
            String option = text.getText().toString();
            Log.i("setAction", "option="+option);
            boolean isARModeOn = ArCoreHelper.checkAR_Permission(GridMenu.this);
            if (isARModeOn && option.equals(lang.Get(Tokens.scanQR_AR)))
                container.setOnClickListener((View view)->{
                    startQRActivity(ArCoreHelper.checkAR_Permission(GridMenu.this));
                });
            else if (option.equals(lang.Get(Tokens.debug_load)))
                container.setOnClickListener((View view)->{
                    loadModel();
                });
            else if (isARModeOn && option.equals(lang.Get(Tokens.AR)))
                container.setOnClickListener((View view)->{
                    new ObjSelectorTaskAR().execute();
                });
            else if (option.equals(lang.Get(Tokens.atlas)))
                container.setOnClickListener((View view)->{
                    RunAtlas();
                });
            else if (option.equals(lang.Get(Tokens.viewItems)))
                container.setOnClickListener((View view)->{
                    new ObjSelectorTask().execute();
                });
            else if(option.equals(lang.Get(Tokens.scanQR)))
                container.setOnClickListener((View view)->{
                    startQRActivity(false);
                });
            else if (option.equals(lang.Get(Tokens.language)))
                container.setOnClickListener((View view)->{
                    LanguageSettings();
                });
            else if (option.equals(lang.Get(Tokens.about)))
                container.setOnClickListener((View view)->{
                    about();
                });
            else if (option.equals(lang.Get(Tokens.help)))
                container.setOnClickListener((View view)->{
                    help();
                });
            else if (option.equals(lang.Get(Tokens.exit)))
                container.setOnClickListener((View view)->{
                    finishAndRemoveTask();
                });
        }
        catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void _loadLanguagePreferences()
    {
        sPref = getSharedPreferences(prefsId, MODE_PRIVATE);
        lang.code = sPref.getInt(languageId, lang.ENG);
    }

    public void UpdateMenuItems()
    {
        String[] menuItems = MenuItemsHolder.GetMainMenuItems(ArCoreHelper.checkAR_Permission(GridMenu.this));

        int childCount = gridView.getChildCount();

        for (int i= 0; i < childCount; i++){
            CardView container = (CardView) gridView.getChildAt(i);
            LinearLayout linlay = (LinearLayout)container.getChildAt(0);
            TextView text = (TextView) linlay.getChildAt(1);
            text.setText(menuItems[i]);
        }

        menuTitle.setText(lang.Get(Tokens.museName));
    }

    public void AR()
    {
        if (ArCoreHelper.checkAR_Permission(GridMenu.this))
        {
            String[] items = MenuItemsHolder.GetLocalObjectsMenuItems();
            ContentUtils.showListDialog(this, lang.Get(Tokens.items),
                    items,
                    (dialog, which) ->
                    {
                        String item = items[which];
                        if(item == null)
                            return;

                        // LinkConventer.MuseamObj obj = LinkConventer.GetInstance().ConvertManager.get(link);
                        // if(obj == null)
                        //     return;


                    });
        }
    }

    public void loadLocal()
    {
        String[] items = MenuItemsHolder.GetLocalObjectsMenuItems();
        ContentUtils.showListDialog(this, lang.Get(Tokens.items),
                items,
                (dialog, which) ->
                {
                    String obj_link = LinkConventer.GetInstance().GetObjLinkFromId(items[which]);

                    if(obj_link != null) {
                        ContentUtils.provideAssets(this);
                        launchModelRendererActivity(Uri.parse("android://" + getPackageName() + "/assets/" + obj_link));
                    }

                });
    }

    private void startQRActivity(boolean isAR)
    {
        Intent qrCodeIntent = new Intent(GridMenu.this.getApplicationContext(), SimpleScannerActivity.class);
        qrCodeIntent.putExtra(SimpleScannerActivity.AR_Status, isAR);
        qrCodeIntent.putExtra("package", getPackageName());
        GridMenu.this.startActivityForResult(qrCodeIntent, REQUEST_CODE_QR_CODE);
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
                    UpdateMenuItems();
                });
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
        Intent aboutIntent = new Intent(GridMenu.this.getApplicationContext(), org.andresoviedo.util.view.TextActivity.class);
        aboutIntent.putExtra("title", lang.Get(Tokens.about));
        aboutIntent.putExtra("text", lang.Get(Tokens.aboutDescription));
        GridMenu.this.startActivity(aboutIntent);
    }

    void help()
    {
        Intent helpIntent = new Intent(GridMenu.this.getApplicationContext(), TextActivity.class);
        helpIntent.putExtra("title", lang.Get(Tokens.help));
        helpIntent.putExtra("text", getResources().getString(R.string.help_text));
        GridMenu.this.startActivity(helpIntent);
    }

    private void RunAtlas()
    {
        atlas_loading_dialog = new ProgressDialog(GridMenu.this);
        atlas_loading_dialog.setCancelable(false);
        atlas_loading_dialog.setMessage(lang.Get(Tokens.loading));
        atlas_loading_dialog.show();
        Intent atlas = new Intent(GridMenu.this.getApplicationContext(), AtlasActivity.class);
        atlas.putExtra("title", lang.Get(Tokens.atlas));
        GridMenu.this.startActivity(atlas);
    }

    //////////////////////////////////////////////////////////
    ////////////////////// Loading ///////////////////////////
    //////////////////////////////////////////////////////////

    private void launch(String file){
        if (file != null) {
            ContentUtils.provideAssets(this);
            launchModelRendererActivity(Uri.parse("assets://" + getPackageName() + "/" + file));
        }
    }

    private void loadModel() {
        ContentUtils.showListDialog(this, "File Provider", new String[]{"Samples", "Repository",
                "File Explorer", "Android Explorer"}, (DialogInterface dialog, int which) -> {
            if (which == 0) {
                loadModelFromAssets();
            } else if (which == 1) {
                loadModelFromRepository();
            } else if (which == 2) {
                loadModelFromSdCard();
            } else {
                loadModelFromContentProvider();
            }
        });

    }

    private void loadModelFromAssets() {
        AssetUtils.createChooserDialog(this, "Select file", null, "models", "(?i).*\\.(obj|stl|dae)",
                (String file) -> {
                    if (file != null) {
                        ContentUtils.provideAssets(this);
                        launchModelRendererActivity(Uri.parse("android://"+getPackageName()+"/assets/" + file));
                    }
                });
    }

    private void loadModelFromRepository() {
        if (AndroidUtils.checkPermission(this, Manifest.permission.INTERNET, REQUEST_INTERNET_ACCESS)) {
            new GridMenu.LoadRepoIndexTask().execute();
        }
    }

    class LoadRepoIndexTask extends AsyncTask<Void, Integer, List<String>> {

        private final ProgressDialog dialog;

        public LoadRepoIndexTask() {
            this.dialog = new ProgressDialog(GridMenu.this);
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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (strings == null) {
                Toast.makeText(GridMenu.this, "Couldn't load repo index", Toast.LENGTH_LONG).show();
                return;
            }
            ContentUtils.createChooserDialog(GridMenu.this, "Select file", null,
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
        if (AndroidUtils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_READ_EXTERNAL_STORAGE)) {
            FileUtils.createChooserDialog(this, "Select file", null, null, SUPPORTED_FILE_TYPES_REGEX,
                    (File file) -> {
                        if (file != null) {
                            ContentUtils.setCurrentDir(file.getParentFile());
                            launchModelRendererActivity(Uri.parse("file://" + file.getAbsolutePath()));
                        }
                    });
        }

    }

    private void loadModelFromContentProvider() {
        // check permission starting from android API 23 - Marshmallow
        if (AndroidUtils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_READ_CONTENT_PROVIDER)) {
            loadModelParameters.clear();
            ContentUtils.clearDocumentsProvided();
            ContentUtils.setCurrentDir(null);
            askForFile(REQUEST_CODE_LOAD_MODEL, "*/*");
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContentUtils.setThreadActivity(this);
        try {
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
                case REQUEST_READ_CONTENT_PROVIDER:
                    loadModelFromContentProvider();
                    break;
                case REQUEST_INTERNET_ACCESS:
                    loadModelFromRepository();
                    break;
                case REQUEST_CODE_LOAD_MODEL:
                    if (resultCode != RESULT_OK) {
                        return;
                    }
                    final Uri uri = data.getData();
                    if (uri == null) {
                        return;
                    }
                    onLoadModel(uri);
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
                    break;
                case REQUEST_CODE_ADD_FILES:

                    // get list of files to prompt to user
                    List<String> files = (List<String>) loadModelParameters.get("files");
                    if (files == null || files.isEmpty()) {
                        launchModelRendererActivity(getUserSelectedModel());
                        break;
                    }

                    // save picked up file
                    final String current = files.remove(0);
                    ContentUtils.addUri(current, data.getData());

                    // no more files then load model...
                    if (files.isEmpty()) {
                        launchModelRendererActivity(getUserSelectedModel());
                        break;
                    }
                    ;

                    final String next = files.get(0);
                    ContentUtils.showDialog(this, "Select file", "Please select file " + next, "OK",
                            "Cancel", (DialogInterface dialog, int which) -> {
                                switch (which) {
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        launchModelRendererActivity(getUserSelectedModel());
                                        break;
                                    case DialogInterface.BUTTON_POSITIVE:
                                        askForFile(REQUEST_CODE_ADD_FILES, "image/*");
                                }
                            });

                    break;
            }
        } catch (Exception ex) {
            Log.e("MenuActivity", ex.getMessage(), ex);
            Toast.makeText(this, "Unexpected exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void onLoadModel(Uri uri) throws IOException {

        // save user selected model
        loadModelParameters.put("model", uri);

        // detect model type
        if (uri.toString().toLowerCase().endsWith(".obj")) {
            askForRelatedFiles(0);
        } else if (uri.toString().toLowerCase().endsWith(".stl")) {
            askForRelatedFiles(1);
        } else if (uri.toString().toLowerCase().endsWith(".dae")) {
            askForRelatedFiles(2);
        } else {
            // no model type from filename, ask user...
            ContentUtils.showListDialog(this, "Select type", new String[]{"Wavefront (*.obj)", "Stereolithography (*" +
                    ".stl)", "Collada (*.dae)"}, (dialog, which) -> {
                try {
                    askForRelatedFiles(which);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private Uri getUserSelectedModel() {
        return (Uri) loadModelParameters.get("model");
    }

    private void askForRelatedFiles(int modelType) throws IOException {
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
                final List<String> images = ColladaLoader.getImages(ContentUtils.getInputStream(getUserSelectedModel()));
                if (images == null || images.isEmpty()) {
                    launchModelRendererActivity(getUserSelectedModel());
                } else {

                    Log.i("MenuActivity", "Prompting user to choose files from picker...");

                    loadModelParameters.put("files", images);
                    String file = images.get(0);

                    ContentUtils.showDialog(this, "Select texture", "This model references a " +
                                    " file (" + file + "). Please select it", "OK",
                            "Cancel", (DialogInterface dialog, int which) -> {
                                switch (which) {
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        launchModelRendererActivity(getUserSelectedModel());
                                        break;
                                    case DialogInterface.BUTTON_POSITIVE:
                                        askForFile(REQUEST_CODE_ADD_FILES, "*/*");
                                }
                            });
                }
                break;
        }
    }

    public void launchModelRendererActivity(Uri uri) {
        Log.i("Menu", "Launching renderer for '" + uri + "'");
        Intent intent = new Intent(getApplicationContext(), ModelActivity.class);
        try {
            URI.create(uri.toString());
            intent.putExtra("uri", uri.toString());
        } catch (Exception e) {
            // info: filesystem url may contain spaces, therefore we re-encode URI
            try {
                intent.putExtra("uri", new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), uri.getQuery(), uri.getFragment()).toString());
            } catch (URISyntaxException ex) {
                Toast.makeText(this, "Error: " + uri.toString(), Toast.LENGTH_LONG).show();
                return;
            }
        }
        intent.putExtra("immersiveMode", "false");

        // content provider case
        if (!loadModelParameters.isEmpty()) {
            intent.putExtra("type", loadModelParameters.get("type").toString());
            //intent.putExtra("backgroundColor", "0.25 0.25 0.25 1");
            loadModelParameters.clear();
        }

        startActivity(intent);
    }

    ///////////////////////////////////////////////////
    /////////// Loader Task ///////////////////////////
    ///////////////////////////////////////////////////

    public class ObjSelectorTask extends AsyncTask<Void, Integer, List<String>> {

        private final ProgressDialog dialog;
        private String link = NetworkManager.GetInstance().GitCompatIndexLink;
        private String[] localItems = MenuItemsHolder.GetLocalObjectsMenuItems();
        private String[] remoteItems = null;

        public ObjSelectorTask() {
            this.link = link;
            this.dialog = new ProgressDialog(GridMenu.this);
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
            List<String> result = ContentUtils.getIndex(link);
            ContentUtils.UpdateAssetsList(result);
            return result;
        }

        private String[] GetRemoteDialogList(List<String> strings)
        {
            List<String> items = new ArrayList<String>();

            for (int i = 0; i < strings.size(); i++)
                if(strings.get(i).length() > 0 && strings.get(i).charAt(0) == '+')
                    items.add(strings.get(i).split(",")[LanguageManager.GetInstance().code + 1]);

            return items.stream().toArray(String[]::new);
        }

        private String ExtractRemoteObjLink(int index, List<String> raw_git_index_file_content)
        {
            if (index < 0)
                return "";

            int index_iter = 0;
            for (int i = 0; i < raw_git_index_file_content.size(); i++)
                if(raw_git_index_file_content.get(i).length() > 0 && raw_git_index_file_content.get(i).charAt(0) == '+')
                {
                    if (index_iter == index)
                        return raw_git_index_file_content.get(i + 1);

                    index_iter++;
                }

            return "";
        }

        private String[] GetDialogItemsList(List<String> strings)
        {
            remoteItems = GetRemoteDialogList(strings);

            String[] result = new String[remoteItems.length + localItems.length];

            int i = 0;
            for (; i < remoteItems.length; i++)
                result[i] = remoteItems[i];

            for (int j = 0; j < localItems.length; j++, i++)
                result[i] = localItems[j];

            return result;
        }

        private void ClickHandler(int index, List<String> strings)
        {
            if (index >= 0) {
                if (index < remoteItems.length)
                {
                    launchModelRendererActivity(Uri.parse(
                            ExtractRemoteObjLink(index, strings)));
                }
                else if (index >= remoteItems.length && index < localItems.length + remoteItems.length) {
                    index -= remoteItems.length; // make index relative to locals
                    if (localItems[index] != LanguageManager.GetInstance().Get(Tokens.back)) {
                        String obj_link = LinkConventer.GetInstance().GetObjLinkFromId(localItems[index]);

                        if(obj_link != null) {
                            ContentUtils.provideAssets(GridMenu.this);
                            launchModelRendererActivity(Uri.parse("android://"+getPackageName()+"/assets/" + obj_link));
                        }
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (strings == null) {
                Toast.makeText(GridMenu.this, "No internet!", Toast.LENGTH_LONG).show();
                return;
            }

            ContentUtils.showListDialog(GridMenu.this, LanguageManager.GetInstance().Get(Tokens.items),
                    GetDialogItemsList(strings),
                    (dialog, which) -> {
                        ClickHandler(which, strings);
                    });
        }
    }

    ///////////////////////////////////////////////////
    /////////// Loader Task AR /////////////////////////
    ///////////////////////////////////////////////////

    public class ObjSelectorTaskAR extends AsyncTask<Void, Integer, List<String>> {

        private final ProgressDialog dialog;
        private String link = NetworkManager.GetInstance().GitARIndexLink;
        private String[] remoteItems = null;

        public ObjSelectorTaskAR() {
            this.dialog = new ProgressDialog(GridMenu.this);
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
            List<String> result = ContentUtils.getIndex(link);
            ContentUtils.UpdateAssetsList(result);
            return result;
        }

        private String[] GetRemoteDialogList(List<String> strings)
        {
            remoteItems = new String[strings.size() / 2];

            for (int i = 0; i < strings.size(); i+=2)
                remoteItems[i / 2] = strings.get(i).split(",")[LanguageManager.GetInstance().code + 1];

            return remoteItems;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (strings == null) {
                Toast.makeText(GridMenu.this, "No internet!", Toast.LENGTH_LONG).show();
                return;
            }

            while (strings.remove(""));

            ContentUtils.showListDialog(GridMenu.this, LanguageManager.GetInstance().Get(Tokens.items),
                    GetRemoteDialogList(strings),
                    (dialog, which) -> {
                        if (which >= 0 && which < remoteItems.length)
                        ArCoreHelper.showArObject(getApplicationContext(),
                                strings.get(which * 2 + 1),
                                strings.get(which * 2));
                    });
        }
    }
}
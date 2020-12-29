package org.andresoviedo.app.model3D.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import org.andresoviedo.app.model3D.DevTools.LinkConventer;
import org.andresoviedo.app.model3D.DevTools.ArCoreHelper;
import org.andresoviedo.lang.LanguageManager;
import org.andresoviedo.lang.Tokens;
import org.apache.commons.io.FilenameUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleScannerActivity extends Activity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    public static final String AR_Status = "AR status";

    private final Set<String> imageExts = new HashSet<>(Arrays.asList("png", "jpg", "jpeg", "gif", "PNG", "JPG", "JPEG", "GIF"));

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        if (!isPermissionGranted())
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);

        if (!isPermissionGranted())
            finish();
        else {
            mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
            setContentView(mScannerView);                // Set the scanner view as the content view
        }
    }

    private boolean isPermissionGranted()
    {
        return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            String link_key = rawResult.getText();
            Log.i("Scanner result", "model = " + link_key);
            if (link_key.isEmpty()) {
                Toast.makeText(this, Tokens.incorrectQR, Toast.LENGTH_SHORT).show();
            }

            if (imageExts.contains(FilenameUtils.getExtension(link_key))) {
                Intent imageIntent = new Intent(SimpleScannerActivity.this.getApplicationContext(), ImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", link_key);
                imageIntent.putExtras(bundle);
                SimpleScannerActivity.this.startActivity(imageIntent);
            } else if (FilenameUtils.getExtension(link_key).endsWith("txt")) {
                Intent textIntent = new Intent(SimpleScannerActivity.this.getApplicationContext(), TextActivity.class);
                Bundle bundle = new Bundle();

                String langSpecificFileName = LanguageManager.GetInstance().GetPrefix()
                        + link_key.substring(link_key.lastIndexOf("/") + 1);
                String originBase = link_key.substring(0, link_key.lastIndexOf("/") + 1);

                bundle.putString("url", originBase + langSpecificFileName);
                textIntent.putExtras(bundle);
                SimpleScannerActivity.this.startActivity(textIntent);
            } else {
                if (getIntent().getBooleanExtra(SimpleScannerActivity.AR_Status, false)) {

                    ArCoreHelper.showArObject(
                            getApplicationContext(),
                            link_key,
                            "Name please calculate");
                } else {
                    String obj_link = LinkConventer.GetInstance().GetObjLinkFromId(link_key);

                    if (obj_link != null) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("file", obj_link);
                        setResult(Activity.RESULT_OK, resultIntent);
                    }
                }

            }
        }
        catch(Exception e){
            Toast.makeText(this, Tokens.incorrectQR, Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
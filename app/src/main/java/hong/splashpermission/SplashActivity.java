package hong.splashpermission;

/**
 * Created by Hong on 2017-02-07.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.widget.Toast;

public class SplashActivity extends Activity {

    // wait time
    private final int SPLASH_DISPLAY_LENGTH = 500;
    private int SDK_INT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SDK_INT = Build.VERSION.SDK_INT;

        initStatusBarColor();
        initPermissionCheck();

    }

    private void initStatusBarColor() {
        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.splashBackground));
        }
    }

    private void initPermissionCheck() {

        String[] permissionList = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (SDK_INT >= Build.VERSION_CODES.M) {

            int permissionResult = PackageManager.PERMISSION_GRANTED;
            for (int i = 0; i < permissionList.length; i++) {
                if (checkSelfPermission(permissionList[i]) == PackageManager.PERMISSION_DENIED) {
                    permissionResult = PackageManager.PERMISSION_DENIED;
                    requestPermissions(permissionList, 1000);
                    break;
                }
            }

            if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                start();
            }

        } else {
            start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1000) {

            int grantResult = PackageManager.PERMISSION_GRANTED;
            if (SDK_INT >= Build.VERSION_CODES.M) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (checkSelfPermission(permissions[i]) == PackageManager.PERMISSION_DENIED) {
                        grantResult = checkSelfPermission(permissions[i]);
                        break;
                    }
                }
            }

            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "권한 요청을 거부했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "권한 요청을 승인했습니다.", Toast.LENGTH_SHORT).show();
                start();
            }
        }
    }

    private void start() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}

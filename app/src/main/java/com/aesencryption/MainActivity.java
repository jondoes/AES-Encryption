package com.aesencryption;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.liquidplayer.webkit.javascriptcore.JSContext;
import org.liquidplayer.webkit.javascriptcore.JSValue;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText edtPassword,edtPlainText,edtOutPut;
        edtPassword= (EditText) findViewById(R.id.edtPassword);
        edtPlainText= (EditText) findViewById(R.id.edtPlainText);
        edtOutPut= (EditText) findViewById(R.id.edtOutPut);


        findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoardFromCurrentFocus(MainActivity.this);
                edtPassword.setText("");
                edtPlainText.setText("");
                edtOutPut.setText("");
                edtPassword.requestFocus();
            }
        });
        findViewById(R.id.btnDoEncrypt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoardFromCurrentFocus(MainActivity.this);
                edtOutPut.setText(doEncryptDecrypt("encrypt",edtPlainText.getText().toString(),edtPassword.getText().toString()));
            }
        });
        findViewById(R.id.btnDoDecrypt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoardFromCurrentFocus(MainActivity.this);
                edtPlainText.setText(edtOutPut.getText());
                edtOutPut.setText(doEncryptDecrypt("decrypt",edtPlainText.getText().toString(),edtPassword.getText().toString()));
            }
        });
    }

    /**
     * Return chipper text or plain text.
     * @param property Name of function e.g encrypt for Encryption & decrypt for decryption.
     * @param text Plain text or decrypted text.
     * @param password Encrypt decrypt password.
     * @return chipper text or plain text.
     */
    private String doEncryptDecrypt(String property,String text,String password){
        JSContext jsContext = new JSContext();
        jsContext.evaluateScript(readAssetFile("aes.js"));
        JSValue encryptFunction = jsContext.property(property);
        return encryptFunction.toFunction().call(null,text,password).toString();
    }
    /**
     *
     * @param inFile File name to be read from assert folder here will read javascript file.
     * @return Content of file
     */
    public String readAssetFile(String inFile) {
        String tContents = "";
        try {
            InputStream stream = getAssets().open(inFile);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (Exception e) {
            // Handle exceptions here
            e.printStackTrace();
        }
        return tContents;
    }
    /**
     * Hide key board from current focus in activity.
     *
     * @param activity Current activity object.
     */
    public void hideKeyBoardFromCurrentFocus(Activity activity) {
        // TODO Auto-generated method stub
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
